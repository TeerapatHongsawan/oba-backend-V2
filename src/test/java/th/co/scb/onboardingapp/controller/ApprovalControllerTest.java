package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.ApprovalDto;
import th.co.scb.onboardingapp.service.ApprovalApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApprovalControllerTest {

    @InjectMocks
    private ApprovalController approvalController;

    @Mock
    private ApprovalApplicationService approvalApplicationService;;

    MockModel mockModel = new MockModel();
    @Test
    public void getRemoteApprovalTest() {
        ObaAuthentication authentication =  mockModel.getObaAuthentication();
        List<ApprovalDto> response = new ArrayList<>();
        response.add(new ApprovalDto());
        when(approvalApplicationService.getRemoteApproval(any())).thenReturn(response);
        assertThat(approvalController.getRemoteApprovals(authentication)).isNotNull();
    }
}