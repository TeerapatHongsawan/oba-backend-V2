package th.co.scb.onboardingapp.model.approval;

import lombok.Data;

import java.util.Set;

@Data
public class Permissions {
    private Set<String> products;
    private Set<UserRoleApprovalConfig> approvalConfig;
    private EditableFieldsConfig editableFields;
    private ValidateFieldsConfig validateFields;
    private DashboardFieldsConfig dashboardFields;
    private Set<String> additionalRoles;
    private Object consent;
    private Object convertEpassbook;
}