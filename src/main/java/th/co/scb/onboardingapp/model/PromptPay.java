package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class PromptPay {
    private String type;
    private String id;
    private String registerStatus;
    private String deregisterStatus;
    private String failedReason;
}
