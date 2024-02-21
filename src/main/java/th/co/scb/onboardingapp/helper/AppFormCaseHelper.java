package th.co.scb.onboardingapp.helper;

import th.co.scb.onboardingapp.model.AdditionalCaseInfo;
import th.co.scb.onboardingapp.model.BranchProfile;
import th.co.scb.onboardingapp.model.CaseInfo;

public class AppFormCaseHelper {

    private AppFormCaseHelper() {
    }


    public static String getBranchName(CaseInfo caseInfo) {
        if (caseInfo == null) {
            return "";
        }

        AdditionalCaseInfo additionalInfo = caseInfo.getAdditionalInfo();
        if (additionalInfo == null) {
            return "";
        }

        BranchProfile branchProfile = additionalInfo.getBranchProfile();
        if (branchProfile == null) {
            return "";
        }

        return branchProfile.getBranchNameTH();
    }

}