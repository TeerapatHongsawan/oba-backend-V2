package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import th.co.scb.entapi.individuals.model.Address;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress extends Address {
    private String addrLang;
    private String internalTypeCode;
    private String isPrimary;
    private boolean sameAsIDCard;
    private boolean officeSameAsCurrent;
    private String referenceFlag;
    private String countryDisplay;
}
