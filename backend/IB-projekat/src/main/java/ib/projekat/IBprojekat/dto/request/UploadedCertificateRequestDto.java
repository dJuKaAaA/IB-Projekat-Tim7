package ib.projekat.IBprojekat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedCertificateRequestDto {

    @NotBlank(message = "Cannot upload blank certificate!")
    private String base64Certificate;

}
