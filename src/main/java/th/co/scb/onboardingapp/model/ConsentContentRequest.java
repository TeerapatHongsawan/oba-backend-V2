package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class ConsentContentRequest {
    private String vendor;
    private String consentType;
    private String consentVersion;
    private String consentLink;
    private String referenceId;
}
