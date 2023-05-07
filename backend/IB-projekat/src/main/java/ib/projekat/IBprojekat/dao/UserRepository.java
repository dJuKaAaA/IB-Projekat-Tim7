package ib.projekat.IBprojekat.dao;

import ib.projekat.IBprojekat.constant.CertificateDemandStatus;
import ib.projekat.IBprojekat.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
}
