package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.mutualfunds.model.BaaResponse;

@Data
public class MutualFund extends EcInfo {
    private String customerStatus;
    private String riskLevel;
    private String reasonCode;
    /* If seller select cash, will pass "0000000000" */
    private String accountBranchCode;
    private String accountBranchName;
    private String accountType;
    private String accountNumber;
    private String accountName;
    private String clientNumber;
    private RiskScore riskScore;
    private BaaResponse baaResponse;
}

