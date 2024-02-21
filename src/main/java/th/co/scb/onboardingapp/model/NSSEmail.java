package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class NSSEmail {
    private String email;
    private String nssProductType;
    private String minimumAmountAlert;
    private String registerStatus;
    private String failedReason;
}
