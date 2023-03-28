package ib.projekat.IBprojekat.certificate.keystore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
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

    public void write(String alias, PrivateKey privateKey, char[] password, java.security.cert.Certificate certificate) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password, new Certificate[]{certificate});
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public void clearKeyStore(String keystoreFile, char[] keystorePassword) {
        try {
            // Load the keystore
            FileInputStream is = new FileInputStream(keystoreFile);
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(is, keystorePassword);

            // Get all aliases in the keystore and delete each entry
            Enumeration<String> aliases = keystore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                keystore.deleteEntry(alias);
            }

            // Save the keystore
            FileOutputStream os = new FileOutputStream(keystoreFile);
            keystore.store(os, keystorePassword);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
