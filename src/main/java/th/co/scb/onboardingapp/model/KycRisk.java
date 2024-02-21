package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class KycRisk {
    private String customer;
    private String beneficiary;
    private String approval;
    private String owner;
    private String guarantee;
}
