package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.config.oauthstuff.JwtTempHolder;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.constant.VerificationCodeType;
import ib.projekat.IBprojekat.dao.CertificateDemandRepository;
import ib.projekat.IBprojekat.dao.PasswordHistoryRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dao.VerificationCodeRepository;
import ib.projekat.IBprojekat.dto.request.*;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import ib.projekat.IBprojekat.entity.CertificateDemandEntity;
import ib.projekat.IBprojekat.entity.PasswordHistoryEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.entity.VerificationCodeEntity;
import ib.projekat.IBprojekat.exception.*;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import ib.projekat.IBprojekat.websecurity.JwtService;
import ib.projekat.IBprojekat.websecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final GlobalConstants globalConstants;
    private final JwtTempHolder jwtTempHolder;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Override
    public UserResponseDto login(LoginRequestDto loginRequest, VerificationCodeType verificationCodeType) {
        logger.info("Started login process");

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
        if (user.getDateForChangePassword().before(new Date())) throw new PasswordOutdatedException();

        if (verificationCodeType == VerificationCodeType.EMAIL) {
            VerificationTargetRequestDto verificationTargetRequestDto = new VerificationTargetRequestDto();
            verificationTargetRequestDto.setEmail(user.getEmail());
            sendVerificationCode(verificationTargetRequestDto);
        } else {
            VerificationTargetRequestDto verificationTargetRequestDto = new VerificationTargetRequestDto();
            verificationTargetRequestDto.setPhoneNumber(user.getPhoneNumber());
            sendVerificationCode(verificationTargetRequestDto);
        }
        logger.info("Successfully finished login process");
        return new UserResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto createAccount(UserRequestDto userRequest, VerificationCodeType verificationCodeType) {
        logger.info("Started account creation");

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

        // password expiration date
        Date futureDate = new Date(System.currentTimeMillis() + globalConstants.PASSWORD_VALIDATION_IN_MILLIS);

        UserEntity newUser = UserEntity.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .dateForChangePassword(futureDate)
                .build();
        newUser = userRepository.save(newUser);

        PasswordHistoryEntity passwordHistoryEntity = PasswordHistoryEntity.builder()
                .password(newUser.getPassword())
                .user(newUser)
                .passwordCreationDate(new Date())
                .build();
        passwordHistoryRepository.save(passwordHistoryEntity);

        verificationCodeService.sendVerificationCode(newUser, verificationCodeType);

        logger.info("Created account");
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
    public void verifyVerificationCode(VerifyVerificationCodeRequestDto verifyVerificationCodeRequestDto) {
        logger.info("Started code verification process");

        UserEntity user = this.getUserByVerificationCodeRequest(verifyVerificationCodeRequestDto);

        VerificationCodeEntity retrievedVerificationCode =
                verificationCodeRepository.findFirstByUserOrderByDateOfExpirationDesc(user).orElseThrow(CannotFindVerificationCodeException::new);

        verificationCodeService.verifyVerificationCode(retrievedVerificationCode, verifyVerificationCodeRequestDto);

        logger.info("Verified code");
    }

    @Override
    public void sendVerificationCode(VerificationTargetRequestDto verificationTargetDto) {
        logger.info("Started verification code sending process");

        UserEntity user = this.getUserFromVerificationTarget(verificationTargetDto);
        VerificationCodeType verificationCodeType =
                this.getVerificationCodeTypeFromVerificationTarget(verificationTargetDto);

        verificationCodeService.sendVerificationCode(user, verificationCodeType);

        logger.info("Sent verification code");
    }

    public TokenResponseDto verifyLogin(VerifyLoginRequestDto userLoginResponseDto) {
        String userEmail = userLoginResponseDto.getEmail();
        String code = userLoginResponseDto.getCode();

        UserEntity user = this.userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        VerifyVerificationCodeRequestDto verifyVerificationCodeRequestDto =
                new VerifyVerificationCodeRequestDto(user.getEmail(), user.getPhoneNumber(), code);

        this.verifyVerificationCode(verifyVerificationCodeRequestDto);

        // creating the claims that will be put in the jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("roles", List.of(user.getRole()));

        String jwt = jwtService.generateToken(claims, new UserDetailsImpl(user));

        logger.info("Logged in after verifying code");

        return new TokenResponseDto(jwt);

    }

    @Override
    public UserResponseDto verifyRegistration(VerifyVerificationCodeRequestDto verifyVerificationCodeRequestDto) {
        this.verifyVerificationCode(verifyVerificationCodeRequestDto);
        UserEntity user = getUserByVerificationCodeRequest(verifyVerificationCodeRequestDto);

        user.setEnabled(true);
        userRepository.save(user);

        logger.info("Verified registration code");

        return new UserResponseDto(user);
    }

    @Override
    public void recoverPassword(PasswordRecoveryRequestDto passwordRecoveryDto, int passwordNonMatchCount,
                                Date passwordValidationTime) {
        logger.info("Started password recovery process");

        String userEmail = passwordRecoveryDto.getUserEmail();
        String userPhoneNumber = passwordRecoveryDto.getUserPhoneNumber();
        String newPassword = passwordRecoveryDto.getNewPassword();

        UserEntity user;
        if (!userEmail.equals("")) {
            user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        } else {
            user = userRepository.findByPhoneNumber(userPhoneNumber).orElseThrow(UserNotFoundException::new);
        }

        isNewPasswordDifferentFromLastN(user, newPassword, passwordNonMatchCount);

        String encryptedNewPassword = passwordEncoder.encode(newPassword);
        PasswordHistoryEntity passwordHistoryEntity = PasswordHistoryEntity.builder()
                .password(encryptedNewPassword)
                .passwordCreationDate(new Date())
                .user(user)
                .build();
        passwordHistoryRepository.save(passwordHistoryEntity);

        user.setPassword(encryptedNewPassword);
        user.setDateForChangePassword(passwordValidationTime);
        userRepository.save(user);

        logger.info("Successfully recovered password");
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest, int passwordNonMatchCount,
                              Date passwordValidationTime) {
        logger.info("Started password reset process");

        String userEmail = passwordResetRequest.getEmail();
        String oldPassword = passwordResetRequest.getOldPassword();
        String newPassword = passwordResetRequest.getNewPassword();

        UserEntity user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);

        isCurrentAndOldPasswordSame(oldPassword, user.getPassword());
        isNewPasswordDifferentFromLastN(user, newPassword, passwordNonMatchCount);

        String encryptedNewPassword = passwordEncoder.encode(newPassword);
        PasswordHistoryEntity passwordHistoryEntity = PasswordHistoryEntity.builder()
                .password(encryptedNewPassword)
                .passwordCreationDate(new Date())
                .user(user)
                .build();
        passwordHistoryRepository.save(passwordHistoryEntity);

        user.setPassword(encryptedNewPassword);
        user.setDateForChangePassword(passwordValidationTime);
        userRepository.save(user);

        logger.info("Successfully reset password");
    }

    @Override
    public TokenResponseDto loginWithGoogle() {
        TokenResponseDto tokenResponse = TokenResponseDto.builder()
                .token(jwtTempHolder.getJwt())
                .build();
        jwtTempHolder.setJwt(null);
        logger.info("Successfully logged in with google OAuth");
        return tokenResponse;
    }

    private UserEntity getUserByVerificationCodeRequest(VerifyVerificationCodeRequestDto verifyVerificationCodeRequestDto) {
        UserEntity user = null;
        String userEmail = verifyVerificationCodeRequestDto.getEmail();
        String userPhoneNumber = verifyVerificationCodeRequestDto.getPhoneNumber();

        if (userEmail == null || !userEmail.isBlank()) {
            user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        } else if (userPhoneNumber == null || !userPhoneNumber.isBlank()) {
            user = userRepository.findByPhoneNumber(userPhoneNumber).orElseThrow(UserNotFoundException::new);
        } else {
            throw new UserNotFoundException();
        }

        return user;
    }

    private UserEntity getUserFromVerificationTarget(VerificationTargetRequestDto verificationTargetDto) {
        UserEntity user = null;
        String userEmail = verificationTargetDto.getEmail();
        String userPhoneNumber = verificationTargetDto.getPhoneNumber();

        if (!userEmail.equals("")) {
            user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        } else if (!userPhoneNumber.equals("")) {
            user = userRepository.findByPhoneNumber(userPhoneNumber).orElseThrow(UserNotFoundException::new);
        }
        return user;
    }

    private VerificationCodeType getVerificationCodeTypeFromVerificationTarget(VerificationTargetRequestDto verificationTargetDto) {
        VerificationCodeType verificationCodeType = null;
        String userEmail = verificationTargetDto.getEmail();
        String userPhoneNumber = verificationTargetDto.getPhoneNumber();

        if (!userEmail.equals("")) {
            verificationCodeType = VerificationCodeType.EMAIL;
        } else if (!userPhoneNumber.equals("")) {
            verificationCodeType = VerificationCodeType.PHONE;
        }

        return verificationCodeType;
    }

    private boolean arePasswordsEqual(String plainTextPassword, String encryptPassword) {
        return BCrypt.checkpw(plainTextPassword, encryptPassword);
    }

    private void isNewPasswordDifferentFromLastN(UserEntity user, String newPassword, int passwordNonMatchCount) {
        List<PasswordHistoryEntity> oldPasswords =
                passwordHistoryRepository.findAllByUserOrderByPasswordCreationDateDesc(user);

        for (PasswordHistoryEntity oldPasswordHistoryEntity : oldPasswords) {
            if (passwordNonMatchCount == 0) {
                break;
            }

            if (arePasswordsEqual(newPassword, oldPasswordHistoryEntity.getPassword())) {
                throw new SamePasswordException("New password cannot match with last %s passwords".formatted(passwordNonMatchCount));
            }
            passwordNonMatchCount--;
        }
    }

    private void isCurrentAndOldPasswordSame(String oldPasswordPlainText, String currentPasswordEncrypted) {
        if (!arePasswordsEqual(oldPasswordPlainText, currentPasswordEncrypted)) {
            throw new OldPasswordNotMatchException();
        }
    }

}
