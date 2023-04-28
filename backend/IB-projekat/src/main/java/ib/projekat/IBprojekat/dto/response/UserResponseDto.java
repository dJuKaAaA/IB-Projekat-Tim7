package ib.projekat.IBprojekat.dto.response;

import ib.projekat.IBprojekat.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;

    public UserResponseDto(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.name = userEntity.getName();
        this.surname = userEntity.getSurname();
        this.phoneNumber = userEntity.getPhoneNumber();
        this.email = userEntity.getEmail();
    }

}
