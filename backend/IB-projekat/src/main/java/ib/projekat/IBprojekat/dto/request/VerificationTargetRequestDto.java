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

//    @Pattern(regexp = "^$|^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$", message = "Invalid email format!")
    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email not provided!")
    private String email;

//    @Pattern(regexp = "^(|\\+381\\d{8,13})$", message = "Invalid phone number")
    @NotBlank(message = "Phone number not provided!")
    @Pattern.List({
            @Pattern(regexp = "^\\+381", message = "Phone must start with +381"),
            @Pattern(regexp = "^(?=.{8,9}).+", message = "Phone number have between 7 and 15 digits")
    })
    private String phoneNumber;
}
