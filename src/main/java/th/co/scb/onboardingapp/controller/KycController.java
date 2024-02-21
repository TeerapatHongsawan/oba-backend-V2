package th.co.scb.onboardingapp.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualRequest;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualResponse;
import th.co.scb.onboardingapp.service.KycApplicationService;


@Slf4j
@RestController
public class KycController {

    @Autowired
    private KycApplicationService kycApplicationService;

    @PostMapping("/api/prekyc/retrieve")
    public PreCalculateKYCIndividualResponse getCustomerPreKyc(@RequestBody PreCalculateKYCIndividualRequest data) {
        return kycApplicationService.getPreCalculateKYCIndividualResponse(data);
    }
}

