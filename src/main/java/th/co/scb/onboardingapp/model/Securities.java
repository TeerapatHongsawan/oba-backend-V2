package th.co.scb.onboardingapp.model;

import lombok.Data;
import th.co.scb.entapi.securities.model.RiskCalcResponse;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Securities {
    private BigDecimal aum;
    private BigDecimal minLimit;
    private BigDecimal maxLimit;
    private String accountSegmentCode;
    private String accountSegmentDesc;
    private BigDecimal creditLimit;
    private boolean consentFlag;
    private boolean pepFlag;

    @NotNull
    private String atsAccountNumber;

    private String atsAccountType;

    @NotNull
    private String atsAccountBranchCode;

    private String atsAccountBranchName;
    private RiskCalcResponse riskScore;
}
