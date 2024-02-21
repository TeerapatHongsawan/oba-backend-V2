package th.co.scb.onboardingapp.model.approval;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ValidateFieldsConfig {
    private final Set<String> mandatory = new HashSet<>();
    private final Set<String> addresses = new HashSet<>();
}
