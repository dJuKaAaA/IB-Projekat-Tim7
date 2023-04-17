package ib.projekat.IBprojekat.exception;

public class EndpointAccessException extends RuntimeException {

    public EndpointAccessException() {
        super("Access denied!");
    }

    public EndpointAccessException(String message) {
        super(message);
    }

}
