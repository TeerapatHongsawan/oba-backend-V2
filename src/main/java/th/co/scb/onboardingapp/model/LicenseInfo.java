package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class LicenseInfo {
    private String licenseNumber;
    private String employeeId;
    private String firstNameEn;
    private String lastNameEn;
    private String firstNameThai;
    private String lastNameThai;
    private String licenseType;
    private String licenseOwner;
}
