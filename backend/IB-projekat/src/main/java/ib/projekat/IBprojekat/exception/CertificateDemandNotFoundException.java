package ib.projekat.IBprojekat.exception;

public class CertificateDemandNotFoundException extends RuntimeException {

    public CertificateDemandNotFoundException() {
        super("Certificate demand not found!");
    }

    public CertificateDemandNotFoundException(String message) {
        super(message);
    }
}
