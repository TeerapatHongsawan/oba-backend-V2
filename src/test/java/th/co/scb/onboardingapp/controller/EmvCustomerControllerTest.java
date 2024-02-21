package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.model.EmvInquiryRedListRequest;
import th.co.scb.onboardingapp.service.EmvInquiryRedListCommandHandler;
import th.co.scb.onboardingapp.utility.MockModel;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmvCustomerControllerTest {

    @InjectMocks
    private EmvCustomerController emvCustomerController;

    @Mock
    private EmvInquiryRedListCommandHandler emvInquiryRedListCommandHandler;



    MockModel mockModel = new MockModel();
    @Test
    public void testInquiryRedList() {
        EmvInquiryRedListRequest request = new EmvInquiryRedListRequest();

        emvCustomerController.inquiryRedList(request, mockModel.getObaAuthentication("0111","8A3D79F1DE9B48DA8A9EF8CD0FDFE2A5"));

        verify(emvInquiryRedListCommandHandler, times(1)).inquiryRedList(any(), any());
    }

}