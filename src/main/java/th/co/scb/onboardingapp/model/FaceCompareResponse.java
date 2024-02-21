package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class FaceCompareResponse {
    private String result;
    private String matchLevel;
    private String matchScore;
}
