package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApprovalPermissionKey implements Serializable {
    private String approvalBranchId;
    private String employeeId;
}
