package th.co.scb.onboardingapp.model.approval;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class DashboardFieldsConfig {
    private final Set<String> changeCustomerInfo = new HashSet<>();
    private final Set<String> changeAccountInfo = new HashSet<>();
    private final Set<String> changePassbookSignature = new HashSet<>();

}
