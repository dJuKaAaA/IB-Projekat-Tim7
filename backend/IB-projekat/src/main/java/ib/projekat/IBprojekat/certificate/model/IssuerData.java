package ib.projekat.IBprojekat.certificate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssuerData {

    private X500Name x500name;
    private PrivateKey privateKey;

}
