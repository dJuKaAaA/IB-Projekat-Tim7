package ib.projekat.IBprojekat.exception;

public class CertificatePullException extends RuntimeException {

    public CertificatePullException() {
        super("Certificate pull error!");
    }

    public CertificatePullException(String message) {
        super(message);
    }
}
