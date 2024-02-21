package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class MutualFundAccount {
    private String clientNumber;
    private String receiveAccount;
    private String dividendTaxDeduct;
    private String email;
    private String firstName;
    private String lastName;
    private String investObjective;
    private List<String> sourceOfFunds;
}
