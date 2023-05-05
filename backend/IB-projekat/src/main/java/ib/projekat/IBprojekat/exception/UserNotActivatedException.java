package ib.projekat.IBprojekat.exception;

public class UserNotActivatedException extends RuntimeException {
    public UserNotActivatedException() {
        super("User account is not activated!");
    }

    public UserNotActivatedException(String message) {
        super(message);
    }
}
