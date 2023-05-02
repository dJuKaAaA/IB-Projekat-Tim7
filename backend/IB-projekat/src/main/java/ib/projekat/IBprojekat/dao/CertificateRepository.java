package ib.projekat.IBprojekat.dao;

import ib.projekat.IBprojekat.entity.CertificateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<CertificateEntity, Long> {

    @Query("select certificate from CertificateEntity certificate where certificate.issuer.id = :userId or certificate.issuedTo.id = :userId")
    Page<CertificateEntity> findByUserId(Long userId, Pageable pageable);

    Optional<CertificateEntity> findBySerialNumber(String serialNumber);

}
