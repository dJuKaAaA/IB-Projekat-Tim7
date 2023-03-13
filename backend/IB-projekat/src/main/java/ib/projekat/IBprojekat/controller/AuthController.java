package ib.projekat.IBprojekat.controller;

import ib.projekat.IBprojekat.dto.request.LoginRequestDto;
import ib.projekat.IBprojekat.dto.request.UserRequestDto;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private IAuthService userCrudService;
    @Autowired
    public void setUserCrudService(@Qualifier("AuthService") IAuthService userCrudService) {
        this.userCrudService = userCrudService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        return new ResponseEntity<>(userCrudService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/create-account")
    public ResponseEntity<UserResponseDto> createAccount(@Valid @RequestBody UserRequestDto userRequest) {
        return new ResponseEntity<>(userCrudService.createAccount(userRequest), HttpStatus.OK);
    }

}
