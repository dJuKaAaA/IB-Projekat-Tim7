package ib.projekat.IBprojekat;

import ib.projekat.IBprojekat.certificate.CertificateGenerator;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.CertificateRepository;
import ib.projekat.IBprojekat.dao.PasswordHistoryRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.entity.PasswordHistoryEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.service.impl.CertificateDemandService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Security;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
@RequiredArgsConstructor
public class IbProjekatApplication {

    private final UserRepository userRepository;

    private final CertificateGenerator certificateGenerator;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final CertificateRepository certificateRepository;
    private final PasswordEncoder passwordEncoder;
    private final CertificateDemandService certificateDemandService;
    private final GlobalConstants globalConstants;

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        SpringApplication.run(IbProjekatApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> setDevelopmentData();
    }

    private String encryptPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    private void setDevelopmentData() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, GlobalConstants.passwordValidationInMinutes);
        Date passwordExpirationDate = calendar.getTime();

        // creating the admin
        //===================================================================
        UserEntity admin = UserEntity.builder()
                .name("Andrew")
                .surname("Tate")
                .phoneNumber("+3812383929")
                .email("admin@email.com")
                .password(passwordEncoder.encode("Admin123"))
                .role(Role.ADMIN)
                .dateForChangePassword(passwordExpirationDate)
                .enabled(true)
                .build();
        admin = userRepository.save(admin);

        PasswordHistoryEntity passwordHistoryEntity = PasswordHistoryEntity.builder()
                .password(encryptPassword("Admin123"))
                .user(admin)
                .passwordCreationDate(new Date())
                .build();
        passwordHistoryRepository.save(passwordHistoryEntity);


        //===================================================================

        // creating a custom user
        //===================================================================
        UserEntity user = UserEntity.builder()
                .name("Ivan")
                .surname("Martic")
                .phoneNumber("+381604672999")
                    .email("ivanmartic311@gmail.com")
                .password(passwordEncoder.encode("Martic123"))
                .role(Role.USER)
                .enabled(true)
                .build();

        user = userRepository.save(user);
        //===================================================================

        // creating the root certificate
        //===================================================================
        certificateDemandService.create(CertificateDemandRequestDto.builder()
                .requesterId(1L)
                .reason("This is a reason :)")
                .type("ROOT")
                .build());

        certificateDemandService.create(CertificateDemandRequestDto.builder()
                .requesterId(2L)
                .reason("This is a reason :)")
                .type("INTERMEDIATE")
                .requestedSigningCertificateId(1L)
                .build());
        //===================================================================

    }

}

