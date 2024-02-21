package th.co.scb.onboardingapp.model.approval;

import lombok.Data;

import java.util.Set;

@Data
public class RoleAdditionalRolesConfig {
    private Set<String> roles;
    private Set<String> values;
}
