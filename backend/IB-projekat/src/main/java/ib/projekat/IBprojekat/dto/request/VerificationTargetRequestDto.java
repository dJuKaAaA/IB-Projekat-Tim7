package ib.projekat.IBprojekat.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationTargetRequestDto {

    @Pattern(regexp = "^$|^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$", message = "Invalid email format!")
    private String email;

    @Pattern(regexp = "^(|\\+381\\d{8,13})$", message = "Invalid phone number")
    private String phoneNumber;
}
