package ib.projekat.IBprojekat.dao;

import ib.projekat.IBprojekat.entity.PasswordHistoryEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistoryEntity, Long> {

    List<PasswordHistoryEntity> findAllByUserOrderByPasswordCreationDateDesc(UserEntity user);
}
