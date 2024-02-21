package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.List;

@Data
public class RiskScore {
    private String riskScore;
    private String riskLevelCode;
    private String riskLevelDescriptionTh;
    private String riskLevelDescriptionEn;
    private List<AssetPlan> assetPlan;
}
