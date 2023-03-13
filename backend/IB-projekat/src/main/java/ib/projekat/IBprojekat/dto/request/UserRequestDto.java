package ib.projekat.IBprojekat.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank(message = "Name not provided!")
    private String name;
    @NotBlank(message = "Surname not provided!")
    private String surname;
    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email not provided!")
    private String email;
    @Pattern.List({
            @Pattern(regexp = "^(?=.{8,20}).+", message = "Password must be between 8 and 20 characters!"),
            @Pattern(regexp = "^(?=.*[0-9]).+", message = "Password must contain at least one number!"),
            @Pattern(regexp = "^(?=.*[a-z]).+", message = "Password must contain at least one lowercase letter!"),
            @Pattern(regexp = "^(?=.*[A-Z]).+", message = "Password must contain at least one uppercase letter!")
    })
    @NotNull(message = "Password not provided!")
    private String password;
}
