package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.entapi.sales_services.model.PromptPayLookupResponse;
import th.co.scb.entapi.sales_services.model.PromptPayLookupResponseV2;
import th.co.scb.onboardingapp.model.PromptPayProxy;
import th.co.scb.onboardingapp.service.CustomerServiceApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;
;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceControllerTest {

    @InjectMocks
    private CustomerServiceController customerServiceController;

    @Mock
    private CustomerServiceApplicationService customerServiceApplicationService;
    MockModel mockModel = new MockModel();
    @Test
    public void prompayLookupTest() {
        when(customerServiceApplicationService.lookupPromptPay(any())).thenReturn(mockModel.getPromptPayResponse());
        assertThat(customerServiceController.prompayLookup(mockModel.getPromptPayProxy())).isNotNull();
    }




}