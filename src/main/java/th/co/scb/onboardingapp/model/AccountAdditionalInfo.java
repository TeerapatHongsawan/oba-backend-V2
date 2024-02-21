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
public class AccountAdditionalInfo {

    @JsonProperty("package")
    private String scbpackage;

    @JsonProperty("defaultDebitCardValues")
    private DefaultDebitCardValues defaultDebitCardValues;
    
}
