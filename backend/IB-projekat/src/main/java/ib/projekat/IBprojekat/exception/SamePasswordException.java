package ib.projekat.IBprojekat.exception;

public class SamePasswordException extends RuntimeException {

    public SamePasswordException() {
        super("Passwords are same!");
    }

    public SamePasswordException(String message) {
        super(message);
    }
}
