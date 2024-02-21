package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class ChangeAccountLongName extends BaseCommand {
    private String accountLongName;
    private boolean sameCardName;
}