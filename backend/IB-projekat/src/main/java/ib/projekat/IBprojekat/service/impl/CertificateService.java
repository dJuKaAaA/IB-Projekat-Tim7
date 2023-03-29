package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.certificate.Base64Utility;
import ib.projekat.IBprojekat.certificate.CertificateGenerator;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreReader;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreWriter;
import ib.projekat.IBprojekat.constant.CertificateType;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.dao.CertificateDemandRepository;
import ib.projekat.IBprojekat.dao.CertificateRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.response.CertificateResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import ib.projekat.IBprojekat.dto.response.UserRefResponseDto;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import ib.projekat.IBprojekat.entity.CertificateEntity;
import ib.projekat.IBprojekat.exception.*;
import ib.projekat.IBprojekat.service.interf.ICertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Collection;

@Service("CertificateService")
@RequiredArgsConstructor
public class CertificateService implements ICertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final CertificateDemandRepository certificateDemandRepository;
    private final CertificateGenerator certificateGenerator;
    private final KeyStoreWriter keyStoreWriter;
    private final KeyStoreReader keyStoreReader;
    private final Base64Utility base64Utility;

    @Override
    public PaginatedResponseDto<CertificateResponseDto> getForUser(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Page<CertificateEntity> certificatesPage = certificateRepository.findByUserId(userId, pageable);

        Collection<CertificateResponseDto> certificates = certificatesPage.getContent().stream()
                .map(this::convertToDto)
                .toList();

        return new PaginatedResponseDto<>(
                certificatesPage.getPageable().getPageNumber(),
                certificatesPage.getPageable().getPageSize(),
                certificates
        );
    }

    @Override
    public CertificateResponseDto create(Long id, Long demandId) {
        CertificateDemandEntity certificateDemand = certificateDemandRepository.findById(demandId)
                .orElseThrow(CertificateDemandNotFoundException::new);

        CertificateEntity signerCertificateEntity = certificateRepository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException("Signer certificate not found!"));

        if (signerCertificateEntity.getType() == CertificateType.END) {
            throw new CannotSignCertificateException("End certificates cannot sign other certificates!");
        }

        // fetching signer private key
        PrivateKey signerPrivateKey = keyStoreReader.readPrivateKey(
                GlobalConstants.jksCertificatesPath,
                GlobalConstants.jksPassword,
                signerCertificateEntity.getSerialNumber(),
                GlobalConstants.jksEntriesPassword
        );

        // generating the new key pair and the certificate
        KeyPair keyPair = certificateGenerator.generateKeyPair();
        X509Certificate certificate = certificateGenerator.generateCertificate(
                certificateDemand.getRequester(),
                certificateDemand.getRequestedIssuer(),
                keyPair.getPublic(),
                signerPrivateKey
        );

        keyStoreWriter.loadKeyStore(GlobalConstants.jksCertificatesPath, GlobalConstants.jksPassword.toCharArray());
        keyStoreWriter.write(
                certificate.getSerialNumber().toString(),
                keyPair.getPrivate(),
                GlobalConstants.jksEntriesPassword.toCharArray(),
                certificate
        );
        keyStoreWriter.saveKeyStore(GlobalConstants.jksCertificatesPath, GlobalConstants.jksPassword.toCharArray());

        CertificateEntity certificateEntity = CertificateEntity.builder()
                .serialNumber(certificate.getSerialNumber().toString())
                .signer(signerCertificateEntity)
                .type(certificateDemand.getType())
                .issuer(certificateDemand.getRequestedIssuer())
                .issuedTo(certificateDemand.getRequester())
                .startDate(certificate.getNotBefore())
                .endDate(certificate.getNotAfter())
                .publicKey(certificate.getPublicKey())
                .signature(certificate.getSignature())
                .build();

        certificateEntity = certificateRepository.save(certificateEntity);

        return convertToDto(certificateEntity);
    }

    @Override
    public boolean checkValidity(Long id) {
        CertificateEntity certificateEntity = certificateRepository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException("Signer certificate not found!"));
        X509Certificate certificate = (X509Certificate) keyStoreReader.readCertificate(
                GlobalConstants.jksCertificatesPath,
                GlobalConstants.jksPassword,
                certificateEntity.getSerialNumber()
        );
        X509Certificate signerCertificate = (X509Certificate) keyStoreReader.readCertificate(
                GlobalConstants.jksCertificatesPath,
                GlobalConstants.jksPassword,
                certificateEntity.getSigner().getSerialNumber()
        );

        while (certificateEntity.getType() != CertificateType.ROOT) {
            try {
                certificate.checkValidity();
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                throw new InvalidCertificateException("Certificate is expired or is not yet valid!");
            }

            try {
                certificate.verify(signerCertificate.getPublicKey());
            } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException |
                     SignatureException e) {
                throw new SignatureIntegrityException();
            }

            certificateEntity = certificateEntity.getSigner();
            certificate = (X509Certificate) keyStoreReader.readCertificate(
                    GlobalConstants.jksCertificatesPath,
                    GlobalConstants.jksPassword,
                    certificateEntity.getSerialNumber()
            );
            signerCertificate = (X509Certificate) keyStoreReader.readCertificate(
                    GlobalConstants.jksCertificatesPath,
                    GlobalConstants.jksPassword,
                    certificateEntity.getSigner().getSerialNumber()
            );
        }

        // we verify the root
        try {
            certificate.verify(signerCertificate.getPublicKey());
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException |
                 SignatureException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private CertificateResponseDto convertToDto(CertificateEntity certificateEntity) {
        UserRefResponseDto issuerRef = UserRefResponseDto.builder()
                .id(certificateEntity.getIssuer().getId())
                .email(certificateEntity.getIssuer().getEmail())
                .build();
        UserRefResponseDto issuedToRef = UserRefResponseDto.builder()
                .id(certificateEntity.getIssuedTo().getId())
                .email(certificateEntity.getIssuedTo().getEmail())
                .build();
        return CertificateResponseDto.builder()
                .id(certificateEntity.getId())
                .serialNumber(certificateEntity.getSerialNumber())
                .type(certificateEntity.getType().name())
                .issuer(issuerRef)
                .issuedTo(issuedToRef)
                .startDate(certificateEntity.getStartDate().toString())
                .endDate(certificateEntity.getEndDate().toString())
                .publicKey(certificateEntity.getPublicKey().toString())
                .signature(base64Utility.encode(certificateEntity.getSignature()))
                .build();
    }

}
