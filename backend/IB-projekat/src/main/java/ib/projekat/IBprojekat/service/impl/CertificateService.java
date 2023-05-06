package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.certificate.Base64Utility;
import ib.projekat.IBprojekat.certificate.CertificateGenerator;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreReader;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreWriter;
import ib.projekat.IBprojekat.constant.CertificateDemandStatus;
import ib.projekat.IBprojekat.constant.CertificateType;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.CertificateDemandRepository;
import ib.projekat.IBprojekat.dao.CertificateRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.UploadedCertificateRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import ib.projekat.IBprojekat.dto.response.UserRefResponseDto;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import ib.projekat.IBprojekat.entity.CertificateEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.exception.*;
import ib.projekat.IBprojekat.service.interf.ICertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Stack;

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
    private final GlobalConstants globalConstants;

    // return all certificate
    @Override
    public PaginatedResponseDto<CertificateResponseDto> getAll(Pageable pageable) {
        Page<CertificateEntity> certificatesPage = certificateRepository.findAll(pageable);

        Collection<CertificateResponseDto> certificatesResponse = certificatesPage.getContent().stream()
                .map(this::convertToDto)
                .toList();
        return new PaginatedResponseDto<>(
                certificatesPage.getPageable().getPageNumber(),
                certificatesResponse.size(),
                certificatesResponse
        );
    }

    // return all certificates for specific user
    @Override
    public PaginatedResponseDto<CertificateResponseDto> getForUser(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Page<CertificateEntity> certificatesPage = certificateRepository.findByUserId(userId, pageable);

        Collection<CertificateResponseDto> certificates = certificatesPage.getContent().stream()
                .map(this::convertToDto)
                .toList();

        return new PaginatedResponseDto<>(
                certificatesPage.getPageable().getPageNumber(),
                certificates.size(),
                certificates
        );
    }

    // create certificate base on the certificate demand
    @Override
    public CertificateResponseDto create(Long demandId) {
        CertificateDemandEntity certificateDemand = certificateDemandRepository.findById(demandId)
                .orElseThrow(CertificateDemandNotFoundException::new);
        if (certificateDemand.getStatus() != CertificateDemandStatus.PENDING) {
            throw new CertificateDemandException("Cannot create certificates from demands that are not pending!");
        }

        // generating the new key pair
        KeyPair keyPair = certificateGenerator.generateKeyPair();

        // fetching signer private key
        // if the requested signing certificate is null then it is supposed to be self-signed
        PrivateKey signerPrivateKey;
        CertificateEntity signerCertificateEntity;

        // if is not root certificate
        // load signer private key form keystore
        if (certificateDemand.getRequestedSigningCertificate() != null) {
            signerCertificateEntity = certificateDemand.getRequestedSigningCertificate();
            signerPrivateKey = keyStoreReader.readPrivateKey(
                    globalConstants.jksCertificatesPath,
                    globalConstants.jksPassword,
                    signerCertificateEntity.getSerialNumber(),
                    globalConstants.jksEntriesPassword
            );

            // end certificate cant sing another certificate
            if (signerCertificateEntity.getType() == CertificateType.END) {
                throw new CannotSignCertificateException("End certificates cannot sign other certificates!");
            }

            checkValidity(signerCertificateEntity.getSerialNumber());

        } else {
            // it's root certificate
            signerCertificateEntity = null;
            signerPrivateKey = keyPair.getPrivate();
        }

        // generating the certificate
        X509Certificate certificate = certificateGenerator.generateCertificate(
                certificateDemand.getRequester(),
                certificateDemand.getRequestedIssuer(),
                keyPair.getPublic(),
                signerPrivateKey
        );

        // save certificate to keystore
        keyStoreWriter.loadKeyStore(globalConstants.jksCertificatesPath, globalConstants.jksPassword.toCharArray());
        keyStoreWriter.write(
                certificate.getSerialNumber().toString(),
                keyPair.getPrivate(),
                globalConstants.jksEntriesPassword.toCharArray(),
                new X509Certificate[]{ certificate }
        );
        keyStoreWriter.saveKeyStore(globalConstants.jksCertificatesPath, globalConstants.jksPassword.toCharArray());

        // save certificate entity to database
        CertificateEntity certificateEntity = CertificateEntity.builder()
                .serialNumber(certificate.getSerialNumber().toString())
                .type(certificateDemand.getType())
                .issuedTo(certificateDemand.getRequester())
                .issuer(certificateDemand.getRequestedIssuer())
                .startDate(certificate.getNotBefore())
                .endDate(certificate.getNotAfter())
                .publicKey(certificate.getPublicKey())
                .signature(certificate.getSignature())
                .build();
        certificateEntity = certificateRepository.save(certificateEntity);

        // if it's root certificate just set that it's self-signed
        if (signerCertificateEntity == null) {
            certificateEntity.setSigner(certificateEntity);
            certificateDemand.setRequestedSigningCertificate(certificateEntity);
        } else {
            certificateEntity.setSigner(signerCertificateEntity);
        }
        certificateEntity = certificateRepository.save(certificateEntity);

        // update demand status
        // we accept the certificate creation request
        certificateDemand.setStatus(CertificateDemandStatus.ACCEPTED);
        certificateDemandRepository.save(certificateDemand);

        return convertToDto(certificateEntity);
    }

    @Override
    public void checkValidity(String serialNumber) {
        // find certificate
        CertificateEntity certificateEntity = certificateRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new CertificateNotFoundException("Certificate not found!"));

        // read certificate from key store
        X509Certificate certificate = (X509Certificate) keyStoreReader.readCertificate(
                globalConstants.jksCertificatesPath,
                globalConstants.jksPassword,
                certificateEntity.getSerialNumber()
        );

        // read signer certificate
        X509Certificate signerCertificate = (X509Certificate) keyStoreReader.readCertificate(
                globalConstants.jksCertificatesPath,
                globalConstants.jksPassword,
                certificateEntity.getSigner().getSerialNumber()
        );

        // iterate through the chain
        while (certificateEntity.getType() != CertificateType.ROOT) {
            if (certificateEntity.isPulled()) {
                throw new InvalidCertificateException("This certificate (or the one in it's chain) has been pulled by it's owner (or the admin)!");
            }

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

            // load new certificate from chain
            certificateEntity = certificateEntity.getSigner();
            certificate = (X509Certificate) keyStoreReader.readCertificate(
                    globalConstants.jksCertificatesPath,
                    globalConstants.jksPassword,
                    certificateEntity.getSerialNumber()
            );
            signerCertificate = (X509Certificate) keyStoreReader.readCertificate(
                    globalConstants.jksCertificatesPath,
                    globalConstants.jksPassword,
                    certificateEntity.getSigner().getSerialNumber()
            );
        }

        // root validation and verification
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

    }

    @Override
    public void checkValidityFromUploadedCertificate(UploadedCertificateRequestDto uploadedCertificateRequest) {
        byte[] certBytes = Base64.getDecoder().decode(uploadedCertificateRequest.getBase64Certificate());

        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(certBytes));

            checkValidity(cert);
        } catch (CertificateException e) {
            throw new InvalidCertificateException(e.getMessage());
        }
    }

    private void checkValidity(X509Certificate certificate) {
        // find certificate
        CertificateEntity certificateEntity = certificateRepository
                .findBySerialNumber(String.valueOf(certificate.getSerialNumber()))
                .orElseThrow(() -> new CertificateNotFoundException("Certificate not found!"));

        // read signer certificate
        X509Certificate signerCertificate = (X509Certificate) keyStoreReader.readCertificate(
                globalConstants.jksCertificatesPath,
                globalConstants.jksPassword,
                certificateEntity.getSigner().getSerialNumber()
        );

        // iterate through the chain
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

            // load new certificate from chain
            certificateEntity = certificateEntity.getSigner();
            certificate = (X509Certificate) keyStoreReader.readCertificate(
                    globalConstants.jksCertificatesPath,
                    globalConstants.jksPassword,
                    certificateEntity.getSerialNumber()
            );
            signerCertificate = (X509Certificate) keyStoreReader.readCertificate(
                    globalConstants.jksCertificatesPath,
                    globalConstants.jksPassword,
                    certificateEntity.getSigner().getSerialNumber()
            );
        }

        // root validation and verification
        if (certificateEntity.isPulled()) {
            throw new InvalidCertificateException("This certificate (or the one in it's chain) has been pulled by the owner!");
        }

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

    }

    @Override
    public void pullCertificate(String serialNumber, String userEmail) {
        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);

        CertificateEntity certificateEntity = certificateRepository.findBySerialNumber(serialNumber)
                .orElseThrow(CertificateNotFoundException::new);

        if (userEntity.getRole() != Role.ADMIN && userEntity.getId() != certificateEntity.getIssuedTo().getId()) {
            throw new InvalidCertificateOwnerException(
                    "User with username %s is not the owner of the requested certificate!"
                            .formatted(userEntity.getEmail()));
        }

        if (certificateEntity.getType() == CertificateType.ROOT) {
            throw new CertificatePullException("Cannot pull ROOT certificates!");
        }

        Stack<CertificateEntity> certificateStack = new Stack<>();
        certificateStack.push(certificateEntity);
        while (!certificateStack.empty()) {
            certificateEntity = certificateStack.pop();
            certificateEntity.setPulled(true);
            certificateRepository.save(certificateEntity);

            Collection<CertificateEntity> childCertificates = certificateRepository.findBySignerId(certificateEntity.getId());
            for (CertificateEntity childCertificate : childCertificates) {
                if (!childCertificate.isPulled()) {
                    certificateStack.push(childCertificate);
                }
            }
        }
    }

    @Override
    public byte[] prepareCertificateForDownload(String serialNumber) throws CertificateException, IOException {
        X509Certificate certificate = (X509Certificate) keyStoreReader.readCertificate(
                globalConstants.jksCertificatesPath,
                globalConstants.jksPassword,
                serialNumber
        );
        byte[] certificateBytes = certificate.getEncoded();
        return certificateBytes;
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
                .isPulled(certificateEntity.isPulled())
                .build();
    }

}
