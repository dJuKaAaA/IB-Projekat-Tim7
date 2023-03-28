package ib.projekat.IBprojekat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certificate_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDemandEntity {

    @Id
    @GeneratedValue
    public Long id;
    @ManyToOne
    private UserEntity requestedIssuer;
    @ManyToOne
    private UserEntity requester;


}
