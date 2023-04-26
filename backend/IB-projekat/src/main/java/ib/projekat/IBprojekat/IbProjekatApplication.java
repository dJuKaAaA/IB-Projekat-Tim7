package ib.projekat.IBprojekat;

import ib.projekat.IBprojekat.certificate.CertificateGenerator;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreWriter;
import ib.projekat.IBprojekat.constant.CertificateType;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.CertificateRepository;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.CertificateDemandRequestDto;
import ib.projekat.IBprojekat.entity.CertificateEntity;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.service.interf.ICertificateDemandService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.*;
import java.security.cert.X509Certificate;

@SpringBootApplication
@RequiredArgsConstructor
public class IbProjekatApplication {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(IbProjekatApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> setDevelopmentData();
	}

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ICertificateDemandService certificateDemandService;

	private void setDevelopmentData() {

		// creating the admin
		//===================================================================
		UserEntity admin = UserEntity.builder()
				.name("Andrew")
				.surname("Tate")
				.phoneNumber("+38163111111")
				.email("admin@email.com")
				.password(passwordEncoder.encode("Admin123"))
				.role(Role.ADMIN)
				.enabled(true)
				.build();

		admin = userRepository.save(admin);
		//===================================================================

		// creating the root certificate
		//===================================================================
		certificateDemandService.create(CertificateDemandRequestDto.builder()
						.requesterId(1L)
						.reason("This is a reason :)")
						.type("ROOT")
						.build());
		//===================================================================


	}

}
