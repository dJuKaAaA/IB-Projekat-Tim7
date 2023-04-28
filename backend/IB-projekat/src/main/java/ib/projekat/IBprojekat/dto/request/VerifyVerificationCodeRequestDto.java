package ib.projekat.IBprojekat.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyVerificationCodeRequestDto {

    @Pattern(regexp = "^$|^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$", message = "Invalid email format!")
    private String email;

    @Pattern(regexp = "^(|\\+381\\d{8,13})$", message = "Invalid phone number!")
    private String phoneNumber;

    private String code;
}
