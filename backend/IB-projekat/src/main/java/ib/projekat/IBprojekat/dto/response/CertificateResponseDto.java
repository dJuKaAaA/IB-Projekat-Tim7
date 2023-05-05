package ib.projekat.IBprojekat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateResponseDto {

    private Long id;
    private String serialNumber;
    private String type;
    private UserRefResponseDto issuer;
    private UserRefResponseDto issuedTo;
    private String startDate;
    private String endDate;
    private String publicKey;
    private String signature;
    private Boolean isPulled;

}
