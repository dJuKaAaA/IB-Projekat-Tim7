package ib.projekat.IBprojekat.exception;

public class CertificateNotFoundException extends RuntimeException {

    public CertificateNotFoundException() {
        super("Certificate not found!");
    }

    public CertificateNotFoundException(String message) {
        super(message);
    }
}
