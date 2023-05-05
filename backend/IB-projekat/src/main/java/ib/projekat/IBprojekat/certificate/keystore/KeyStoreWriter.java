package ib.projekat.IBprojekat.certificate.keystore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Service
public class KeyStoreWriter {

    @Autowired
    private KeyStore keyStore;

    public void loadKeyStore(String fileName, char[] password) {
        try {
            if (fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    // alias - certificate serial number
    // password - constants file jks entry password
    public void write(String alias, PrivateKey privateKey, char[] password, Certificate[] certificateChain) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password, certificateChain);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

}
