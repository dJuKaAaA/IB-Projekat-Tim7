package ib.projekat.IBprojekat.dao;

import ib.projekat.IBprojekat.constant.CertificateDemandStatus;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CertificateDemandRepository extends JpaRepository<CertificateDemandEntity, Long> {

//    @Query("select certificateDemand from CertificateDemandEntity certificateDemand where certificateDemand.requestedSigningCertificate.issuedTo.id = :issuedToId")
    Page<CertificateDemandEntity> findByRequesterId(Long requesterId, Pageable pageable);

    Page<CertificateDemandEntity> findByRequesterIdAndStatus(Long requesterId, CertificateDemandStatus status, Pageable pageable);

    @Query(value = "select * from certificate_demands where status = 'PENDING'", nativeQuery = true)
    Page<CertificateDemandEntity> findAllPending(Pageable page);
    @Query(value = "select * from certificate_demands where requested_issuer_id = ?1 and status = 'PENDING'", nativeQuery = true)
    Page<CertificateDemandEntity> findPendingByRequestedIssuer(Long requesterId, Pageable page);
    @Query("select certificateDemand from CertificateDemandEntity certificateDemand where certificateDemand.requester.id = :requesterId and certificateDemand.status = 'PENDING'")
    Page<CertificateDemandEntity> findPendingByRequesterId(Long requesterId, Pageable pageable);

}
