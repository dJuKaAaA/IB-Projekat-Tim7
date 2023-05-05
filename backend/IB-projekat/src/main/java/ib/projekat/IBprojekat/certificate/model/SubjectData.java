package ib.projekat.IBprojekat.certificate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectData {

    private PublicKey publicKey;
    private X500Name x500name;
    private BigInteger serialNumber;
    private Date startDate;
    private Date endDate;

}
