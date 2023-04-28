package ib.projekat.IBprojekat.service.interf;

import ib.projekat.IBprojekat.constant.VerificationCodeType;
import ib.projekat.IBprojekat.dto.request.LoginRequestDto;
import ib.projekat.IBprojekat.dto.request.RegistrationVerificationRequestDto;
import ib.projekat.IBprojekat.dto.request.UserRequestDto;
import ib.projekat.IBprojekat.dto.request.VerificationTargetDto;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {

    TokenResponseDto login(LoginRequestDto loginRequest);

    UserResponseDto createAccount(UserRequestDto userRequest, VerificationCodeType verificationCodeType);

    void checkUserIdMatchesUserEmail(Long userId, String userEmail);

    void checkIsDemandIntendedForUser(String userEmail, Long demandId);

    UserResponseDto verifyRegistration(RegistrationVerificationRequestDto registrationVerificationRequestDto);


    void sendPasswordRecoveryCode(VerificationCodeType verificationCodeType,
                                  VerificationTargetDto verificationTargetDto);
}
