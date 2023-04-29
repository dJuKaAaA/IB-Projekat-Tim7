package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.constant.VerificationCodeType;
import ib.projekat.IBprojekat.dao.CertificateDemandRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dao.VerificationCodeRepository;
import ib.projekat.IBprojekat.dto.request.LoginRequestDto;
import ib.projekat.IBprojekat.dto.request.RegistrationVerificationRequestDto;
import ib.projekat.IBprojekat.dto.request.UserRequestDto;
import ib.projekat.IBprojekat.dto.request.VerificationTargetDto;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.entity.VerificationCodeEntity;
import ib.projekat.IBprojekat.exception.*;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import ib.projekat.IBprojekat.websecurity.JwtService;
import ib.projekat.IBprojekat.websecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service("AuthService")
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final CertificateDemandRepository certificateDemandRepository;

    private final VerificationCodeRepository verificationCodeRepository;

    private final VerificationCodeService verificationCodeService;

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

        // creating the claims that will be put in the jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());

        String jwt = jwtService.generateToken(claims, new UserDetailsImpl(user));

        return new TokenResponseDto(jwt);

    }

    @Override
    @Transactional
    public UserResponseDto createAccount(UserRequestDto userRequest, VerificationCodeType verificationCodeType) {
        Optional<UserEntity> potentiallyExistingUser = userRepository.findByEmail(userRequest.getEmail());
        if (potentiallyExistingUser.isPresent()) {
            UserEntity user = potentiallyExistingUser.get();
            if (user.isEnabled()) {
                throw new EmailAlreadyExistsException();
            } else {
                verificationCodeRepository.deleteAllByUser(user);
                userRepository.delete(potentiallyExistingUser.get());
            }
        }

        UserEntity newUser = UserEntity.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();
        newUser = userRepository.save(newUser);

        verificationCodeService.sendVerificationCode(newUser, verificationCodeType);

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

    @Override
    public UserResponseDto verifyRegistration(RegistrationVerificationRequestDto registrationVerificationRequestDto) {

        UserEntity userEntity =
                userRepository.findByEmail(registrationVerificationRequestDto.getEmail()).orElseThrow(UserNotFoundException::new);

        VerificationCodeEntity retrievedVerificationCode =
                verificationCodeRepository.findFirstByUserOrderByDateOfExpirationDesc(userEntity).orElseThrow(CannotFindVerificationCodeException::new);

        verificationCodeService.verifyVerificationCode(retrievedVerificationCode, registrationVerificationRequestDto);

        userEntity.setEnabled(true);
        userRepository.save(userEntity);

        return new UserResponseDto(userEntity);
    }

    @Override
    public void sendPasswordRecoveryCode(VerificationCodeType verificationCodeType,
                                         VerificationTargetDto verificationTargetDto) {
        UserEntity user = null;
        if (isVerificationCodeTypeEmail(verificationCodeType)) {
            String userEmail = verificationTargetDto.getEmail();
            user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        } else if (isVerificationCodeTypePhone(verificationCodeType)) {
            String userPhone = verificationTargetDto.getPhone();
            user = userRepository.findByPhoneNumber(userPhone).orElseThrow(UserNotFoundException::new);
        }
        verificationCodeService.sendVerificationCode(user, verificationCodeType);
    }

    private boolean isVerificationCodeTypeEmail(VerificationCodeType verificationCodeType) {
        return verificationCodeType == VerificationCodeType.EMAIL;
    }

    private boolean isVerificationCodeTypePhone(VerificationCodeType verificationCodeType) {
        return verificationCodeType == VerificationCodeType.PHONE;
    }


}
