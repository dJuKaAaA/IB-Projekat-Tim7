package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.dao.CertificateRepository;
import ib.projekat.IBprojekat.dto.request.CertificateRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import ib.projekat.IBprojekat.service.interf.ICertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("CertificateService")
@RequiredArgsConstructor
public class CertificateService implements ICertificateService {

    private final CertificateRepository certificateRepository;

    @Override
    public PaginatedResponseDto<CertificateResponseDto> getForUser(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public CertificateResponseDto create(CertificateRequestDto certificateRequest) {
        return null;
    }

}
