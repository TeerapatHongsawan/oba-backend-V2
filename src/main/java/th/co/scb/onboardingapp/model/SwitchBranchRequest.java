package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class SwitchBranchRequest {
    private String branchId;
    private String deviceIp;
}
