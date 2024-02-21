package th.co.scb.onboardingapp.model;

import lombok.Data;

@Data
public class FaceCompareRequest {
    private String deviceId;
    private String uploadSessionId;
    private String hackFacialScore;
    private String isForeigner;
}
