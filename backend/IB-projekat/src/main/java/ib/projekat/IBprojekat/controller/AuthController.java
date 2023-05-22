package ib.projekat.IBprojekat.controller;

import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.VerificationCodeType;
import ib.projekat.IBprojekat.dto.request.*;
import ib.projekat.IBprojekat.dto.response.ReCaptchaResponse;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import ib.projekat.IBprojekat.exception.ReCaptchaException;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final GlobalConstants globalConstants;
    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/login/{verificationCodeType}/")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest,
                                                 @PathVariable String verificationCodeType,
                                                 @RequestParam("g-recaptcha-response") String captchaResponse) {


        String params = "?secret=" + globalConstants.GOOGLE_RECAPTCHA_SECRET_KEY + "&response=" + captchaResponse;
        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(globalConstants.GOOGLE_RECAPTCHA_VERIFICATION_URL + params, HttpMethod.POST, null,
                ReCaptchaResponse.class).getBody();

        if (reCaptchaResponse.isSuccess() || captchaResponse.equals("")) {
            VerificationCodeType convertedVerificationCodeType = VerificationCodeType.valueOf(verificationCodeType.toUpperCase());
            return new ResponseEntity<>(authService.login(loginRequest, convertedVerificationCodeType), HttpStatus.OK);
        } else {
            throw new ReCaptchaException();
        }

    }

    @PostMapping("/create-account/{verificationCodeType}")
    public ResponseEntity<UserResponseDto> createAccount(@Valid @RequestBody UserRequestDto userRequest,
                                                         @PathVariable String verificationCodeType, @RequestParam("g-recaptcha-response") String captchaResponse) {
        String params = "?secret=" + globalConstants.GOOGLE_RECAPTCHA_SECRET_KEY + "&response=" + captchaResponse;
        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(globalConstants.GOOGLE_RECAPTCHA_VERIFICATION_URL + params, HttpMethod.POST, null,
                ReCaptchaResponse.class).getBody();
        if (reCaptchaResponse.isSuccess() || captchaResponse.equals("")) {
            VerificationCodeType convertedVerificationCodeType = VerificationCodeType.valueOf(verificationCodeType.toUpperCase());
            return new ResponseEntity<>(authService.createAccount(userRequest, convertedVerificationCodeType), HttpStatus.OK);
        } else {
            throw new ReCaptchaException();
        }
    }

    @PostMapping("/sendVerificationCode")
    public ResponseEntity sendVerificationCode(@Valid
                                               @RequestBody VerificationTargetRequestDto verificationTargetDto) {
        authService.sendVerificationCode(verificationTargetDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Verification code is successfully sent!");
    }

    @PostMapping("/verifyVerificationCode")
    public ResponseEntity verifyVerificationCode(@Valid @RequestBody VerifyVerificationCodeRequestDto codeVerificationRequest) {
        this.authService.verifyVerificationCode(codeVerificationRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Code successfully verified!");
    }


    @PostMapping("/verifyRegistration")
    public ResponseEntity<UserResponseDto> verifyRegistration(@Valid @RequestBody VerifyVerificationCodeRequestDto registrationVerification) {
        return new ResponseEntity<>(authService.verifyRegistration(registrationVerification), HttpStatus.OK);
    }

    @PostMapping("/recoverPassword")
    public ResponseEntity recoverPassword(@Valid @RequestBody PasswordRecoveryRequestDto passwordRecoveryRequestDto) {
        Date passwordExpirationDate = new Date(System.currentTimeMillis() + globalConstants.PASSWORD_VALIDATION_IN_MILLIS);

        authService.recoverPassword(passwordRecoveryRequestDto, globalConstants.PASSWORD_NON_MATCH_COUNT, passwordExpirationDate);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Password successfully changed!");
    }


    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        Date passwordExpirationDate = new Date(System.currentTimeMillis() + globalConstants.PASSWORD_VALIDATION_IN_MILLIS);
        authService.resetPassword(passwordResetRequest, globalConstants.PASSWORD_NON_MATCH_COUNT, passwordExpirationDate);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Password successfully changed!");
    }

    @PostMapping("/verifyLogin")
    public ResponseEntity<TokenResponseDto> verifyLogin(@Valid @RequestBody VerifyLoginRequestDto verifyUserLoginRequestDto) {
        return new ResponseEntity<>(authService.verifyLogin(verifyUserLoginRequestDto), HttpStatus.OK);
    }


}
