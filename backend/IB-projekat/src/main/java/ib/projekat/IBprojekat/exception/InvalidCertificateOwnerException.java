package ib.projekat.IBprojekat.exception;

public class InvalidCertificateOwnerException extends RuntimeException {

    public InvalidCertificateOwnerException() {
        super("Invalid certificate owner!");
    }

    public InvalidCertificateOwnerException(String message) {
        super(message);
    }
}
