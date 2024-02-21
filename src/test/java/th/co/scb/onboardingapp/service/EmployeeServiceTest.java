package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.exception.InternalErrorException;
import th.co.scb.onboardingapp.model.AuthorizedLevel;
import th.co.scb.onboardingapp.model.entity.LoginBranchEntity;
import th.co.scb.onboardingapp.repository.*;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    BranchRepository branchRepository;

    @Mock
    ApprovalPermissionRepository approvalPermissionRepository;

    @Mock
    LoginBranchRepository loginBranchRepository;

    @Mock
    OrganizationRepository organizationRepository;

    @Mock
    LoginSessionRepository loginSessionRepository;

    MockModel mockModel = new MockModel();

    @Test
    public void shouldReturnEmployeeEntityWhenGetEmployee() {
        when(employeeRepository.findById(anyString())).thenReturn(Optional.of(mockModel.getemployeeEntity()));
        assertEquals(Optional.of(mockModel.getemployeeEntity()), employeeService.getEmployee("s99999"));
    }

    @Test
    public void shouldReturnLoginBranchInfoWhenGetLoginBranch() {
        when(loginBranchRepository.findByEmployeeIdAndBranchId(anyString(), anyString())).thenReturn(Optional.of(mockModel.loginBranchEntity()));
        assertEquals(Optional.of(mockModel.loginBranchInfo()), employeeService.getLoginBranch("s99999", "0"));
    }

    @Test
    public void shouldReturnOrganizationEntityListWhenGetBranches() {
        when(organizationRepository.findByEmployeeId(anyString())).thenReturn(mockModel.organizationEntityList());
        assertEquals(mockModel.organizationEntityList(), employeeService.getBranches("s99999"));
    }

    @Test
    public void shouldReturnAuthorizedLevelListWhenGetAuthorizedLevelsRequired() {
        assertEquals(mockModel.authorizedLevelList(), employeeService.getAuthorizedLevelsRequired(AuthorizedLevel.SSC));
    }

    @Test
    public void shouldReturnLoginSessionEntityListWhenGetLoginSession() {
        when(loginSessionRepository.findByEmployeeId(anyString(), anyString())).thenReturn(mockModel.loginSessionEntityList());
        assertEquals(mockModel.loginSessionEntityList(), employeeService.getLoginSession("s99999", "ONBD"));
    }

    @Test
    public void shouldFailWhenGetLoginBranch() {
        LoginBranchEntity loginBranchEntity = mockModel.loginBranchEntity();
        loginBranchEntity.setRoles("");
        when(loginBranchRepository.findByEmployeeIdAndBranchId(anyString(), anyString())).thenReturn(Optional.of(loginBranchEntity));
        assertThrows(InternalErrorException.class, () -> employeeService.getLoginBranch("s99999", "0"));
    }
}
