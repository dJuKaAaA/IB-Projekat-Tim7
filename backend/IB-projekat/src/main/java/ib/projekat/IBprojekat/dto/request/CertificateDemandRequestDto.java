package ib.projekat.IBprojekat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDemandRequestDto {

    @NotNull(message = "Information whether the certificate is end or intermediate not provided!")
    private Boolean isEnd;
    @NotNull(message = "Request issuer id not provided!")
    private Long requestedIssuerId;
    @NotNull(message = "Requester id not provided!")
    private Long requesterId;

}
