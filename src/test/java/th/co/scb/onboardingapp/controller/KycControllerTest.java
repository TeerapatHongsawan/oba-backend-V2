package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualRequest;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualResponse;
import th.co.scb.onboardingapp.service.KycApplicationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KycControllerTest {

    @InjectMocks
    private KycController kycController;

    @Mock
    private KycApplicationService kycApplicationService;

    @Test
    public void getCustomerPreKycTest() {
        PreCalculateKYCIndividualRequest data = new PreCalculateKYCIndividualRequest();
        data.setIsicCode("");
        data.setNationalityCode("TH");
        data.setCountrySourceOfIncome("TH");
        data.setOccupationCode("01");
        PreCalculateKYCIndividualResponse repsone = new PreCalculateKYCIndividualResponse();
        repsone.setKycScore("100");
        when(kycApplicationService.getPreCalculateKYCIndividualResponse(any())).thenReturn(repsone);
         assertEquals("100",kycController.getCustomerPreKyc(data).getKycScore());
        verify(kycApplicationService, times(1)).getPreCalculateKYCIndividualResponse(any());
    }


}