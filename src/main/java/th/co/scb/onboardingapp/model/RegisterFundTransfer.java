package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class RegisterFundTransfer {
    private String affiliateType;
    private String bankCode;
    private String bankName;
    private String currentAccountNumber;
    private String deviceId;
    private String feeType;
    private String processingBranchCode;
    private String savingsAccountNumber;
    private String transferType;
    private String activeRTFT;
    private String regisStatus;
}

