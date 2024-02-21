package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class LogRequest {
    private String caseId;
    private String level;
    private String message;
    private String timestampISO;
    private String deviceId;
}
