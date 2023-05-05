package ib.projekat.IBprojekat.exception;

public class OldPasswordNotMatchException extends RuntimeException {

    public OldPasswordNotMatchException() {
        super("The old password entered does not match the current password!");
    }

    public OldPasswordNotMatchException(String message) {
        super(message);
    }
}
