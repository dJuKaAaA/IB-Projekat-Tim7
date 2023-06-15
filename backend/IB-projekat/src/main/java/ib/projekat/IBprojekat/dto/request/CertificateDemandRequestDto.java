package ib.projekat.IBprojekat.dto.request;

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
public class CertificateDemandRequestDto {

    @NotBlank(message = "Certificate type not provided!")
    @Pattern(regexp = "^(ROOT|INTERMEDIATE|END)$", message = "Certificate can only be type ROOT, INTERMEDIATE or END!")
    private String type;
    private Long requestedSigningCertificateId;
    @NotNull(message = "Requester id not provided!")
    private Long requesterId;
    @NotBlank(message = "Reason not provided")
    @Length(max = 300, message = "Reason cannot have more than 300 characters!")
    private String reason;

}
