package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class KycTransaction {
    private String deposit;
    private String depositAmount;
    private String withdraw;
    private String withdrawAmount;
    private String buy;
    private String buyAmount;
    private String sell;
    private String sellAmount;
}
