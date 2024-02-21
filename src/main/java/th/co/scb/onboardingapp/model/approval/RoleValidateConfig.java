package th.co.scb.onboardingapp.model.approval;

import lombok.Data;

import java.util.Set;

@Data
public class RoleValidateConfig {
    private Set<String> roles;
    private ValidateFieldsConfig values;
}
