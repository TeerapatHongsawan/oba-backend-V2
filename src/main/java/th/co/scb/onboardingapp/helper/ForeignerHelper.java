package th.co.scb.onboardingapp.helper;

import th.co.scb.onboardingapp.model.CaseInfo;

import java.util.Arrays;

public class ForeignerHelper {

    public static final String DOCTYPE_ALIEN = "P7";
    public static final String DOCTYPE_PASSPORT = "P8";

    private ForeignerHelper() {
    }

    public static boolean isForeigner(CaseInfo caseInfo) {
        return Arrays.stream(new String[]{DOCTYPE_ALIEN, DOCTYPE_PASSPORT}).anyMatch(s -> s.equalsIgnoreCase(caseInfo.getCustomerInfo().getDocType()));
    }

    public static boolean isDocTypeForeigner(String docType) {
        return DOCTYPE_PASSPORT.equalsIgnoreCase(docType) || DOCTYPE_ALIEN.equalsIgnoreCase(docType);
    }

}
