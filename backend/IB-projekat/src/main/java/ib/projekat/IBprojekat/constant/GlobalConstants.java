package ib.projekat.IBprojekat.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalConstants {

    public final String jksCertificatesPath = "src/main/resources/keystore/ib-projekat.jks";
    @Value("${jksPassword}")
    public String jksPassword;
    @Value("${jksPassword}")
    public String jksEntriesPassword;
    public final long oneYearInMillis = 1000L * 60L * 60L * 24L * 365L;

    public final String ACCOUNT_SID = "AC278438aec92b584dacab40f92d2ff838";
    public final String AUTH_TOKEN = "370acec769d42adb8588f032f99f615a";

    public final String PHONE_NUMBER = "+16205089516";

}
