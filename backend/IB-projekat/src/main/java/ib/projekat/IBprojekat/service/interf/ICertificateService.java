package ib.projekat.IBprojekat.service.interf;

import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ICertificateService {

    PaginatedResponseDto<CertificateResponseDto> getAll(Pageable pageable);
    PaginatedResponseDto<CertificateResponseDto> getForUser(Long userId, Pageable pageable);
    CertificateResponseDto create(Long demandId);
    void checkValidity(Long id);
    void pullCertificate(Long id, String userEmail);

}

