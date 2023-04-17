package ib.projekat.IBprojekat.exception;

public class CertificateDemandException extends RuntimeException {

    public CertificateDemandException() {
        super("Certificate demand error!");
    }

    public CertificateDemandException(String message) {
        super(message);
    }
}
