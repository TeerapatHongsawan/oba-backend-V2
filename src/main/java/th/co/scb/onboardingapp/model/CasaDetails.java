package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class CasaDetails {
    private String caseId;
    private String applicationNo;
    private String transactionId;
    private String citizenId;
    private String productId;
    private String casaAccNo;
    private String casaAccName;
    private String bankCode;

    // use for deepLink back
    private String appId;

    // open account flow only
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String revolvingAccountNo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String chaiyoCardNo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String chaiyoCardRef;
}
