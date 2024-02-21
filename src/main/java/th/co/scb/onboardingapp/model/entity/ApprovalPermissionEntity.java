package th.co.scb.onboardingapp.model.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import th.co.scb.onboardingapp.model.AuthorizedLevel;


@Data
public class ApprovalPermissionEntity {
    private String approvalBranchId;
    private String employeeId;
    @Enumerated(EnumType.STRING)
    private AuthorizedLevel authorizedLevel;
}
