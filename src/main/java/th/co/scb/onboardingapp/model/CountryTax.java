package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class CountryTax {
    private String countryCode;
    private String foreignTIN;
    private String reason;
    private String remark;
}
