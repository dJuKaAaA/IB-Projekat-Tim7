package ib.projekat.IBprojekat.certificate.keystore;

import ib.projekat.IBprojekat.certificate.model.IssuerData;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;

import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

@Service
public class KeyStoreReader {

    @Autowired
    private KeyStore keyStore;

    public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, password);

            Certificate cert = keyStore.getCertificate(alias);

            PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
            return new IssuerData(issuerName, privKey);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
                 | UnrecoverableKeyException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // alias - certificate serial number
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                return cert;
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
                 | CertificateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // alias - serial keystore number
    // pass - private key password, na osnovu njega se preuzima private key
    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, keyStorePass.toCharArray());

            if (keyStore.isKeyEntry(alias)) {
                return (PrivateKey) keyStore.getKey(alias, pass.toCharArray());
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
                 | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


    public X509Certificate[] getChainForCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, keyStorePass.toCharArray());

            Certificate[] chain = keyStore.getCertificateChain(alias);
            if (chain == null) {
                return null;
            }

            if (!(chain[0] instanceof X509Certificate)) {
                throw new RuntimeException("Certificate chain for alias " + alias + " is not of type X509Certificate[]");
            }

            return Arrays.copyOf(chain, chain.length, X509Certificate[].class);
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
