package ib.projekat.IBprojekat.exception;

public class CertificateTooLargeException extends RuntimeException {

    public CertificateTooLargeException() {
        super("Certificate is too large!");
    }

    public CertificateTooLargeException(String message) {
        super(message);
    }
}
