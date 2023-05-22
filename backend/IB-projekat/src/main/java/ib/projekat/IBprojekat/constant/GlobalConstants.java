package ib.projekat.IBprojekat.constant;

import org.springframework.stereotype.Component;

@Component
public class GlobalConstants {

    public final static String jksPassword = "123456";
    public final static String jksEntriesPassword = "123456";
    public final String JKS_PATH = "src/main/resources/keystore/ib-projekat.jks";
    public final long ONE_YEAR_IN_MILLIS = 1000L * 60L * 60L * 24L * 365L;
    public final String ACCOUNT_SID = "AC278438aec92b584dacab40f92d2ff838";
    public final String AUTH_TOKEN = "728d419329682de4cf9957f09dbb768e";
    public final String PHONE_NUMBER = "+16205089516";
    // when changing the outdated password, how many past passwords should be compared to the new password
    // starting with the latest outdated password
    public final int PASSWORD_NON_MATCH_COUNT = 1;
    public final String GOOGLE_RECAPTCHA_VERIFICATION_URL = "https://www.google.com/recaptcha/api/siteverify";
    public final String GOOGLE_RECAPTCHA_SECRET_KEY = "6LeaQxEmAAAAAN0wy39ToWH3U7_LKx0d4RtDYw2r";

    private final long TWO_MINUTES_IN_MILLIS = 1000L * 60L * 2L;
    public final long PASSWORD_VALIDATION_IN_MILLIS = TWO_MINUTES_IN_MILLIS;


}
