package th.co.scb.onboardingapp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaptureResponse {
    private String uploadSessionId;
    private String ticket;
}
