package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.deposits.model.AccountAddress;

@Data
public class ChangeAddress extends BaseCommand {
    private AccountAddress accountAddress;
    private String accountAddressText;
}