package ib.projekat.IBprojekat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "verification_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationCodeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private Date dateOfGeneration;
    private Date dateOfExpiration;

    @ManyToOne
    private UserEntity user;
}
