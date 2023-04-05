package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.certificate.CertificateGenerator;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreReader;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreWriter;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.CertificateDemandRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.LoginRequestDto;
import ib.projekat.IBprojekat.dto.request.UserRequestDto;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.exception.*;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import ib.projekat.IBprojekat.websecurity.JwtService;
import ib.projekat.IBprojekat.websecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Optional;

@Service("AuthService")
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final CertificateDemandRepository certificateDemandRepository;

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
                .phoneNumber(userRequest.getPhoneNumber())
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
                .phoneNumber(newUser.getPhoneNumber())
                .email(newUser.getEmail())
                .build();
    }

    @Override
    public void checkUserIdMatchesUserEmail(Long userId, String userEmail) {
        UserEntity userById = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!userById.getEmail().equals(userEmail)) {
            throw new EndpointAccessException();
        }
    }

    @Override
    public void checkIsDemandIntendedForUser(String userEmail, Long demandId) {
        UserEntity potentialIssuer = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        CertificateDemandEntity certificateDemandEntity = certificateDemandRepository.findById(demandId)
                .orElseThrow(CertificateDemandNotFoundException::new);
        if (potentialIssuer.getId() != certificateDemandEntity.getRequestedIssuer().getId()) {
            throw new EndpointAccessException();
        }
    }

}
