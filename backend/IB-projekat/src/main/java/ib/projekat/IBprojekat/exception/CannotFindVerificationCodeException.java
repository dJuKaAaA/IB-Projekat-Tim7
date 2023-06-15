package ib.projekat.IBprojekat.exception;

public class CannotFindVerificationCodeException extends RuntimeException {


    public CannotFindVerificationCodeException() {
        super("Verification code doesn't exist!");
    }

    public CannotFindVerificationCodeException(String message) {
        super(message);
    }
}
