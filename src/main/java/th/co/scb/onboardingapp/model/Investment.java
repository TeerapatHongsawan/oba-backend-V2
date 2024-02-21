package th.co.scb.onboardingapp.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Investment {
    private MutualFund mutualFund;

    @Valid
    private Securities securities;

    private Suitability suitability;

    private RelativePerson spouse;
    private boolean showSpouse;

    private Omnibus omnibus;

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
    private String email;
    private boolean mailingToEmail;

    private boolean requireInvestAdvisorFlag;
    private boolean dividendTaxDeduct;
    private boolean baaConnectionSuccess;
    private OEF oef;

    /**
     * Existing mutual fund accounts
     */
    private List<MutualFundAccount> mutualFundAccounts;
    private LicenseInfo license;
}

