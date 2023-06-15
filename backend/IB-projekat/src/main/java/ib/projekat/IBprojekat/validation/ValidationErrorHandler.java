package ib.projekat.IBprojekat.validation;

import ib.projekat.IBprojekat.dto.response.ErrorResponseDto;
import ib.projekat.IBprojekat.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(ValidationErrorHandler.class);

    @ExceptionHandler({InvalidCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleInvalidCredentialsException(InvalidCredentialsException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EmailAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        String errorMessage = errorList.get(0).getDefaultMessage();

        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), errorMessage));
        return new ResponseEntity<>(new ErrorResponseDto(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CertificateDemandNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleCertificateDemandNotFoundException(CertificateDemandNotFoundException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CertificateNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleCertificateNotFoundException(CertificateNotFoundException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CannotSignCertificateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleCannotSignCertificateException(CannotSignCertificateException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SignatureIntegrityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleSignatureIntegrityException(SignatureIntegrityException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidCertificateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleInvalidCertificateException(InvalidCertificateException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CertificateDemandException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleCertificateDemandException(CertificateDemandException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EndpointAccessException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorResponseDto> handleEndpointAccessException(EndpointAccessException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({CannotFindVerificationCodeException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleVerificationCodeException(CannotFindVerificationCodeException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({VerificationCodeExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleVerificationCodeExpiredException(VerificationCodeExpiredException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({OldPasswordNotMatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleOldPasswordNotMatchException(OldPasswordNotMatchException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PasswordOutdatedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handlePasswordOutdatedException(PasswordOutdatedException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SamePasswordException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleSamePasswordException(SamePasswordException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotActivatedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleUserNotActivatedException(UserNotActivatedException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidCertificateOwnerException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorResponseDto> handleNotCertificateOwnerException(InvalidCertificateOwnerException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({CertificateRetractionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleCertificatePullException(CertificateRetractionException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ReCaptchaException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleReCaptchaException(ReCaptchaException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CertificateTooLargeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleCertificateTooLargeException(CertificateTooLargeException e) {
        logger.error("%s error thrown with message: %s".formatted(e.getClass().toString(), e.getMessage()));
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
