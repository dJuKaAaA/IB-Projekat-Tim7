package ib.projekat.IBprojekat.validation;

import ib.projekat.IBprojekat.dto.response.ErrorResponseDto;
import ib.projekat.IBprojekat.exception.EmailAlreadyExistsException;
import ib.projekat.IBprojekat.exception.InvalidCredentialsException;
import ib.projekat.IBprojekat.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ValidationErrorHandler {

    @ExceptionHandler({InvalidCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmailAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        String errorMessage = errorList.get(0).getDefaultMessage();

        return new ResponseEntity<>(new ErrorResponseDto(errorMessage), HttpStatus.BAD_REQUEST);
    }

}
