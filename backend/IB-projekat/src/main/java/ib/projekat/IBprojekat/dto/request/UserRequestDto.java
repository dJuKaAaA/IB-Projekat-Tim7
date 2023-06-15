package ib.projekat.IBprojekat.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank(message = "Name not provided!")
    @Length(max = 50, message = "Name cannot have more than 50 character!")
    private String name;

    @NotBlank(message = "Surname not provided!")
    @Length(max = 50, message = "Surname cannot have more than 50 character!")
    private String surname;

    @NotBlank(message = "Phone number not provided!")
    @Pattern.List({
            @Pattern(regexp = "^\\+381.*", message = "Phone must start with +381"),
            @Pattern(regexp = "^(?=.{8,9}).+", message = "Phone number have between 8 and 9 digits")
    })
    private String phoneNumber;

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
