package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.FaceCompareRequest;
import th.co.scb.onboardingapp.model.FaceCompareResponse;
import th.co.scb.onboardingapp.service.FaceCompareCommandHandler;


@RestController
public class FaceCompareController {

    @Autowired
    private FaceCompareCommandHandler faceCompareCommandHandler;

    @PostMapping("/api/face-compare/verification")
    public FaceCompareResponse faceVerification(@RequestBody FaceCompareRequest request, ObaAuthentication auth) {
        return faceCompareCommandHandler.faceVerification(request, auth);
    }
}

