package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class ConfigResponse {
    private String serverVersion;
    private String hostAddress;
    private String hostName;
    private String[] activeProfile;
}
