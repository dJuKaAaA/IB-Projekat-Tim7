package ib.projekat.IBprojekat.service.interf;

import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateDemandResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ICertificateDemandService {

    CertificateDemandResponseDto create(CertificateDemandRequestDto certificateDemandRequest);
    CertificateDemandResponseDto reject(Long id);

    CertificateDemandResponseDto accept(Long id);
    PaginatedResponseDto<CertificateDemandResponseDto> getByRequesterId(Long requesterId, Pageable pageable);

    PaginatedResponseDto<CertificateDemandResponseDto> getByRequesterIdPending(Long requesterId, Pageable pageable);

    PaginatedResponseDto<CertificateDemandResponseDto> getAllPending(Long requesterId, Pageable pageable);

}
