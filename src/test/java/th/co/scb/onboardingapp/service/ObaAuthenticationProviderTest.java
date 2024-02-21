package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.exception.*;
import th.co.scb.onboardingapp.model.DataLoginSession;
import th.co.scb.onboardingapp.model.LoginBranchInfo;
import th.co.scb.onboardingapp.model.entity.*;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ObaAuthenticationProviderTest {

    @InjectMocks
    private ObaAuthenticationProvider obaAuthenticationProvider;

    @Mock
    private LdapValidator ldapValidator;

    @Mock
    private EmployeeService employeeService;

    MockModel mockModel = new MockModel();

    @Test
    public void shouldReturnEmployeeBranchWhenGetEmployeeBranch() {
        doNothing().when(ldapValidator).validate(anyString(), anyString());
        when(employeeService.getBranches(anyString())).thenReturn(mockModel.getBranch());

        assertEquals(mockModel.getEmployeeBranch(), obaAuthenticationProvider.getEmployeeBranch("s99999", "scb1234!"));
    }

    @Test
    public void shouldReturnDataLoginSessionListWhenGetLoginSession() {
        when(employeeService.getLoginSession(anyString(), anyString())).thenReturn(mockModel.getMultiLoginList());
        List<DataLoginSession> expect = new ArrayList<>();
        DataLoginSession dataLoginSession = new DataLoginSession();
        dataLoginSession.setEmployeeId("s99999");
        dataLoginSession.setLastActivityTime(mockModel.getLastActivityTime("2024-01-05 11:24:12"));
        dataLoginSession.setDeviceId("web");
        dataLoginSession.setStatus("active");
        dataLoginSession.setAppName("ONBD");
        dataLoginSession.setToken("192.168.1.103");
        expect.add(dataLoginSession);
        assertEquals(expect, obaAuthenticationProvider.getLoginSession("s99999", "ONBD"));
    }

    @Test
    public void shouldFailWhenUseThrowNotExistingEmployeeMethodAndEmployeeIsNull() {
        EmployeeEntity employeeEntity = null;
        assertThrows(UnauthorizedException.class, () -> obaAuthenticationProvider.authenticate("s99999", "0", "ONBD", null));
    }

    @Test
    public void shouldFailWhenUseEjectNonBranchUserLoginToIProMethodAndAppNameIsIPROAndDoNotHaveBranchRoleNamebranch() {
        LoginBranchInfo loginBranchInfo = mockModel.loginBranchInfo();
        Set<String> role = new HashSet<>();
        loginBranchInfo.setRoles(role);
        when(employeeService.getEmployee(anyString())).thenReturn(Optional.of(mockModel.getemployeeEntity()));
        when(employeeService.getLoginBranch(anyString(), anyString())).thenReturn(Optional.of(loginBranchInfo));
        assertThrows(UnauthorizedException.class, () -> obaAuthenticationProvider.authenticate("s99999", "0", "IPRO", null));
    }

    @Test
    public void shouldFailWhenUseEjectEmptyRoleUserMethodAndResolvedRolesIsEmpty() {
        LoginBranchInfo loginBranchInfo = mockModel.loginBranchInfo();
        Set<String> role = new HashSet<>();
        loginBranchInfo.setRoles(role);
        when(employeeService.getEmployee(anyString())).thenReturn(Optional.of(mockModel.getemployeeEntity()));
        when(employeeService.getLoginBranch(anyString(), anyString())).thenReturn(Optional.of(loginBranchInfo));
        assertThrows(UnauthorizedException.class, () -> obaAuthenticationProvider.authenticate("s99999", "0", "ONBD", null));
    }

    @Test
    public void shouldFailWhenUsegetLoginBranchInfoMethodAndloginBranchInfoIsNull() {
        LoginBranchInfo loginBranchInfo = mockModel.loginBranchInfo();
        Set<String> role = new HashSet<>();
        loginBranchInfo.setRoles(role);
        when(employeeService.getEmployee(anyString())).thenReturn(Optional.of(mockModel.getemployeeEntity()));
        when(employeeService.getLoginBranch(anyString(), anyString())).thenReturn(Optional.ofNullable(null));
        assertThrows(UnauthorizedException.class, () -> obaAuthenticationProvider.authenticate("s99999", "0", "ONBD", null));
    }

//    @Test
//    public void shouldReturnWhenAuthenticate() {
//        when(employeeService.getEmployee(anyString())).thenReturn(Optional.of(mockModel.getemployeeEntity()));
//        when(employeeService.getLoginBranch(anyString(), anyString())).thenReturn(Optional.of(mockModel.loginBranchInfo()));
//
//        Set<String> resolvedLoginRoles = new HashSet<>();
//        resolvedLoginRoles.add("payment_cash");
//        resolvedLoginRoles.add("payment_atm");
//        resolvedLoginRoles.add("payment_account_deduct");
//        resolvedLoginRoles.add("investment");
//        resolvedLoginRoles.add("branch");
//        resolvedLoginRoles.add("travel_card");
//        resolvedLoginRoles.add("facial");
//        ObaAuthentication result = new ObaAuthentication("s99999", resolvedLoginRoles, "0",null, "ONBD");
//        ObaAuthentication actual = obaAuthenticationProvider.authenticate("s99999", "0", "ONBD");
//        assertEquals(result, obaAuthenticationProvider.authenticate("s99999", "0", "ONBD"));
//    }
}
