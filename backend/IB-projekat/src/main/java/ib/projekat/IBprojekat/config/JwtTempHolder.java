package ib.projekat.IBprojekat.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class JwtTempHolder {
    private String jwt;
}
