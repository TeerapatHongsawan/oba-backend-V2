package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class AppFormApprover {
    private String approverId;
    private String approverDate;
    private String approverIdWithoutPrefix;

    public AppFormApprover(String approverId, String approverDate, String approverIdWithoutPrefix) {
        this.approverId = approverId;
        this.approverDate = approverDate;
        this.approverIdWithoutPrefix = approverIdWithoutPrefix;
    }
}
