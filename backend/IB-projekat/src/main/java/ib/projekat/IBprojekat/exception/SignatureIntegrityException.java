package ib.projekat.IBprojekat.exception;

public class SignatureIntegrityException extends RuntimeException {

    public SignatureIntegrityException() {
        super("Signature integrity loss!");
    }

    public SignatureIntegrityException(String message) {
        super(message);
    }
}
