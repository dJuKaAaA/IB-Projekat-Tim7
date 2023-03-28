package ib.projekat.IBprojekat.service.interf;

import ib.projekat.IBprojekat.dto.request.CertificateRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ICertificateService {

    PaginatedResponseDto<CertificateResponseDto> getForUser(Long userId, Pageable pageable);
    CertificateResponseDto create(CertificateRequestDto certificateRequest);

}

