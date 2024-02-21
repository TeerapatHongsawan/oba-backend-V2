package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class DebitCardService extends EcInfo {
    private String accountNumber;
    private String accountBranchId;
    private String email;
    private DebitCard debitCard;
}
