package ib.projekat.IBprojekat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordHistoryEntity {


    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private UserEntity user;
    private String password;
    private Date passwordCreationDate;
}
