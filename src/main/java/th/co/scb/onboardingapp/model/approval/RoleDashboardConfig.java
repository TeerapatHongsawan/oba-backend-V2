package th.co.scb.onboardingapp.model.approval;

import lombok.Data;

import java.util.Set;

@Data
public class RoleDashboardConfig {
    private Set<String> roles;
    private DashboardFieldsConfig values;
}
