package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class CompletedListSearchEvent {
    private final String eventType;
    private final String function;
    private final CaseInfo caseInfo;
}
