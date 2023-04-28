package ib.projekat.IBprojekat.controller;

import ib.projekat.IBprojekat.constant.VerificationCodeType;
import ib.projekat.IBprojekat.dto.request.LoginRequestDto;
import ib.projekat.IBprojekat.dto.request.RegistrationVerificationRequestDto;
import ib.projekat.IBprojekat.dto.request.UserRequestDto;
import ib.projekat.IBprojekat.dto.request.VerificationTargetDto;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/create-account/{verificationCodeType}")
    public ResponseEntity<UserResponseDto> createAccount(@Valid @RequestBody UserRequestDto userRequest,
                                                         @PathVariable String verificationCodeType) {
        VerificationCodeType convertedVerificationCodeType = VerificationCodeType.valueOf(verificationCodeType.toUpperCase());
        return new ResponseEntity<>(authService.createAccount(userRequest, convertedVerificationCodeType), HttpStatus.OK);
    }

    @PostMapping("/verifyRegistration")
    public ResponseEntity<UserResponseDto> verifyRegistration(@Valid @RequestBody RegistrationVerificationRequestDto registrationVerification) {
        return new ResponseEntity<>(authService.verifyRegistration(registrationVerification), HttpStatus.OK);
    }

    @PostMapping("/sendVerificationCode/{verificationCodeType}")
    public ResponseEntity sendVerificationCode(@Valid @PathVariable String verificationCodeType,
                                               VerificationTargetDto verificationTargetDto) {
        VerificationCodeType convertedVerificationCodeType = VerificationCodeType.valueOf(verificationCodeType.toUpperCase());
        authService.sendPasswordRecoveryCode(convertedVerificationCodeType, verificationTargetDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/recoverPassword")
    public ResponseEntity recoverPassword() {
        return null;
    }


}
