package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.constant.CertificateType;
import ib.projekat.IBprojekat.dao.CertificateDemandRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateDemandResponseDto;
import ib.projekat.IBprojekat.dto.response.UserRefResponseDto;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.exception.UserNotFoundException;
import ib.projekat.IBprojekat.service.interf.ICertificateDemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("CertificateDemandService")
@RequiredArgsConstructor
public class CertificateDemandService implements ICertificateDemandService {

    private final CertificateDemandRepository certificateDemandRepository;
    private final UserRepository userRepository;

    @Override
    public CertificateDemandResponseDto create(CertificateDemandRequestDto certificateDemandRequest) {
        UserEntity requester = userRepository.findById(certificateDemandRequest.getRequesterId())
                .orElseThrow(() -> new UserNotFoundException("Requester not found!"));
        UserEntity requestedIssuer = userRepository.findById(certificateDemandRequest.getRequestedIssuerId())
                .orElseThrow(() -> new UserNotFoundException("Requested issuer not found!"));

        CertificateType certificateType = CertificateType.INTERMEDIATE;
        if (certificateDemandRequest.getIsEnd()) {
            certificateType = CertificateType.END;
        }

        CertificateDemandEntity certificateDemand = CertificateDemandEntity.builder()
                .type(certificateType)
                .requestedIssuer(requestedIssuer)
                .requester(requester)
                .reason(certificateDemandRequest.getReason())
                .build();

        certificateDemand = certificateDemandRepository.save(certificateDemand);

        UserRefResponseDto requesterRef = UserRefResponseDto.builder()
                .id(certificateDemand.getRequester().getId())
                .email(certificateDemand.getRequester().getEmail())
                .build();
        UserRefResponseDto requestedIssuerRef = UserRefResponseDto.builder()
                .id(certificateDemand.getRequestedIssuer().getId())
                .email(certificateDemand.getRequestedIssuer().getEmail())
                .build();
        return CertificateDemandResponseDto.builder()
                .id(certificateDemand.getId())
                .type(certificateDemand.getType().name())
                .requestedIssuer(requestedIssuerRef)
                .requester(requesterRef)
                .reason(certificateDemand.getReason())
                .build();
    }

}
