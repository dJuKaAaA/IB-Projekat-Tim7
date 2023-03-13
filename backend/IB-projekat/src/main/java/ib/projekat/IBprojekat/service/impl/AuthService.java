package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.LoginRequestDto;
import ib.projekat.IBprojekat.dto.request.UserRequestDto;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.exception.EmailAlreadyExistsException;
import ib.projekat.IBprojekat.exception.InvalidCredentialsException;
import ib.projekat.IBprojekat.exception.UserNotFoundException;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import ib.projekat.IBprojekat.websecurity.JwtService;
import ib.projekat.IBprojekat.websecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("AuthService")
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException(e.getMessage());
        }

        UserEntity user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(UserNotFoundException::new);
        String jwt = jwtService.generateToken(new UserDetailsImpl(user));

        return new TokenResponseDto(jwt);

    }

    @Override
    public UserResponseDto createAccount(UserRequestDto userRequest) {
        Optional<UserEntity> potentiallyExistingUser = userRepository.findByEmail(userRequest.getEmail());
        if (potentiallyExistingUser.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        UserEntity newUser = UserEntity.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();
        newUser = userRepository.save(newUser);

        return UserResponseDto.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .surname(newUser.getSurname())
                .email(newUser.getEmail())
                .build();
    }
}
