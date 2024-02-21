package th.co.scb.onboardingapp.model.approval;

import lombok.Data;

import java.util.Set;

@Data
public class ApprovalConfig {
    private Set<String> roles;
    private Boolean facial;
    private String facialResult;
    private Boolean ciChange;
    private Boolean addMobilePhone;
    private Boolean requireSignature;
    private Boolean specialFeeCode;
    private Boolean newCustomer;
    private Boolean caOpen;
    private Boolean rtftWaive;
    private Boolean nameChange;
    private Boolean income;
    private Boolean titleChange;
    private Boolean typeChange;
    private Boolean ciDetail;
    private Boolean addressChange;
    private Boolean changePhone;
    private Boolean changeEmail;
    private Boolean changeAcctName;
    private Boolean acctName;
    private Boolean changeMailAddr;
    private Boolean changeAcctMail;
    private Boolean changeSignature;
    private Boolean convertPB;
    private Boolean uplift;
    private String kycLevel1;
    private String kycLevel2;
    private String kycLevel3;
}
