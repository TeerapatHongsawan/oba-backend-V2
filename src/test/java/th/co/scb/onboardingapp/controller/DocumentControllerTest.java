package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.AccountTypeInfo;
import th.co.scb.onboardingapp.model.CaptureResponse;
import th.co.scb.onboardingapp.model.UploadRequest;
import th.co.scb.onboardingapp.service.DocumentApplicationService;
import th.co.scb.onboardingapp.service.ProductApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentControllerTest {

    @InjectMocks
    private DocumentController documentController;

    @Mock
    private DocumentApplicationService documentApplicationService;

    MockModel mockModel = new MockModel();
    @Test
    public void testUpload() {
        documentController.upload(new UploadRequest(), mockModel.getObaAuthentication());

        verify(documentApplicationService, times(1)).uploadDocument(any(), any());
    }


}