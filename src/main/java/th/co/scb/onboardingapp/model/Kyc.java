package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class Kyc {
    private String kycFormType;
    private String kycScore;
    private String originKYCScore;
    private String kycDateTime;
    private KycPurpose purposeObj;
    private KycSourceOfIncome sourceOfIncomeObj;
    private KycTransaction transactionObj;
    private KycTransaction transactionFundObj;
    private KycRisk risk;
}

