package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.certificate.CertificateGenerator;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreReader;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreWriter;
import ib.projekat.IBprojekat.certificate.model.IssuerData;
import ib.projekat.IBprojekat.certificate.model.SubjectData;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.dao.CertificateDemandRepository;
import ib.projekat.IBprojekat.dao.CertificateRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.response.CertificateResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import ib.projekat.IBprojekat.dto.response.UserRefResponseDto;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import ib.projekat.IBprojekat.entity.CertificateEntity;
import ib.projekat.IBprojekat.exception.CertificateDemandNotFoundException;
import ib.projekat.IBprojekat.exception.UserNotFoundException;
import ib.projekat.IBprojekat.service.interf.ICertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;

@Service("CertificateService")
@RequiredArgsConstructor
public class CertificateService implements ICertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final CertificateDemandRepository certificateDemandRepository;
    private final CertificateGenerator certificateGenerator;
    private final KeyStoreWriter keyStoreWriter;
    private final KeyStoreReader keyStoreReader;

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
    public CertificateResponseDto create(Long signerId, Long demandId) {
        CertificateDemandEntity certificateDemand = certificateDemandRepository.findById(demandId)
                .orElseThrow(CertificateDemandNotFoundException::new);

        // TODO: Check if the signer certificate exists

        // TODO: Certificates validity

        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = new Date(System.currentTimeMillis() + GlobalConstants.oneYearInMillis);

        IssuerData issuerData = certificateGenerator.generateIssuerData(certificateDemand.getRequestedIssuer());
        SubjectData subjectData = certificateGenerator.generateSubjectData(certificateDemand.getRequester(), startDate, endDate);

        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        keyStoreWriter.loadKeyStore(GlobalConstants.jksCertificatesPath, GlobalConstants.jksPassword.toCharArray());
        keyStoreWriter.write(
                certificate.getSerialNumber().toString(),
                issuerData.getPrivateKey(),
                GlobalConstants.jksEntriesPassword.toCharArray(),
                certificate
        );

        // TODO: Put signature in certificateEntity
        CertificateEntity certificateEntity = CertificateEntity.builder()
                .serialNumber(certificate.getSerialNumber().toString())
                .type(certificateDemand.getType())
                .issuer(certificateDemand.getRequestedIssuer())
                .issuedTo(certificateDemand.getRequester())
                .startDate(startDate)
                .endDate(endDate)
                .publicKey(certificateDemand.getRequester().getPublicKey())
                .signature(new byte[] { 107, 117, 114, 97, 99})
                .build();

        certificateEntity = certificateRepository.save(certificateEntity);

        return convertToDto(certificateEntity);
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
                .publicKey(certificateEntity.getPublicKey().toString())  // mozda ne vrati bas ono sto smo mislili (public key)
                .signature(new String(certificateEntity.getSignature(), StandardCharsets.UTF_8))
                .build();
    }

}
