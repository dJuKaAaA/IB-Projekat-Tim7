package ib.projekat.IBprojekat.service.impl;


import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.VerificationCodeType;
import ib.projekat.IBprojekat.dao.VerificationCodeRepository;
import ib.projekat.IBprojekat.dto.request.VerifyVerificationCodeRequestDto;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.entity.VerificationCodeEntity;
import ib.projekat.IBprojekat.exception.CannotFindVerificationCodeException;
import ib.projekat.IBprojekat.exception.VerificationCodeExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final ComunicationService comunicationService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final GlobalConstants globalConstants;

    public void verifyVerificationCode(VerificationCodeEntity verificationCodeEntity,
                                       VerifyVerificationCodeRequestDto registrationVerificationRequestDto) {
        String sentVerificationCode = registrationVerificationRequestDto.getCode();
        Date verificationRequestCreationDate = new Date();
        this.verifyVerificationCodesMatch(verificationCodeEntity, sentVerificationCode);
        isVerificationCodeExpired(verificationCodeEntity, verificationRequestCreationDate);

    }

    public void sendVerificationCode(UserEntity user, VerificationCodeType verificationCodeType) {
        String verificationCode = Integer.toString(generateVerificationCode());
        String subject = "Verification Code";
        int codeValidityPeriodInMinutes = 30;

        saveVerificationCode(user, verificationCode, codeValidityPeriodInMinutes);

        if (isVerificationCodeTypeEmail(verificationCodeType)) {
            comunicationService.sendTextEmail(user.getEmail(), subject, verificationCode);
        } else if (isVerificationCodeTypePhone(verificationCodeType)) {
            String message = subject + ": " + verificationCode;
            comunicationService.sentPhoneMessage(globalConstants.PHONE_NUMBER, user.getPhoneNumber(), message);
        }
    }

    private int generateVerificationCode() {
        Random r = new Random(System.currentTimeMillis());
        return 10000 + r.nextInt(20000);
    }

    private void saveVerificationCode(UserEntity user, String verificationCode, int codeValidityPeriodInMinutes) {

        Date dateOfGeneration = new Date();
        Date dateOfExpiration = generateExpirationDateOfVerificationCode(codeValidityPeriodInMinutes);

        VerificationCodeEntity verificationCodeEntity = VerificationCodeEntity.builder()
                .code(verificationCode)
                .dateOfGeneration(dateOfGeneration)
                .dateOfExpiration(dateOfExpiration)
                .user(user)
                .build();

        verificationCodeRepository.save(verificationCodeEntity);
    }

    private Date generateExpirationDateOfVerificationCode(int validityPeriodInMinutes) {
        Date currentDate = new Date();
        long timeInMilliseconds = currentDate.getTime();
        long validityPeriodInInMilliseconds = (long) validityPeriodInMinutes * 60 * 1000;
        return new Date(timeInMilliseconds + validityPeriodInInMilliseconds);
    }

    private void isVerificationCodeExpired(VerificationCodeEntity verificationCode, Date expirationDate) {
        if (verificationCode.getDateOfExpiration().before(expirationDate))
            throw new VerificationCodeExpiredException();
    }

    private void verifyVerificationCodesMatch(VerificationCodeEntity retrievedVerificationCode, String sentVerificationCode) {
        if (!retrievedVerificationCode.getCode().equals(sentVerificationCode))
            throw new CannotFindVerificationCodeException();
    }

    private boolean isVerificationCodeTypeEmail(VerificationCodeType verificationCodeType) {
        return verificationCodeType == VerificationCodeType.EMAIL;
    }

    private boolean isVerificationCodeTypePhone(VerificationCodeType verificationCodeType) {
        return verificationCodeType == VerificationCodeType.PHONE;
    }
}
