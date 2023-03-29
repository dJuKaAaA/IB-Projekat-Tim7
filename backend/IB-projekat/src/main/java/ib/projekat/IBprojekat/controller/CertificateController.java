package ib.projekat.IBprojekat.controller;

import ib.projekat.IBprojekat.dto.response.CertificateResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import ib.projekat.IBprojekat.service.interf.ICertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final ICertificateService certificateService;

    @GetMapping("/for-user/{userId}")
    public ResponseEntity<PaginatedResponseDto<CertificateResponseDto>> getForUser(@PathVariable("userId") Long userId,
                                                                                   Pageable pageable) {
        return new ResponseEntity<>(certificateService.getForUser(userId, pageable), HttpStatus.OK);
    }

    @PostMapping("/for-demand/{demandId}/signer-certificate/{id}")
    public ResponseEntity<CertificateResponseDto> create(@PathVariable("id") Long id,
                                                         @PathVariable("demandId") Long demandId) {
        return new ResponseEntity<>(certificateService.create(id, demandId), HttpStatus.OK);
    }

}
