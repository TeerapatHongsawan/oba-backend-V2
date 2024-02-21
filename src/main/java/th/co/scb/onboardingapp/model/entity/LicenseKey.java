package th.co.scb.onboardingapp.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LicenseKey implements Serializable {
    private String licenseType;
    private String licenseNumber;
}
