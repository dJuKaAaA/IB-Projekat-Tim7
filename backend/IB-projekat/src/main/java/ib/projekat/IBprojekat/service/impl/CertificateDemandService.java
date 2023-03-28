package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateDemandResponseDto;
import ib.projekat.IBprojekat.service.interf.ICertificateDemandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("CertificateDemandService")
@RequiredArgsConstructor
public class CertificateDemandService implements ICertificateDemandService {

    @Override
    public CertificateDemandResponseDto create(CertificateDemandRequestDto certificateRequest) {
        return null;
    }

}
