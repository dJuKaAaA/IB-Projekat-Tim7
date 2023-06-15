package ib.projekat.IBprojekat.exception;

public class VerificationCodeExpiredException extends RuntimeException {


    public VerificationCodeExpiredException() {
        super("Verification code expired!");
    }

    public VerificationCodeExpiredException(String message) {
        super(message);
    }


}
