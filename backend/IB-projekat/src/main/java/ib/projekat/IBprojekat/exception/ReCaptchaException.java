package ib.projekat.IBprojekat.exception;

public class ReCaptchaException extends RuntimeException {
    public ReCaptchaException() {
        super("You are a robot!!!");
    }

    public ReCaptchaException(String message) {
        super(message);
    }
}
