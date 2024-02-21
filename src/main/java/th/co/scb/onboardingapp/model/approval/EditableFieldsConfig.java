package th.co.scb.onboardingapp.model.approval;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class EditableFieldsConfig {
    private final Set<String> basicInformation = new HashSet<>();
    private final Set<String> currentAddress = new HashSet<>();
    private final Set<String> officeAddress = new HashSet<>();
    private final Set<String> contact = new HashSet<>();
}
