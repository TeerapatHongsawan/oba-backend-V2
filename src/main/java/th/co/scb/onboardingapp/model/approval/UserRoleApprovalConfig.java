package th.co.scb.onboardingapp.model.approval;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class UserRoleApprovalConfig {
    private final Set<Requirement> requirement = new LinkedHashSet<>();
    private boolean facial;
    private String facialResult;
}