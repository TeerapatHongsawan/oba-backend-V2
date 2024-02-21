package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.mutualfunds.model.BaaResponse;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Omnibus extends EcInfo {
    private String clientNumber;
    private RiskScore riskScore;
    private BaaResponse baaResponse;

    private String accountBranchName;
    private String accountType;
    private String accountNumber;
    private String accountName;

    private boolean dividendTaxDeduct;
    private String email;
    private boolean mailingToEmail;
    @NotNull
    private DataItem investObjective;
    private DataItem investExperience;
    @NotNull
    private List<DataItem> sourceOfFund;
    @NotNull
    private DataItem workPositionCode;
    private String investObjectiveOther;
    private String investExperienceOther;
    private String sourceOfFundOther;
    private String workPositionOther;
    private String investAdvisorName;
    private boolean requireInvestAdvisorFlag;
}
