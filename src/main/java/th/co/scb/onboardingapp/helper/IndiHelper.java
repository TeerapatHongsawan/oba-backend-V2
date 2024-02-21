package th.co.scb.onboardingapp.helper;


import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

public class IndiHelper {

    private static int rmIdLength = 30;

    private IndiHelper() {
    }

    public static String generateRQUID() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
        int random = generateRandom(100000);
        return sdf.format(dt) + String.format("%05d", random);
    }

    private static int generateRandom(int maximumValue) {
        SecureRandom ranGen = new SecureRandom();
        return ranGen.nextInt(maximumValue);
    }

    public static String formatRmId(String rmId) {
        if (rmId.length() < rmIdLength) {
            String prefix = "0014";
            return String.format("%s%26s", prefix, rmId).replace(" ", "0");
        }
        return rmId;
    }

    public static <T> Optional<T> getOrElse(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public static String incomeRangeCodeMapping(Long income) {
        if (income == 0) {
            return "8";
        } else if (income <= 10000) {
            return "1";
        } else if (income <= 20000) {
            return "2";
        } else if (income <= 30000) {
            return "3";
        } else if (income <= 50000) {
            return "4";
        } else if (income <= 100000) {
            return "5";
        } else if (income <= 200000) {
            return "6";
        } else {
            return "7";
        }
    }

}
