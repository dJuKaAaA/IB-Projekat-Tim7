package ib.projekat.IBprojekat.controller;

import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateDemandResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import ib.projekat.IBprojekat.service.interf.ICertificateDemandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificate-demand")
@RequiredArgsConstructor
public class CertificateDemandController {

    private final ICertificateDemandService certificateDemandService;

    @PostMapping
    public ResponseEntity<CertificateDemandResponseDto> create(@Valid @RequestBody CertificateDemandRequestDto certificateDemandRequest) {
        return new ResponseEntity<>(certificateDemandService.create(certificateDemandRequest), HttpStatus.OK);
    }

    @GetMapping("/by-issued-to/{issuedToId}")
    public ResponseEntity<PaginatedResponseDto<CertificateDemandResponseDto>> getByIssuedTo(@PathVariable("issuedToId") Long issuedToId,
                                                                                            Pageable pageable) {
        return new ResponseEntity<>(certificateDemandService.getByIssuedToId(issuedToId, pageable), HttpStatus.OK);
    }

}
