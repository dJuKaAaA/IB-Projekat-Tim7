package ib.projekat.IBprojekat.validation;

import ib.projekat.IBprojekat.dto.response.ErrorResponseDto;
import ib.projekat.IBprojekat.exception.*;
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

    @ExceptionHandler({CertificateDemandNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleCertificateDemandNotFoundException(CertificateDemandNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CertificateNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleCertificateNotFoundException(CertificateNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CannotSignCertificateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleCannotSignCertificateException(CannotSignCertificateException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SignatureIntegrityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleSignatureIntegrityException(SignatureIntegrityException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidCertificateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleInvalidCertificateException(InvalidCertificateException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CertificateDemandException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleCertificateDemandException(CertificateDemandException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EndpointAccessException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorResponseDto> handleEndpointAccessException(EndpointAccessException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({CannotFindVerificationCodeException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleVerificationCodeException(CannotFindVerificationCodeException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({VerificationCodeExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleVerificationCodeExpiredException(VerificationCodeExpiredException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({OldPasswordNotMatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleOldPasswordNotMatchException(OldPasswordNotMatchException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PasswordOutdatedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handlePasswordOutdatedException(PasswordOutdatedException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SamePasswordException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleSamePasswordException(SamePasswordException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotActivatedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleUserNotActivatedException(UserNotActivatedException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidCertificateOwnerException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorResponseDto> handleNotCertificateOwnerException(InvalidCertificateOwnerException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({CertificatePullException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleCertificatePullException(CertificatePullException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ReCaptchaException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleReCaptchaException(ReCaptchaException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
