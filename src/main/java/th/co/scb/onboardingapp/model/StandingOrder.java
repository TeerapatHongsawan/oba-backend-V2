package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class StandingOrder {
    private boolean autoRenew;
    private String linkAccountNumber;
    private int dayofMonth;
    private String registerStatus;
    private String failedReason;
}
