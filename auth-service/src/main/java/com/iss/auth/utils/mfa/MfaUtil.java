package com.iss.auth.utils.mfa;
import java.util.Random;

public class MfaUtil {
    public static String generateMfaCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999)); // 000000~999999
    }
}

