package th.co.scb.onboardingapp.service.submission;


import th.co.scb.onboardingapp.model.CaseInfo;

public interface BaseSubmissionPlugin {
    String getName();

    default boolean isHardStop() {
        return false;
    }

    boolean isApply(CaseInfo caseInfo);

    void submit(CaseInfo caseInfo, SubmissionState state);
}


