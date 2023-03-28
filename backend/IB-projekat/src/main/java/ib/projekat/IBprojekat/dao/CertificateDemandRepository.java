package ib.projekat.IBprojekat.dao;

import ib.projekat.IBprojekat.entity.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateDemandRepository extends JpaRepository<CertificateEntity, Long> {
}
