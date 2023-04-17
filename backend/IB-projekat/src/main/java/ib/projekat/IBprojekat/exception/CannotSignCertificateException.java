package ib.projekat.IBprojekat.exception;

public class CannotSignCertificateException extends RuntimeException {

    public CannotSignCertificateException() {
        super("Cannot sign certificate!");
    }

    public CannotSignCertificateException(String message) {
        super(message);
    }
}
