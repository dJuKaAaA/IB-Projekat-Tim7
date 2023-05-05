package ib.projekat.IBprojekat;

import ib.projekat.IBprojekat.certificate.CertificateGenerator;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreWriter;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.CertificateRepository;
import ib.projekat.IBprojekat.dao.PasswordHistoryRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.entity.PasswordHistoryEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
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
    private final KeyStoreWriter keyStoreWriter;

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

        PasswordHistoryEntity passwordHistoryEntity = PasswordHistoryEntity.builder()
                .password(encryptPassword("Admin123"))
                .user(admin)
                .passwordCreationDate(new Date())
                .build();
        passwordHistoryRepository.save(passwordHistoryEntity);


        //===================================================================

        // creating the root certificate
        //===================================================================
//		KeyPair keyPair = certificateGenerator.generateKeyPair();
//        X509Certificate certificate = certificateGenerator.generateCertificate(
//				admin,
//				admin,
//				keyPair.getPublic(),
//				keyPair.getPrivate()
//        );
//
//        keyStoreWriter.loadKeyStore(GlobalConstants.jksCertificatesPath, GlobalConstants.jksPassword.toCharArray());
//        keyStoreWriter.write(
//                certificate.getSerialNumber().toString(),
//                keyPair.getPrivate(),
//                GlobalConstants.jksEntriesPassword.toCharArray(),
//                certificate
//        );
//        keyStoreWriter.saveKeyStore(GlobalConstants.jksCertificatesPath, GlobalConstants.jksPassword.toCharArray());
//
//        CertificateEntity certificateEntity = CertificateEntity.builder()
//                .serialNumber(certificate.getSerialNumber().toString())
//                .type(CertificateType.ROOT)
//                .issuer(admin)
//                .issuedTo(admin)
//                .startDate(certificate.getNotBefore())
//                .endDate(certificate.getNotAfter())
//                .publicKey(keyPair.getPublic())
//                .signature(certificate.getSignature())
//                .build();
//
//        certificateEntity = certificateRepository.save(certificateEntity);
//		certificateEntity.setSigner(certificateEntity);
//		certificateEntity = certificateRepository.save(certificateEntity);
        //===================================================================

    }

}
