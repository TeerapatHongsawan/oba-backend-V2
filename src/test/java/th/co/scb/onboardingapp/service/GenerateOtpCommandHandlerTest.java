package th.co.scb.onboardingapp.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.entapi.otp.TxnAuthOTPApi;
import th.co.scb.entapi.otp.model.GenerationResponseDataModel;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.CustomerContactChannel;
import th.co.scb.onboardingapp.model.CustomerInfo;
import th.co.scb.onboardingapp.model.GenerateOtpRequest;
import th.co.scb.onboardingapp.model.GenerateOtpResponse;
import th.co.scb.onboardingapp.model.entity.OtpEntity;
import th.co.scb.onboardingapp.repository.OtpJpaRepository;
import th.co.scb.onboardingapp.utility.MockModel;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenerateOtpCommandHandlerTest {

    @InjectMocks
    private GenerateOtpCommandHandler generateOtpCommandHandler;

    @Mock
    private CaseLibraryService caseLibraryService;


    @Mock
    private OtpApplicationService otpApplicationService;
    @Mock
    private OtpJpaRepository otpRepository;

    @Mock
    private TJLogProcessor tjLogProcessor;
    @Mock
    private OtpApiService otpApiService;;
    @Mock
    private MappingHelper mappingHelper;
    MockModel mockModel = new MockModel();
    @Test
    public void testGetGenerateOtpResponse() {
        GenerateOtpRequest otpRequest = mockModel.getOTPRequest();
        otpRequest.setOtpMobile("0864035009");
        CustomerInfo customerInfo = mockModel.getCustomerInfo();
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setDuraiton("300");
        otpEntity.setToken("J4enklp-Mz");
        otpEntity.setCaseId(UUID.randomUUID().toString());
        otpEntity.setMobileNo("0933333333");
        otpEntity.setReferenceNo("TEST");
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2010, 07, 04, 15, 45, 15));
        otpEntity.setCreatedDate(timestamp);

        when(caseLibraryService.getCustomerInfo(any())).thenReturn(ofNullable(customerInfo));
        when(otpRepository.findById(any())).thenReturn(ofNullable(otpEntity));
        when(otpApiService.generateOTP(any())).thenReturn(mockModel.getOTPApiResponse());
        when(mappingHelper.map(any(), any())).thenReturn(mockModel.getOTPResponse());

       // when(otpApplicationService.getGenerateOtpResponse(any(),any())).thenReturn(mockModel.getOTPResponse());
        GenerateOtpResponse generateOtpResponse = generateOtpCommandHandler.getGenerateOtpResponse(otpRequest, mockModel.getObaAuthentication());

        assertThat(generateOtpResponse).isNotNull();
    }


}