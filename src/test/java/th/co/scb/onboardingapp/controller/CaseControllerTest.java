package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.CaseItem;
import th.co.scb.onboardingapp.service.CaseApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseControllerTest {

    @InjectMocks
    private CaseController caseController;

    @Mock
    private CaseApplicationService caseApplicationService;
    MockModel mockModel = new MockModel();

       @Test
    public void getApprovedListTest() {

        ObaAuthentication authentication =  mockModel.getObaAuthentication();
           List<CaseItem> res = new ArrayList<>();
           res.add(new CaseItem());

           when(caseApplicationService.getCaseApprovedList(any())).thenReturn(res);
           assertThat(caseController.getApprovedList(authentication)).isNotNull();
    }
//    @Test
//    public void createTest() {
//        CaseRequest caseRequest = getMockCaseRequest();
//        ObaAuthentication authentication =  mockModel.getObaAuthentication();
//        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
//        caseController.create(caseRequest, authentication, httpServletResponse);
//        verify(caseApplicationService, times(1)).createCase(any(), any(), any());
//    }

//    @Test
//    public void caseContinueTest() {
//        ObaAuthentication authentication =  mockModel.getObaAuthentication();
//        caseController.caseContinue(authentication);
//        verify(caseApplicationService, times(1)).continueCase(any());
//    }

 //   @Test
//    public void approveTest() {
//        ApproveRequest request = getMockApproveRequest();
//        ObaAuthentication authentication =  mockModel.getObaAuthentication();
//        caseController.approve(request, authentication);
//        verify(caseApplicationService, times(1)).approveCase(any(), any());
//    }

//    @Test
//    public void loadTest() {
//        String id = getMockString();
//        ObaAuthentication authentication = mockModel.getObaAuthentication();
//        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
//        caseController.load(id, authentication, httpServletResponse);
//        verify(caseApplicationService, times(1)).loadCase(any(), any(), any());
//    }

//    @Test
//    public void saveTest() {
//        CaseInfo caseInfo = getMockCaseInfo();
//        caseController.save(caseInfo);
//        verify(caseApplicationService, times(1)).saveCase(any());
//    }

//    @Test
//    public void closeTest() {
//        ObaAuthentication authentication = mockModel.getObaAuthentication();
//        caseController.close(authentication);
//        verify(caseApplicationService, times(1)).closeCase(any());
//    }

    @Test
    public void refreshTest() {
        ObaAuthentication authentication = mockModel.getObaAuthentication();
        caseController.refresh(authentication);
        verify(caseApplicationService, times(1)).refreshCase(any());
    }

//    @Test
//    public void startSubmissionTest() {
//        ObaAuthentication authentication = getMockIndiAuthentication();
//        caseController.startSubmission(authentication);
//        verify(caseApplicationService, times(1)).startCaseSubmission(any());
//    }

//    @Test
//    public void accountInfoTest() {
//        String accountNumber = getMockString();
//        ObaAuthentication authentication = getMockIndiAuthentication();
//        caseController.accountInfo(accountNumber, authentication);
//        verify(caseApplicationService, times(1)).getAccountInfo(any(), any());
//    }

//    @Test
//    public void submissionStatusTest() {
//        ObaAuthentication authentication = getMockIndiAuthentication();
//        caseController.submissionStatus(authentication);
//        verify(caseApplicationService, times(1)).checkSubmissionStatus(any());
//    }

    @Test
    public void validateTest() {
        ObaAuthentication authentication = mockModel.getObaAuthentication();
        caseController.validate(authentication);
        verify(caseApplicationService, times(1)).validateCase(any());
    }

    @Test
    public void stampfulfilmentTest() {
        ObaAuthentication authentication = mockModel.getObaAuthentication();
        caseController.stampfulfilment(authentication);
        verify(caseApplicationService, times(1)).caseStampCaseFulfilmentDate(any());
    }

   @org.junit.Test(expected= Throwable.class)
    public void stampfulfilmentThrowTest() {
     //   ObaAuthentication authentication = mockModel.getObaAuthentication();
        when(caseApplicationService.getCaseApprovedList(any())).thenThrow(new Throwable());

        //verify(caseApplicationService, times(1)).caseStampCaseFulfilmentDate(any());
    }


}