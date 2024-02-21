package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class ChangeSignature extends BaseCommand {
    private boolean canDelete;//true=change only book request, false=change accountLongName request
    private boolean changePassbook;//true=PASSBOOK,false=Signature (EPASSBOOK)
}
