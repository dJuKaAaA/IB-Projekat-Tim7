package ib.projekat.IBprojekat.service.interf;

import ib.projekat.IBprojekat.constant.VerificationCodeType;
import ib.projekat.IBprojekat.dto.request.*;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface IAuthService {

    TokenResponseDto login(LoginRequestDto loginRequest);

    UserResponseDto createAccount(UserRequestDto userRequest, VerificationCodeType verificationCodeType);

    void checkUserIdMatchesUserEmail(Long userId, String userEmail);

    void checkIsDemandIntendedForUser(String userEmail, Long demandId);

    void verifyVerificationCode(VerifyVerificationCodeRequestDto registrationVerificationRequestDto);

    void sendVerificationCode(
            VerificationTargetDto verificationTargetDto);

    UserResponseDto verifyRegistration(VerifyVerificationCodeRequestDto registrationVerificationRequestDto);


    void recoverPassword(PasswordRecoveryRequestDto passwordRecoveryDto, int passwordNonMatchCount,
                         Date passwordValidationTime);

    void resetPassword(PasswordResetRequest passwordResetRequest, int passwordNonMatchCount,
                       Date passwordValidationTime);
}
