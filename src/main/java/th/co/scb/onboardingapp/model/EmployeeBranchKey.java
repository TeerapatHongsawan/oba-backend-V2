package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeBranchKey implements Serializable {

    private String employeeId;
    private String branchId;

}
