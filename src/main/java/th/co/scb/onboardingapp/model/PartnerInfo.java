package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class PartnerInfo {
    private String key;
    private String partnerDisplay;
    private boolean upcomingPartner;
    private String partnerValue;
    private String originalPartnerValue;
    private boolean changed;
}
