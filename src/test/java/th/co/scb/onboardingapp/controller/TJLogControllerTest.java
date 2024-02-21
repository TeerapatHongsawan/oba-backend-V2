package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.model.CaptureResponse;
import th.co.scb.onboardingapp.model.TJLogActivity;
import th.co.scb.onboardingapp.model.TJLogUIActivity;
import th.co.scb.onboardingapp.service.DocumentApplicationService;
import th.co.scb.onboardingapp.service.TJLogApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TJLogControllerTest {

    @InjectMocks
    private TJLogController tjLogController;

    @Mock
    private TJLogApplicationService tjLogApplicationService;

    MockModel mockModel = new MockModel();
    @Test
    public void testAddLog() {
        TJLogUIActivity tjLog = new TJLogUIActivity();
        tjLog.setTxnCode("TXN106");
        tjLogController.add(tjLog, mockModel.getObaAuthentication("0111","8A3D79F1DE9B48DA8A9EF8CD0FDFE2A5"));

        verify(tjLogApplicationService, times(1)).addTJLog(any(), any());
    }
}