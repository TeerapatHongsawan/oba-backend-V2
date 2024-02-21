package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.IdClass;
import lombok.Data;
import th.co.scb.onboardingapp.model.EmployeeBranchKey;


@Data
@IdClass(EmployeeBranchKey.class)
public class LoginBranchEntity {
    private String employeeId;
    private String branchId;
    private String approvalBranchId;
    private String roles;
}
