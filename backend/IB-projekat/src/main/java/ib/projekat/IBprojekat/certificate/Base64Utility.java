package ib.projekat.IBprojekat.certificate;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class Base64Utility {

    public String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public byte[] decode(String base64Data) {
        return Base64.getDecoder().decode(base64Data);
    }
}
