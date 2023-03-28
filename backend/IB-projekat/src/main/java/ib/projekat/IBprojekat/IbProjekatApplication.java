package ib.projekat.IBprojekat;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class IbProjekatApplication {

	public static void main(String[] args) {
		SpringApplication.run(IbProjekatApplication.class, args);
		Security.addProvider(new BouncyCastleProvider());
	}

}
