package ib.projekat.IBprojekat.service.interf;

import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateDemandResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ICertificateDemandService {

    CertificateDemandResponseDto create(CertificateDemandRequestDto certificateRequest);

}
