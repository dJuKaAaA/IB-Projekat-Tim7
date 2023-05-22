package ib.projekat.IBprojekat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyLoginRequestDto {
    private String email;
    private String code;
}
