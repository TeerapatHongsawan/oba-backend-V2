package th.co.scb.onboardingapp.model.deposite;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CDDResult {
    private String cddResult;
    private String cddResultFlag;
}