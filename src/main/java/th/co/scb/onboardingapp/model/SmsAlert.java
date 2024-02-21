package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class SmsAlert {
    private String mobileNo;
    private String feeType;
    private String feeAccountType;
    private int minimumAmountAlert;
    private String baseFeeType;
    private String registerStatus;
    private String failedReason;
}
