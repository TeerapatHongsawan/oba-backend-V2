package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class IssueNewCard extends BaseCommand {
    private String branchCode;
    private boolean sendToContactAddress;
}
