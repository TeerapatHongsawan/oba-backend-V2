package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.Set;

@Data
public class LoginBranchInfo {
    private String employeeId;
    private String branchId;
    private String approvalBranchId;

    /**
     *
     * - case role ("maker","checker") can be both at the same time
     * - user group ("branch","wealth","wealth_specialist","dsa","private","ssme","bulk") only one at a time
     */
    private Set<String> roles;
}
