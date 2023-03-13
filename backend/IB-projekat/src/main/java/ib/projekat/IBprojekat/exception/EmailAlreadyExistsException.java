package ib.projekat.IBprojekat.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("Email already exists!");
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
