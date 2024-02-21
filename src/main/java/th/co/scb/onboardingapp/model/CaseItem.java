package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CaseItem {
    private String caseId;
    private String docNo;
    private String docType;
    private String accountLongName;
    private String originalAccountLongName;
    private String accountShortName;
    private String approvalName;
    private String custNameEN;
    private String custNameTH;

    // add to use in Chai Yo
    private Boolean isUpdateChaiyoCardSuccess;
    private String casaDetailsLoanType;
    private CasaDetails casaDetails;
    private String acceptChaiyoToC;
    private String acceptConsentPopup;

    private List<ProductItem> productList;
    private LocalDateTime completedDate;
    private FastEasy fasteasy;
    private MutualFund mutualFund;
    private Securities securities;
    private Omnibus omnibus;
    private MutualFundAccount omnibusReferenceAccount;
    private DebitCardService debitCardService;
    private TravelCard travelCard;
    private boolean dividendTaxDeduct;
}

