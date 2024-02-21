package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.FaceCompareRequest;
import th.co.scb.onboardingapp.model.FaceCompareResponse;


import static java.util.Objects.nonNull;

@Component
public class FaceCompareCommandHandler {

    @Autowired
    private FaceCompareService faceCompareService;

    @Autowired
    private TJLogProcessor tjLogProcessor;

    public FaceCompareResponse faceVerification(FaceCompareRequest data, ObaAuthentication auth) {
        if ("Y".equalsIgnoreCase(data.getIsForeigner())) {
            FaceCompareResponse faceVerificationResponse = new FaceCompareResponse();
            faceVerificationResponse.setResult(nonNull(data.getHackFacialScore()) ? data.getHackFacialScore() : "3");
            tjLogProcessor.logFaceCapturing(auth.getCaseId());
            return faceVerificationResponse;
        } else {
            return faceCompareService.getFaceVerificationResponse(data, auth);
        }
    }
}