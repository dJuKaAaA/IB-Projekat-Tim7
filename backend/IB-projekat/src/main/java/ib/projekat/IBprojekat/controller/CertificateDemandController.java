package ib.projekat.IBprojekat.controller;

import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateDemandResponseDto;
import ib.projekat.IBprojekat.dto.response.PaginatedResponseDto;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import ib.projekat.IBprojekat.service.interf.ICertificateDemandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/certificate-demand")
@RequiredArgsConstructor
public class CertificateDemandController {

    private final ICertificateDemandService certificateDemandService;
    private final IAuthService authService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CertificateDemandResponseDto> create(@Valid @RequestBody CertificateDemandRequestDto certificateDemandRequest,
                                                               Principal principal) {
        authService.checkUserIdMatchesUserEmail(certificateDemandRequest.getRequesterId(), principal.getName());
        return new ResponseEntity<>(certificateDemandService.create(certificateDemandRequest), HttpStatus.OK);
    }

    @GetMapping("/by-requester/{requesterId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<CertificateDemandResponseDto>> getByIssuedTo(@PathVariable Long requesterId,
                                                                                            Pageable pageable,
                                                                                            Principal principal) {
        authService.checkUserIdMatchesUserEmail(requesterId, principal.getName());
        return new ResponseEntity<>(certificateDemandService.getByRequesterId(requesterId, pageable), HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CertificateDemandResponseDto> reject(@PathVariable Long id, Principal principal){
        authService.checkIsDemandIntendedForUser(principal.getName(), id);
        return new ResponseEntity<>(certificateDemandService.reject(id), HttpStatus.OK);
    }

}
