package ib.projekat.IBprojekat;

import ib.projekat.IBprojekat.certificate.CertificateGenerator;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreReader;
import ib.projekat.IBprojekat.constant.CertificateType;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.CertificateRepository;
import ib.projekat.IBprojekat.dao.PasswordHistoryRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.dto.response.CertificateDemandResponseDto;
import ib.projekat.IBprojekat.entity.CertificateEntity;
import ib.projekat.IBprojekat.entity.PasswordHistoryEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.service.impl.CertificateDemandService;
import ib.projekat.IBprojekat.service.impl.CertificateService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Security;
import java.security.cert.X509Certificate;
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
    private final CertificateService certificateService;
    private final GlobalConstants globalConstants;
    private final KeyStoreReader keyStoreReader;
    private final Logger logger = LoggerFactory.getLogger(IbProjekatApplication.class);

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
                .name("Andrew")
                .surname("Tate")
                .phoneNumber("+381604672999")
                .email("ivan.djukanovic07@gmail.com")
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
                .email("ivanmartic311@gmail.com")
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
        X509Certificate trustedCert = (X509Certificate) keyStoreReader.readCertificate(
                globalConstants.JKS_PATH,
                globalConstants.jksPassword,
                globalConstants.TRUSTED_CERT_SERIAL_NUMBER
        );

        CertificateEntity trustedCertEntity = CertificateEntity.builder()
                .serialNumber(globalConstants.TRUSTED_CERT_SERIAL_NUMBER)
                .signer(null)
                .type(CertificateType.ROOT)
                .issuer(admin)
                .issuedTo(admin)
                .startDate(trustedCert.getNotBefore())
                .endDate(trustedCert.getNotAfter())
                .publicKey(trustedCert.getPublicKey())
                .signature(trustedCert.getSignature())
                .build();
        trustedCertEntity = certificateRepository.save(trustedCertEntity);
        trustedCertEntity.setSigner(trustedCertEntity);
        trustedCertEntity = certificateRepository.save(trustedCertEntity);

//        CertificateDemandResponseDto response = certificateDemandService.create(CertificateDemandRequestDto.builder()
//                .requesterId(user1.getId())
//                .requestedSigningCertificateId(trustedCertEntity.getId())
//                .reason("Some reason")
//                .type("INTERMEDIATE")
//                .build());
//        certificateService.create(response.getId());
        //===================================================================

    }

}

