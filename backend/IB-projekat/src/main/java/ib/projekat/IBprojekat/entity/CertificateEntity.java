package ib.projekat.IBprojekat.entity;

import ib.projekat.IBprojekat.constant.CertificateType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String serialNumber;
    @ManyToOne
    private CertificateEntity signer;
    @Enumerated(EnumType.STRING)
    private CertificateType type;
    @ManyToOne
    private UserEntity issuer;
    @ManyToOne
    private UserEntity issuedTo;
    private Date startDate;
    private Date endDate;
    private byte[] publicKey;
    private byte[] signature;


}
