package ib.projekat.IBprojekat.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid credentials!");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
