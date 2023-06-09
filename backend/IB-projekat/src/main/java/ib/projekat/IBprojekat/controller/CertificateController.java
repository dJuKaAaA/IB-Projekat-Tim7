package ib.projekat.IBprojekat.controller;

import ib.projekat.IBprojekat.dto.response.CertificateDownloadResponseDto;
import ib.projekat.IBprojekat.dto.request.UploadedCertificateRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import ib.projekat.IBprojekat.service.interf.ICertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final ICertificateService certificateService;
    private final IAuthService authService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<CertificateResponseDto>> getAll(Pageable pageable) {
        return new ResponseEntity<>(certificateService.getAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/for-user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<CertificateResponseDto>> getForUser(@PathVariable("userId") Long userId,
                                                                                   Pageable pageable,
                                                                                   Principal principal) {
        authService.checkUserIdMatchesUserEmail(userId, principal.getName());
        return new ResponseEntity<>(certificateService.getForUser(userId, pageable), HttpStatus.OK);
    }


    @PostMapping("/for-demand/{demandId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CertificateResponseDto> create(@PathVariable("demandId") Long demandId,
                                                         Principal principal) {
        authService.checkIsDemandIntendedForUser(principal.getName(), demandId);
        return new ResponseEntity<>(certificateService.create(demandId), HttpStatus.OK);
    }

    @GetMapping("/{serialNumber}/validate")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public HttpStatus validate(@PathVariable("serialNumber") String serialNumber){
        certificateService.checkValidity(serialNumber);
        return HttpStatus.NO_CONTENT;
    }

    @PostMapping("/validate-from-upload")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public HttpStatus validateFromUpload(@Valid @RequestBody UploadedCertificateRequestDto uploadedCertificateRequest){
        certificateService.checkValidityFromUploadedCertificate(uploadedCertificateRequest);
        return HttpStatus.NO_CONTENT;
    }

    @PutMapping("/{serialNumber}/retract")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public HttpStatus retract(@PathVariable("serialNumber") String serialNumber, Principal principal) {
        certificateService.retract(serialNumber, principal.getName());
        return HttpStatus.NO_CONTENT;
    }

    @GetMapping("/{serialNumber}/download")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CertificateDownloadResponseDto> download(@PathVariable String serialNumber,
                                                                   Principal principal) {
        return new ResponseEntity<>(certificateService.prepareForDownload(serialNumber, principal.getName()), HttpStatus.OK);
    }

}
