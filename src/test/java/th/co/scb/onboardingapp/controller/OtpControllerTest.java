package th.co.scb.onboardingapp.controller;//package th.co.scb.indi.apiproxy.controller.api;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.GenerateOtpRequest;
import th.co.scb.onboardingapp.model.ValidateOtpRequest;
import th.co.scb.onboardingapp.service.OtpApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OtpControllerTest {

    @InjectMocks
    private OtpController otpController;

    @Mock
    private OtpApplicationService otpApplicationService;
    MockModel mockModel = new MockModel();
    @Test
    public void generateTest() {

        ObaAuthentication auth = mockModel.getObaAuthentication();

        when(otpApplicationService.getGenerateOtpResponse(any(),any())).thenReturn(mockModel.getOTPResponse());
        assertThat(otpController.generate(mockModel.getOTPRequest(),auth)).isNotNull();
        verify(otpApplicationService, times(1)).getGenerateOtpResponse(any(), any());
    }

//    @Test
//    public void validateTest() {
//        ValidateOtpRequest data = new ValidateOtpRequest();
//        ObaAuthentication auth = mockModel.getObaAuthentication();
//        otpController.validate(data, auth);
//        verify(otpApplicationService, times(1)).validateOtp(any(), any());
//    }

}