package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.Map;

@Data
public class CustomerInformationEvent {
    private final CaseInfo caseInfo;
    private final Map<String, String> workflow;
}
