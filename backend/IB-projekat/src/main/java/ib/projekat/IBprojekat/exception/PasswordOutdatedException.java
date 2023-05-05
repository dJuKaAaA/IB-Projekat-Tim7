package ib.projekat.IBprojekat.exception;

public class PasswordOutdatedException extends RuntimeException {
    public PasswordOutdatedException() {
        super("The password is outdated");
    }

    public PasswordOutdatedException(String message) {
        super(message);
    }
}
