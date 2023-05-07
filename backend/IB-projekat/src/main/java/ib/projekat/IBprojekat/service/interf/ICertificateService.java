package ib.projekat.IBprojekat.service.interf;

import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.request.UploadedCertificateRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;

@Service
public interface ICertificateService {

    PaginatedResponseDto<CertificateResponseDto> getAll(Pageable pageable);
    PaginatedResponseDto<CertificateResponseDto> getForUser(Long userId, Pageable pageable);
    CertificateResponseDto create(Long demandId);
    void checkValidity(String serialNumber);
    void checkValidityFromUploadedCertificate(UploadedCertificateRequestDto uploadedCertificateRequest);
    void pullCertificate(String serialNumber, String userEmail);

    byte[] prepareCertificateForDownload(String serialNumber) throws CertificateException, IOException;
}

