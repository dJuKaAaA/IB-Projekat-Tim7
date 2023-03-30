package ib.projekat.IBprojekat.dao;

import ib.projekat.IBprojekat.dto.response.CertificateDemandResponseDto;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CertificateDemandRepository extends JpaRepository<CertificateDemandEntity, Long> {

    @Query("select certificateDemand from CertificateDemandEntity certificateDemand where certificateDemand.requestedSigningCertificate.issuedTo.id = :userId")
    Page<CertificateDemandResponseDto> findByRequestedUser(Long userId, Pageable pageable);

    // fetches the certificate demands that are requested from the user with the id 'userId'
    @Query("select certificateDemand from CertificateDemandEntity certificateDemand where certificateDemand.requestedSigningCertificate.issuer.id = :userId and certificateDemand.status = 'PENDING'")
    Page<CertificateDemandResponseDto> findPending(Long userId, Pageable pageable);

}
