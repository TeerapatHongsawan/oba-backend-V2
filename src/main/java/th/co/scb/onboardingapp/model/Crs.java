package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class Crs {
    private String crsStatus;
    private String cityOfBirth;
    private String countryOfBirth;
    private List<CountryTax> countryTaxs;
    private String langToggle;
}
