package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.repository.GeneralParameterRepository;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeneralParamServiceTest {

    @InjectMocks
    private GeneralParamService generalParamService;

    @Mock
    private GeneralParameterRepository generalParameterRepository;

    MockModel mockModel = new MockModel();

    @Test
    public void shouldReturnGeneralParameterGetFaceNotAllowBranchWhenGetFaceNotAllowBranch() {
        when(generalParameterRepository.findById(anyString())).thenReturn(Optional.of(mockModel.generalParameterEntityGetFaceNotAllowBranch()));
        assertEquals(mockModel.generalParameterGetFaceNotAllowBranch(), generalParamService.getFaceNotAllowBranch());
    }
    @Test
    public void shouldReturnGeneralParameterGetFaceNotAllowRoleWhenGetFaceNotAllowRole() {
        when(generalParameterRepository.findById(anyString())).thenReturn(Optional.of(mockModel.generalParameterEntityGetFaceNotAllowRole()));
        assertEquals(mockModel.generalParameterGetFaceNotAllowRole(), generalParamService.getFaceNotAllowRole());
    }
    @Test
    public void shouldReturnGeneralParameterGetAllowLoginBranchGeneralParamWhenGetAllowLoginBranchGeneralParam() {
        when(generalParameterRepository.findById(anyString())).thenReturn(Optional.of(mockModel.generalParameterEntityGetAllowLoginBranchGeneralParam()));
        assertEquals(mockModel.generalParameterGetAllowLoginBranchGeneralParam(), generalParamService.getAllowLoginBranchGeneralParam());
    }
    @Test
    public void shouldReturnGeneralParameterGetVerifyMultiLoginWhenGetVerifyMultiLogin() {
        when(generalParameterRepository.findById(anyString())).thenReturn(Optional.of(mockModel.generalParameterEntityGetVerifyMultiLogin()));
        assertEquals(mockModel.generalParameterGetVerifyMultiLogin(), generalParamService.getVerifyMultiLogin());
    }

}
