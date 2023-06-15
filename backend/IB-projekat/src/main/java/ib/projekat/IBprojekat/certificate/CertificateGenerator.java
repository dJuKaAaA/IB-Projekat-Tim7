package ib.projekat.IBprojekat.certificate;

import ib.projekat.IBprojekat.certificate.model.IssuerData;
import ib.projekat.IBprojekat.certificate.model.SubjectData;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CertificateGenerator {

    private final GlobalConstants globalConstants;

    // privatnim kljucem potpisujemo
    public X509Certificate generateCertificate(UserEntity requester, UserEntity requestedIssuer, PublicKey subjectPublicKey, PrivateKey issuerPrivateKey) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            Date validityStartDate = new Date(System.currentTimeMillis());
            Date expirationDate = new Date(System.currentTimeMillis() + globalConstants.ONE_YEAR_IN_MILLIS);
            SubjectData subjectData = generateSubjectData(
                    requester,
                    validityStartDate,
                    expirationDate,
                    subjectPublicKey
            );

            IssuerData issuerData = generateIssuerData(requestedIssuer, issuerPrivateKey);

            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                    issuerData.getX500name(),
                    subjectData.getSerialNumber(),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            certGen.addExtension(
                    Extension.basicConstraints,
                    true,
                    new BasicConstraints(true)
            );

            X509CertificateHolder certHolder = certGen.build(contentSigner);

            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            return certConverter.getCertificate(certHolder);
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException |
                 CertificateException e) {
            e.printStackTrace();
        } catch (CertIOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private IssuerData generateIssuerData(UserEntity user, PrivateKey issuerKey) {
        return new IssuerData(buildX500Name(user), issuerKey);
    }

    private SubjectData generateSubjectData(UserEntity user, Date startDate, Date endDate, PublicKey publicKey) {
        // generating the serial number for the certificate
        BigInteger serialNumber = new java.math.BigInteger(32, new java.security.SecureRandom());

        return new SubjectData(publicKey, buildX500Name(user), serialNumber, startDate, endDate);
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private X500Name buildX500Name(UserEntity user) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);

        builder.addRDN(BCStyle.CN, "%s %s".formatted(user.getName(), user.getSurname()));
        builder.addRDN(BCStyle.SURNAME, user.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, user.getName());

        builder.addRDN(BCStyle.O, "Tim 7");
        builder.addRDN(BCStyle.OU, "Tim 7");
        builder.addRDN(BCStyle.C, "sr");

        builder.addRDN(BCStyle.E, user.getEmail());

        builder.addRDN(BCStyle.UID, user.getId().toString());

        return builder.build();
    }

}
