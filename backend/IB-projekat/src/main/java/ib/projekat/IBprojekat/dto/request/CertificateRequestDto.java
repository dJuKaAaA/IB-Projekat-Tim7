package ib.projekat.IBprojekat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateRequestDto {

    private Boolean isEnd;
    private Long issuerId;
    private Long issuedToId;
    private String publicKey;
    private String validFrom;
    private String validTo;

}
