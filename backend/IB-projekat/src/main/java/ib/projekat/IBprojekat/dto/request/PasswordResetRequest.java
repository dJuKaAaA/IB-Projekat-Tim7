package ib.projekat.IBprojekat.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {

    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email not provided!")
    String email;

    @NotBlank(message = "Old password not provided!")
    String oldPassword;

    @Pattern.List({
            @Pattern(regexp = "^(?=.{8,20}).+", message = "Password must be between 8 and 20 characters!"),
            @Pattern(regexp = "^(?=.*[0-9]).+", message = "Password must contain at least one number!"),
            @Pattern(regexp = "^(?=.*[a-z]).+", message = "Password must contain at least one lowercase letter!"),
            @Pattern(regexp = "^(?=.*[A-Z]).+", message = "Password must contain at least one uppercase letter!")
    })
    @NotNull(message = "Password not provided!")
    String newPassword;

}
