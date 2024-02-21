package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IdentificationsItem {

    /**
     * fatca, consent, vip, monitoring, kyc, watchlist, cdd, face, consentManagement
     */
    @JsonProperty("idenType")
    private String idenType;

    @JsonProperty("idenStatus")
    private String idenStatus;

    @JsonProperty("idenDetail")
    private IdenDetail idenDetail;
}