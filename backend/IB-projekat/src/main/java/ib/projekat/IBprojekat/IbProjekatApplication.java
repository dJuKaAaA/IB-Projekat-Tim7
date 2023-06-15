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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Security;
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

    private void setDevelopmentData() {
        Date passwordExpirationDate = new Date(System.currentTimeMillis() + globalConstants.PASSWORD_VALIDATION_IN_MILLIS);

        // creating the admin
        //===================================================================
        UserEntity admin = UserEntity.builder()
                .name("Ivan")
                .surname("Martic")
                .phoneNumber("+381604672999")
                .email("ivanmartic311@gmail.com")
                .password(passwordEncoder.encode("Admin123"))
                .role(Role.ADMIN)
                .dateForChangePassword(passwordExpirationDate)
                .enabled(true)
                .build();
        admin = userRepository.save(admin);

        PasswordHistoryEntity passwordHistoryEntityAdmin = PasswordHistoryEntity.builder()
                .password(admin.getPassword())
                .user(admin)
                .passwordCreationDate(new Date())
                .build();
        passwordHistoryRepository.save(passwordHistoryEntityAdmin);

        //===================================================================

        // creating a custom user
        //===================================================================
        UserEntity user1 = UserEntity.builder()
                .name("Neko")
                .surname("Neki")
                .phoneNumber("+3812383929")
                .email("ivan.djukanovic07@gmail.com")
                .password(passwordEncoder.encode("Neko1234"))
                .role(Role.USER)
                .dateForChangePassword(passwordExpirationDate)
                .enabled(true)
                .build();
        user1 = userRepository.save(user1);

        PasswordHistoryEntity passwordHistoryEntityUser1 = PasswordHistoryEntity.builder()
                .password(admin.getPassword())
                .user(user1)
                .passwordCreationDate(new Date())
                .build();
        passwordHistoryRepository.save(passwordHistoryEntityUser1);
        //===================================================================

        // creating the root certificate
        //===================================================================
        certificateDemandService.create(CertificateDemandRequestDto.builder()
                .requesterId(admin.getId())
                .reason("This is a reason :)")
                .type("ROOT")
                .build());
        //===================================================================

    }

}

