package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryRelation {

    private String valueCode;
    private String valueThaiDesc;
    private String valueEnglishDesc;
    private String cbsValueCode;
}
