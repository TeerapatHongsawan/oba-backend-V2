package th.co.scb.onboardingapp.model.approval;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class PermissionConfig {
    private Set<RoleProductConfig> products;
    private Set<ApprovalConfig> approvalConfiguration;
    private Set<RoleValidateConfig> validate;
    private Set<RoleEditableConfig> editable;
    private Set<RoleAdditionalRolesConfig> additionalRoles;
    private Set<RoleDashboardConfig> dashboard;
}
