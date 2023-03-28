package ib.projekat.IBprojekat.service.impl;

import ib.projekat.IBprojekat.certificate.CertificateGenerator;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreReader;
import ib.projekat.IBprojekat.certificate.keystore.KeyStoreWriter;
import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.LoginRequestDto;
import ib.projekat.IBprojekat.dto.request.UserRequestDto;
import ib.projekat.IBprojekat.dto.response.TokenResponseDto;
import ib.projekat.IBprojekat.dto.response.UserResponseDto;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.exception.EmailAlreadyExistsException;
import ib.projekat.IBprojekat.exception.InvalidCredentialsException;
import ib.projekat.IBprojekat.exception.UserNotFoundException;
import ib.projekat.IBprojekat.service.interf.IAuthService;
import ib.projekat.IBprojekat.websecurity.JwtService;
import ib.projekat.IBprojekat.websecurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Optional;

@Service("AuthService")
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final KeyStoreWriter keyStoreWriter;
    private final CertificateGenerator certificateGenerator;

    @Override
    public TokenResponseDto login(LoginRequestDto loginRequest) {
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
        String jwt = jwtService.generateToken(new UserDetailsImpl(user));

        return new TokenResponseDto(jwt);

    }

    @Override
    public UserResponseDto createAccount(UserRequestDto userRequest) {
        Optional<UserEntity> potentiallyExistingUser = userRepository.findByEmail(userRequest.getEmail());
        if (potentiallyExistingUser.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        KeyPair keyPair = certificateGenerator.generateKeyPair();

        UserEntity newUser = UserEntity.builder()
                .name(userRequest.getName())
                .surname(userRequest.getSurname())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .publicKey(keyPair.getPublic())
                .build();
        newUser = userRepository.save(newUser);

        // saving the private key for the user
        X509Certificate dummyCert;
        try {
            dummyCert = generateSelfSignedCertificate(keyPair.getPublic(), keyPair.getPrivate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        keyStoreWriter.loadKeyStore(GlobalConstants.jksPrivateKeysPath, GlobalConstants.jksPassword.toCharArray());
        keyStoreWriter.write(
                newUser.getEmail(),
                keyPair.getPrivate(),
                GlobalConstants.jksEntriesPassword.toCharArray(),
                dummyCert);
        keyStoreWriter.saveKeyStore(GlobalConstants.jksPrivateKeysPath, GlobalConstants.jksPassword.toCharArray());

        return UserResponseDto.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .surname(newUser.getSurname())
                .email(newUser.getEmail())
                .build();
    }

    private X509Certificate generateSelfSignedCertificate(PublicKey publicKey, PrivateKey privateKey) throws Exception {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setSubjectDN(new X500Principal("CN=Dummy Certificate"));
        certGen.setIssuerDN(new X500Principal("CN=Dummy Certificate"));
        certGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30)); // 30 days ago
        certGen.setNotAfter(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365)); // 1 year from now
        certGen.setPublicKey(publicKey);
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        return certGen.generate(privateKey);
    }

}
