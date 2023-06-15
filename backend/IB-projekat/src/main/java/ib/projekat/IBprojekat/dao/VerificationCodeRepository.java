package ib.projekat.IBprojekat.dao;

import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.entity.VerificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeEntity, Long> {
    Optional<VerificationCodeEntity> findFirstByUserOrderByDateOfExpirationDesc(UserEntity userEntity);

    void deleteAllByUser(UserEntity userEntity);
}
