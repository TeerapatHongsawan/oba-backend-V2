package th.co.scb.onboardingapp.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.right;

public interface DocumentHelper {

    static String buildUploadSessionId(String nextVal, String branchCode, String prefix) {
        String pCode = "01";
        String suffix = leftPad(nextVal, 4, "0");

        return prefix + padBranchCode(branchCode) + getCurrentDate() + pCode + suffix;
    }

    static String padBranchCode(String branchCode) {
        if (branchCode != null && branchCode.length() <= 4) {
            return leftPad(branchCode, 4, "0");
        } else {
            return right(branchCode, 4);
        }
    }

    static String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
