package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.model.MasterJointType;
import th.co.scb.onboardingapp.service.CustommerJointInfoService;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustommerJointInfoControllerTest {

    @InjectMocks
    private CustommerJointInfoController custommerJointInfoController;

    @Mock
    private CustommerJointInfoService custommerJointInfoService;

    MockModel mockModel = new MockModel();

    @Test
    public void masterJoinTypeTest() {
        when(custommerJointInfoService.JointInfo()).thenReturn(mockModel.getMasterJointType());
        Assertions.assertEquals(mockModel.getMasterJointType(), custommerJointInfoController.jointType());

    }
}
