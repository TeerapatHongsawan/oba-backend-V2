package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class IndustryInfo {
    private String key;
    private String industryDisplay;
    private String industryValue;
    private String originalIndustryValue;
    private List<PartnerInfo> partnerInfo;
}
