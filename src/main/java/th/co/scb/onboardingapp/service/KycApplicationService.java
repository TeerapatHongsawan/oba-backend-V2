package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualRequest;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualResponse;


@Slf4j
@Service
public class KycApplicationService {

    @Autowired
    private KycCalculationCommandHandler kycCalculationCommandHandler;

    public PreCalculateKYCIndividualResponse getPreCalculateKYCIndividualResponse(PreCalculateKYCIndividualRequest data) {
      return kycCalculationCommandHandler.getPreCalculateKYCIndividualResponse(data);
    }
}
