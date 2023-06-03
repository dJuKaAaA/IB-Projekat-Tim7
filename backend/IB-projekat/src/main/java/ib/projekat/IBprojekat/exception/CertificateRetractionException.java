package ib.projekat.IBprojekat.exception;

public class CertificateRetractionException extends RuntimeException {

    public CertificateRetractionException() {
        super("Certificate pull error!");
    }

    public CertificateRetractionException(String message) {
        super(message);
    }
}
