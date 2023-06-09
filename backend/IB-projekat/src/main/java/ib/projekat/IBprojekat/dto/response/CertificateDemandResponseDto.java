package ib.projekat.IBprojekat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDemandResponseDto {

    private Long id;
    private String type;
    private Long requestedSigningCertificateId;
    private UserRefResponseDto requestedIssuer;
    private UserRefResponseDto requester;
    private String reason;
    private String status;

}
