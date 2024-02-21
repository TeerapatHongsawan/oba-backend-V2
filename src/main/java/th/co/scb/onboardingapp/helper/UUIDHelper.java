package th.co.scb.onboardingapp.helper;

import java.util.UUID;

public interface UUIDHelper {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
