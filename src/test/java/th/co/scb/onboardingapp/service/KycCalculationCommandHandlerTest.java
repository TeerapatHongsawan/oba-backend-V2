package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.entapi.indikyc.CustProfileIndiKYCApi;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualRequest;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KycCalculationCommandHandlerTest {

    @InjectMocks
    private KycCalculationCommandHandler kycCalculationCommandHandler;

    @Mock
    private CustProfileIndiKYCApi custProfileIndiKYCApi;

//    @Test
//    public void testGetPreCalculateKYCIndividualResponse() {
//        PreCalculateKYCIndividualRequest preCalculateKYCIndividualRequest = new PreCalculateKYCIndividualRequest();
//        preCalculateKYCIndividualRequest.setOccupationCode("01");
//        preCalculateKYCIndividualRequest.setNationalityCode("TH");
//        preCalculateKYCIndividualRequest.setCountrySourceOfIncome("TH");
//        PreCalculateKYCIndividualResponse mockPreCalculateKYCIndividualResponse = getPreCalculateKYCIndividualResponse();
//
//        when(custProfileIndiKYCApi.preCalculateKYCScore(any())).thenReturn(mockPreCalculateKYCIndividualResponse);
//
//        PreCalculateKYCIndividualResponse preCalculateKYCIndividualResponse = kycCalculationCommandHandler.getPreCalculateKYCIndividualResponse(preCalculateKYCIndividualRequest);
//
//        assertThat(preCalculateKYCIndividualResponse.getKycScore()).isNotNull();
//    }

    private PreCalculateKYCIndividualResponse getPreCalculateKYCIndividualResponse() {
        PreCalculateKYCIndividualResponse preCalculateKYCIndividualResponse = new PreCalculateKYCIndividualResponse();
        preCalculateKYCIndividualResponse.setKycScore("311");

        return preCalculateKYCIndividualResponse;
    }
}