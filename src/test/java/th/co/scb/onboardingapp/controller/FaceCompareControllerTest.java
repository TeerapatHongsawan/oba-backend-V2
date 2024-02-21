package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.model.CaptureResponse;
import th.co.scb.onboardingapp.model.FaceCompareRequest;
import th.co.scb.onboardingapp.model.FaceCompareResponse;
import th.co.scb.onboardingapp.service.DocumentApplicationService;
import th.co.scb.onboardingapp.service.FaceCompareCommandHandler;
import th.co.scb.onboardingapp.service.FaceCompareService;
import th.co.scb.onboardingapp.utility.MockModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FaceCompareControllerTest {

    @InjectMocks
    private FaceCompareController faceCompareController;

    @Mock
    private FaceCompareCommandHandler faceCompareCommandHandler;

    MockModel mockModel = new MockModel();
    @Test
    public void testFaceVerificationMatch() {
        FaceCompareRequest request = new FaceCompareRequest();
        request.setDeviceId("web");
        request.setHackFacialScore("2");
        request.setUploadSessionId("OBIN183020220601010003");
        FaceCompareResponse expect = new FaceCompareResponse();
        expect.setMatchLevel("3");
        expect.setMatchScore("4");
        when(faceCompareCommandHandler.faceVerification(any(),any())).thenReturn(mockModel.getFaceCompareResponse());
        assertEquals(expect,faceCompareController.faceVerification(request, mockModel.getObaAuthentication("0111","8A3D79F1DE9B48DA8A9EF8CD0FDFE2A5")));
        verify(faceCompareCommandHandler, times(1)).faceVerification(any(), any());
    }
    @Test
    public void testFaceVerificationNotMatch() {
        FaceCompareRequest request = new FaceCompareRequest();
        request.setDeviceId("web");
        request.setHackFacialScore("2");
        request.setUploadSessionId("OBIN183020220601010003");
        FaceCompareResponse expect = new FaceCompareResponse();
        expect.setMatchLevel("1");
        expect.setMatchScore("5");
        when(faceCompareCommandHandler.faceVerification(any(),any())).thenReturn(mockModel.getFaceCompareResponse());
        assertNotEquals(expect,faceCompareController.faceVerification(request, mockModel.getObaAuthentication("0111","8A3D79F1DE9B48DA8A9EF8CD0FDFE2A5")));
        verify(faceCompareCommandHandler, times(1)).faceVerification(any(), any());
    }
}