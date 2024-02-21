package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class W8FormDetails {
    private String customerNameThai;
    private String customerName;
    private String countryOfCitizenship;
    private String permanentResidenceAddress;
    private String cityOrTown;
    private String country;
    private String mailAddress;
    private String mailCityOrTown;
    private String mailCountry;
    private String foreignTax;
    private String referenceNo;
    private String birthDate;
    private W8FormDetailsClaimTax claimTax;
    private String socialSecurityNumber;
    private String taxPayer;
}
