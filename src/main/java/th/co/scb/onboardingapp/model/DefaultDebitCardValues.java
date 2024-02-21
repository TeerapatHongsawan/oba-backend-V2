package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DefaultDebitCardValues {

    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("feeCode")
    private String feeCode;
}
