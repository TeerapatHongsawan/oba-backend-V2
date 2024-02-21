package th.co.scb.onboardingapp.helper;


import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CustomerInfo;
import java.util.Arrays;

public class CaseHelper {

    private CaseHelper() {
    }

    public static final String DOCTYPE_ALIEN = "P7";
    public static final String DOCTYPE_PASSPORT = "P8";

    public static boolean isForeigner(CaseInfo caseInfo) {
        if (caseInfo == null) {
            return false;
        }

        return Arrays.stream(new String[]{DOCTYPE_ALIEN, DOCTYPE_PASSPORT}).anyMatch(s -> s.equalsIgnoreCase(caseInfo.getCustomerInfo().getDocType().toUpperCase()));
    }

    public static boolean isForeigner(String docType) {
        return Arrays.stream(new String[]{DOCTYPE_ALIEN, DOCTYPE_PASSPORT}).anyMatch(s -> s.equalsIgnoreCase(docType));
    }

    public static void setDefaultData(CaseInfo caseInfo) {
        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        if (customerInfo.getCustomerTypeCode() == null) {
            customerInfo.setCustomerTypeCode("21");
        }
        if (customerInfo.getSourceOfIncome() == null) {
            customerInfo.setSourceOfIncome("TH");
        }
        if (customerInfo.getOcCode() == null) {
            customerInfo.setOcCode("3261");
        }
        if (customerInfo.getBusinessUnitOwnerCode() != null) {
            customerInfo.setOcCode(customerInfo.getBusinessUnitOwnerCode());
        }
    }
}
