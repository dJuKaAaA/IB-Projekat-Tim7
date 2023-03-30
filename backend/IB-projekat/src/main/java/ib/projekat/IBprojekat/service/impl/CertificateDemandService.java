package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.constant.CertificateDemandStatus;
import ib.projekat.IBprojekat.constant.CertificateType;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.CertificateDemandRepository;
import ib.projekat.IBprojekat.dao.CertificateRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateDemandResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import ib.projekat.IBprojekat.dto.response.UserRefResponseDto;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import ib.projekat.IBprojekat.entity.CertificateEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.exception.CertificateDemandException;
import ib.projekat.IBprojekat.exception.CertificateDemandNotFoundException;
import ib.projekat.IBprojekat.exception.CertificateNotFoundException;
import ib.projekat.IBprojekat.exception.UserNotFoundException;
import ib.projekat.IBprojekat.service.interf.ICertificateDemandService;
import ib.projekat.IBprojekat.service.interf.ICertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service("CertificateDemandService")
@RequiredArgsConstructor
public class CertificateDemandService implements ICertificateDemandService {

    private final CertificateDemandRepository certificateDemandRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final ICertificateService certificateService;

    @Override
    public CertificateDemandResponseDto create(CertificateDemandRequestDto certificateDemandRequest) {
        UserEntity requester = userRepository.findById(certificateDemandRequest.getRequesterId())
                .orElseThrow(() -> new UserNotFoundException("Requester not found!"));

        if (requester.getRole() != Role.ADMIN && certificateDemandRequest.getType().equals("ROOT")) {
            throw new CertificateDemandException("Only admins can request root certificates!");
        }
        if (!certificateDemandRequest.getType().equals("ROOT") && certificateDemandRequest.getRequestedSigningCertificateId() == null) {
            throw new CertificateDemandException("Requested signing certificate id not provided");
        }

        CertificateEntity requestedSigningCertificate;
        UserEntity requestedIssuer;
        if (certificateDemandRequest.getType().equals("ROOT")) {
            requestedSigningCertificate = null;
            requestedIssuer = requester;
        } else {
            requestedSigningCertificate = certificateRepository.findById(certificateDemandRequest.getRequestedSigningCertificateId())
                    .orElseThrow(() -> new CertificateNotFoundException("Requested signing certificate not found!"));
            requestedIssuer = requestedSigningCertificate.getIssuer();
        }

        CertificateDemandEntity certificateDemand = CertificateDemandEntity.builder()
                .type(CertificateType.valueOf(certificateDemandRequest.getType()))
                .requestedSigningCertificate(requestedSigningCertificate)
                .requestedIssuer(requestedIssuer)
                .requester(requester)
                .reason(certificateDemandRequest.getReason())
                .status(CertificateDemandStatus.PENDING)
                .build();

        certificateDemand = certificateDemandRepository.save(certificateDemand);

        // if the user request a certificate from themselves, it is accepted immediately
        // the requestedSigningCertificate being null means that the requested certificate is of type 'ROOT' and should be self-signed
        // there is already a validation above that says that only admins can create root certificates
        if (requestedSigningCertificate == null || requestedSigningCertificate.getIssuer().getId() == requester.getId()) {
            certificateService.create(certificateDemand.getId());
            certificateDemand = certificateDemandRepository.findById(certificateDemand.getId())
                    .orElseThrow(CertificateDemandNotFoundException::new);
        }

        return convertToDto(certificateDemand);
    }

    @Override
    public CertificateDemandResponseDto reject(Long id) {
        CertificateDemandEntity certificateDemand = certificateDemandRepository.findById(id)
                .orElseThrow(CertificateDemandNotFoundException::new);
        certificateDemand.setStatus(CertificateDemandStatus.REJECTED);
        certificateDemand = certificateDemandRepository.save(certificateDemand);

        return convertToDto(certificateDemand);
    }

    @Override
    public PaginatedResponseDto<CertificateDemandResponseDto> getByIssuedToId(Long issuedToId, Pageable pageable) {
        userRepository.findById(issuedToId).orElseThrow(UserNotFoundException::new);

        Page<CertificateDemandEntity> certificateDemandsPage = certificateDemandRepository.findByIssuedToId(issuedToId, pageable);

        Collection<CertificateDemandResponseDto> certificateDemands = certificateDemandsPage.getContent().stream()
                .map(this::convertToDto)
                .toList();

        return new PaginatedResponseDto<>(
                certificateDemandsPage.getPageable().getPageNumber(),
                certificateDemandsPage.getPageable().getPageSize(),
                certificateDemands
        );
    }

    private CertificateDemandResponseDto convertToDto(CertificateDemandEntity certificateDemandEntity) {
        UserRefResponseDto requesterRef = UserRefResponseDto.builder()
                .id(certificateDemandEntity.getRequester().getId())
                .email(certificateDemandEntity.getRequester().getEmail())
                .build();
        UserRefResponseDto requestedIssuerRef = UserRefResponseDto.builder()
                .id(certificateDemandEntity.getRequestedIssuer().getId())
                .email(certificateDemandEntity.getRequestedIssuer().getEmail())
                .build();
        return CertificateDemandResponseDto.builder()
                .id(certificateDemandEntity.getId())
                .type(certificateDemandEntity.getType().name())
                .requestedSigningCertificateId(certificateDemandEntity.getRequestedSigningCertificate().getId())
                .requester(requesterRef)
                .requestedIssuer(requestedIssuerRef)
                .reason(certificateDemandEntity.getReason())
                .status(certificateDemandEntity.getStatus().name())
                .build();
    }

}