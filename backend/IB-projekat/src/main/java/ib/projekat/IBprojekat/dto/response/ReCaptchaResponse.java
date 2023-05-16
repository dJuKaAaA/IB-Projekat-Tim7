package ib.projekat.IBprojekat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReCaptchaResponse {
    private boolean success;
    private String callenge_ts;
    private String hostname;
}
