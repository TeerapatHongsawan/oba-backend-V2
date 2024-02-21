package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.EmployeeInfo;
import th.co.scb.onboardingapp.model.EmployeeMe;
import th.co.scb.onboardingapp.service.EmployeeApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeApplicationService employeeApplicationService;
    MockModel mockModel = new MockModel();
    @Test
    public void testBrowse() {
        ObaAuthentication authentication =  mockModel.getObaAuthentication();
        List<EmployeeInfo> expect = new ArrayList<>();
        EmployeeInfo empInfo = new EmployeeInfo();
        empInfo.setEmployeeId("s99999");
        empInfo.setFirstNameEn("test");
        empInfo.setLastNameEn("test");
        empInfo.setFirstNameThai("เทส");
        empInfo.setLastNameThai("เทส");
        expect.add(empInfo);
        List<EmployeeInfo> response = new ArrayList<>();
        response.add(mockModel.getemployeeInfo());

        when(employeeApplicationService.getEmployeesInfo(any())).thenReturn(response);



        assertEquals(expect,employeeController.browse(authentication));
    }

    @Test
    public void testGetMe() {
        ObaAuthentication authentication =  mockModel.getObaAuthentication();
        when(employeeApplicationService.getEmployeeMe(any())).thenReturn(mockModel.getemployeeMe());

        employeeController.getMe(authentication);
        EmployeeMe expect = new EmployeeMe();
        expect.setEmployeeId("s99999");
        expect.setFirstNameEn("test");
        expect.setLastNameEn("test");
        expect.setFirstNameThai("เทส");
        expect.setLastNameThai("เทส");
        expect.setOcCode("0");
        expect.setOcName("test");
        expect.setOcNameEn("s99999");
        expect.setSamOcCode("0");
        expect.setBranchId("0");
        expect.setBranchNameThai("เทส");
        expect.setBranchNameEn("s99999");
        expect.setApprovalBranchId("PRVT/");
        expect.setRoles(mockModel.getRole());
        assertEquals(expect,employeeController.getMe(authentication));
    }
//
//    @Test
//    public void testgetLicense() {
//        employeeController.getLicense("AA", "BB");
//
//        verify(employeeApplicationService, times(1)).getEmployeeLicense(any(), any());
//    }
//
//    @Test
//    public void testValidate() {
//        employeeController.validate(null);
//
//        verify(employeeApplicationService, times(1)).validateEmployee(any());
//    }


}