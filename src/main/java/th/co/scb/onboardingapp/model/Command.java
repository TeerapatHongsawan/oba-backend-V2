package th.co.scb.onboardingapp.model;

import lombok.Data;


@Data
public class Command {
    private Boolean ekycSignatureUpdate;
    private ChangeAccountLongName changeAccountLongName;
    private ChangeAddress changeAddress;
    private ChangeSignature changeSignature;
    private ChangeEmail changeEmail;
    private IssueNewCard issueNewCard;
    private ChangeEpassbook changeEpassbook;
}