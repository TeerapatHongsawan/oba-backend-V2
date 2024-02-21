package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.scb.onboardingapp.model.approval.Permissions;


import java.util.Map;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMe {
    private String employeeId;
    private String firstNameEn;
    private String lastNameEn;
    private String firstNameThai;
    private String lastNameThai;
    private String ocCode;
    private String ocName;
    private String ocNameEn;
    private String samOcCode;
    private String branchId;
    private String branchNameThai;
    private String branchNameEn;
    private String approvalBranchId;
    private Permissions permissions;
    private Set<String> roles;
    private Map<String, String> licenses;
}