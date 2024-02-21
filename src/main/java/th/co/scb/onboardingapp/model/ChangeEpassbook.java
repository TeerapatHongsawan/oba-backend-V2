package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class ChangeEpassbook extends BaseCommand {

    /**
     * Is this passbook number valid
     */
    private boolean passbookValid;
    private boolean onLoad;
    private String passbookNumber;
    private String email;
    private String productCode;
    private String cbsProductCode;
    private String ePassbookProductCode;
    private String ePassbookCBSproductCode;
    private String smsRelated;
    private String adjustCode;
    private boolean deleted;
    private String updateStatus;
    private String emailSendSuccess;
    private Boolean isGetBeneficiariesInquiry;
    private List<Beneficiary> beneficiaryList;
}
