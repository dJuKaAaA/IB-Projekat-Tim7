package ib.projekat.IBprojekat.exception;

public class InvalidCertificateException extends RuntimeException {

    public InvalidCertificateException() {
        super("Certificate is invalid!");
    }

    public InvalidCertificateException(String message) {
        super(message);
    }
}
