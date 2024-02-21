package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class LoanDetails {
    String transactionId;
    String revolvingAccountNo;
    String loanAccountNo;
    String citizenId;
    String productId;

    // Optional in Car/Motorbike but require in truck
    String accountNumber;
    String accountName;
    String bankCode;
    String loanType;
    String approvedAmount;

    // use on re-issue
    String acceptChaiyoToC;
    String acceptConsentPopup;
    String cifId;
    String chaiyoCardNo;
    String chaiyoCardRef;
    Boolean cardUpdateStatus;

    // use for deepLink back
    String appId;

}
