package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.Date;

@Data
public class ApprovalListEvent {
    private final String eventType;
    private final String function;
    private final String employeeId;
    private final String caseId;
    private final Date updatedTime;

}
