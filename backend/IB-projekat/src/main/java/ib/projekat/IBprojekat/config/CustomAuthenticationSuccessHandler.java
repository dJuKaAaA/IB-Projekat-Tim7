package ib.projekat.IBprojekat.config;

import ib.projekat.IBprojekat.constant.GlobalConstants;
import ib.projekat.IBprojekat.constant.Role;
import ib.projekat.IBprojekat.dao.UserRepository;
import ib.projekat.IBprojekat.dto.request.UserRequestDto;
import ib.projekat.IBprojekat.entity.UserEntity;
import ib.projekat.IBprojekat.websecurity.JwtService;
import ib.projekat.IBprojekat.websecurity.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    private final JwtTempHolder jwtTempHolder;
    private final GlobalConstants globalConstants;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Started OAuth login process");

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            OAuth2User oauth2User = oauthToken.getPrincipal();
            String jwt = "";
            if (oauth2User instanceof DefaultOAuth2User) {
                DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) oauth2User;
                String email = defaultOAuth2User.getAttribute("email");
                UserEntity user;
                Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
                if (userEntityOptional.isPresent()) {
                    user = userEntityOptional.get();

                } else {
                    user = UserEntity.builder()
                            .name(defaultOAuth2User.getAttribute("given_name"))
                            .surname(defaultOAuth2User.getAttribute("family_name"))
                            .phoneNumber("+381604672999")
                            .email(defaultOAuth2User.getAttribute("email"))
                            .password("Bratmoi" + String.valueOf((new Random()).nextLong()))
                            .role(Role.USER)
                            .enabled(true)
                            .dateForChangePassword(new Date(System.currentTimeMillis() + globalConstants.PASSWORD_VALIDATION_IN_MILLIS))
                            .build();
                    user = userRepository.save(user);
                }

                Map<String, Object> claims = new HashMap<>();
                claims.put("id", user.getId());
                claims.put("roles", List.of(user.getRole()));

                jwt = jwtService.generateToken(claims, new UserDetailsImpl(user));
            }

            jwtTempHolder.setJwt(jwt);

            logger.info("Successfully generated a jwt token with OAuth credentials");

            response.sendRedirect("https://localhost:4200/oauth-login");
        } else {
            logger.error("Unable to login with OAuth");
            OAuth2Error oauth2Error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
    }
}