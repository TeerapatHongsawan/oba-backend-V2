package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import th.co.scb.onboardingapp.exception.BadRequestException;
import th.co.scb.onboardingapp.exception.UnauthorizedException;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.repository.LoginSessionRepository;
import th.co.scb.onboardingapp.service.api.SessionApiService;
import th.co.scb.onboardingapp.utility.MockModel;

import java.time.LocalDateTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserLoginCommandHandlerTest {
    @InjectMocks
    private  UserLoginCommandHandler userLoginCommandHandler;

    @Mock
    private ServerVersionService serverVersionService;
    @Mock
    private GeneralParamService generalParamService;
    @Mock
    private ObaAuthenticationProvider obaAuthenticationProvider;
    @Mock
    private LoginSessionRepository loginSessionRepository;

    @Mock
    private Environment environment;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private SecurityLogger securityLogger;

    @Mock
    private SessionApiService sessionApiService;

    @Mock
    private ObaTokenService tokenService;

    MockModel mockModel = new MockModel();

    @Test
    public void shouldReturnDataItemListWhenLogin() {
        TokenInfo tokenInfo = new TokenInfo("test","test",1234213213L);
        doNothing().when(serverVersionService).checkVersion(anyString());
        doNothing().when(loginSessionRepository).updateDataLoginSession(anyString(), anyString(), anyString(), anyString());
        when(generalParamService.getVerifyMultiLogin()).thenReturn(mockModel.generalParameterGetVerifyMultiLogin());
        when(obaAuthenticationProvider.getEmployeeBranch(anyString(), anyString())).thenReturn(mockModel.getEmployeeBranch());
        when(obaAuthenticationProvider.getLoginSession(anyString(), anyString())).thenReturn(mockModel.getDataLoginSessionList());
        when(obaAuthenticationProvider.authenticate(anyString(), anyString(), anyString(), any())).thenReturn(mockModel.getObaAuthentication());
        when(tokenService.create(any())).thenReturn(tokenInfo);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        List<DataItem> expect = new ArrayList<>();
        DataItem dataItem = new DataItem();
        dataItem.setText("เทส");
        dataItem.setCode("0");
        expect.add(dataItem);
        assertEquals(expect, userLoginCommandHandler.login(mockModel.loginRequest(), response, request));
    }

    @Test
    public void shouldReturnDataItemListWhenLoginDiffTimeLower200AndSameDeviceIdAndSameAppName() {
        TokenInfo tokenInfo = new TokenInfo("test","test",1234213213L);
        when(tokenService.create(any())).thenReturn(tokenInfo);
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime mockLdt = ldt.minusSeconds(10);
        List<DataLoginSession> verifyMultiLogin = mockModel.getDataLoginSessionList();
        verifyMultiLogin.get(0).setLastActivityTime(mockLdt);
        doNothing().when(serverVersionService).checkVersion(anyString());
        when(generalParamService.getVerifyMultiLogin()).thenReturn(mockModel.generalParameterGetVerifyMultiLogin());
        when(obaAuthenticationProvider.getEmployeeBranch(anyString(), anyString())).thenReturn(mockModel.getEmployeeBranch());
        when(obaAuthenticationProvider.getLoginSession(anyString(), anyString())).thenReturn(verifyMultiLogin);
        when(obaAuthenticationProvider.authenticate(anyString(), anyString(), anyString(), any())).thenReturn(mockModel.getObaAuthentication());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        List<DataItem> expect = new ArrayList<>();
        DataItem dataItem = new DataItem();
        dataItem.setText("เทส");
        dataItem.setCode("0");
        expect.add(dataItem);
        assertEquals(expect, userLoginCommandHandler.login(mockModel.loginRequest(), response, request));
    }

    @Test
    public void shouldReturnDataItemListWhenLoginDiffTimeLower200AndSameDeviceIdAndDiffAppName() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime mockLdt = ldt.minusSeconds(10);
        List<DataLoginSession> verifyMultiLogin = mockModel.getDataLoginSessionList();
        verifyMultiLogin.get(0).setLastActivityTime(mockLdt);
        verifyMultiLogin.get(0).setAppName("IOB");
        doNothing().when(serverVersionService).checkVersion(anyString());
        when(generalParamService.getVerifyMultiLogin()).thenReturn(mockModel.generalParameterGetVerifyMultiLogin());
        when(obaAuthenticationProvider.getEmployeeBranch(anyString(), anyString())).thenReturn(mockModel.getEmployeeBranch());
        when(obaAuthenticationProvider.getLoginSession(anyString(), anyString())).thenReturn(verifyMultiLogin);
        when(obaAuthenticationProvider.authenticate(anyString(), anyString(), anyString(), any())).thenReturn(mockModel.getObaAuthentication());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        List<DataItem> expect = new ArrayList<>();
        DataItem dataItem = new DataItem();
        dataItem.setText("เทส");
        dataItem.setCode("0");
        expect.add(dataItem);
        assertEquals(expect, userLoginCommandHandler.login(mockModel.loginRequest(), response, request));
    }

    @Test
    public void shouldFailWhenLoginDiffTimeLower200AndDiffDeviceIdAndSameAppName() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime mockLdt = ldt.minusSeconds(10);
        List<DataLoginSession> verifyMultiLogin = mockModel.getDataLoginSessionList();
        verifyMultiLogin.get(0).setLastActivityTime(mockLdt);
        verifyMultiLogin.get(0).setDeviceId("android");
        doNothing().when(serverVersionService).checkVersion(anyString());
        when(generalParamService.getVerifyMultiLogin()).thenReturn(mockModel.generalParameterGetVerifyMultiLogin());
        when(obaAuthenticationProvider.getEmployeeBranch(anyString(), anyString())).thenReturn(mockModel.getEmployeeBranch());
        when(obaAuthenticationProvider.getLoginSession(anyString(), anyString())).thenReturn(verifyMultiLogin);
        when(obaAuthenticationProvider.authenticate(anyString(), anyString(), anyString(), any())).thenReturn(mockModel.getObaAuthentication());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        assertThrows(UnauthorizedException.class, () -> userLoginCommandHandler.login(mockModel.loginRequest(), response, request));
    }

    @Test
    public void shouldReturnDataItemListWhenLoginDiffTimeMoreThan200AndDiffDeviceIdAndIsNotConfirmLoginDuplicateUser() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime mockLdt = ldt.minusSeconds(300);
        List<DataLoginSession> verifyMultiLogin = mockModel.getDataLoginSessionList();
        verifyMultiLogin.get(0).setLastActivityTime(mockLdt);
        verifyMultiLogin.get(0).setDeviceId("android");
        doNothing().when(serverVersionService).checkVersion(anyString());
        when(generalParamService.getVerifyMultiLogin()).thenReturn(mockModel.generalParameterGetVerifyMultiLogin());
        when(obaAuthenticationProvider.getEmployeeBranch(anyString(), anyString())).thenReturn(mockModel.getEmployeeBranch());
        when(obaAuthenticationProvider.getLoginSession(anyString(), anyString())).thenReturn(verifyMultiLogin);
        when(obaAuthenticationProvider.authenticate(anyString(), anyString(), anyString(), any())).thenReturn(mockModel.getObaAuthentication());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        List<DataItem> expect = new ArrayList<>();
        DataItem dataItem = new DataItem();
        dataItem.setText("CONFIRM_TO_LOGIN");
        dataItem.setCode("000000");
        expect.add(dataItem);
        LoginRequest loginRequest = mockModel.loginRequest();
        loginRequest.setIsConfirmLoginDuplicateUser(false);
        assertEquals(expect, userLoginCommandHandler.login(loginRequest, response, request));
    }

    @Test
    public void shouldReturnDataItemListWhenLoginDiffTimeMoreThan200AndSameDeviceIdAndDiffAppname() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTime mockLdt = ldt.minusSeconds(300);
        List<DataLoginSession> verifyMultiLogin = mockModel.getDataLoginSessionList();
        verifyMultiLogin.get(0).setLastActivityTime(mockLdt);
        verifyMultiLogin.get(0).setAppName("IOB");
        doNothing().when(serverVersionService).checkVersion(anyString());
        doNothing().when(loginSessionRepository).insertDataLoginSession(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        when(generalParamService.getVerifyMultiLogin()).thenReturn(mockModel.generalParameterGetVerifyMultiLogin());
        when(obaAuthenticationProvider.getEmployeeBranch(anyString(), anyString())).thenReturn(mockModel.getEmployeeBranch());
        when(obaAuthenticationProvider.getLoginSession(anyString(), anyString())).thenReturn(verifyMultiLogin);
        when(obaAuthenticationProvider.authenticate(anyString(), anyString(), anyString(), any())).thenReturn(mockModel.getObaAuthentication());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        List<DataItem> expect = new ArrayList<>();
        DataItem dataItem = new DataItem();
        dataItem.setText("เทส");
        dataItem.setCode("0");
        expect.add(dataItem);
        assertEquals(expect, userLoginCommandHandler.login(mockModel.loginRequest(), response, request));
    }

    @Test
    public void shouldReturnDataItemListWhenLoginVerifyMultiLoginIsEmptyAndIsNotVerifyMultiLogin() {
        TokenInfo tokenInfo = new TokenInfo("test","test",1234213213L);
        when(tokenService.create(any())).thenReturn(tokenInfo);

        List<DataLoginSession> emptyDataLoginSession = new ArrayList<>();
        GeneralParam isVerifyMultiLogin = mockModel.generalParameterGetVerifyMultiLogin();
        Map<String, Object> value = new HashMap<>();
        value.put("isVerify", true);
        isVerifyMultiLogin.setValue(value);
        doNothing().when(serverVersionService).checkVersion(anyString());
        when(generalParamService.getVerifyMultiLogin()).thenReturn(isVerifyMultiLogin);
        when(obaAuthenticationProvider.getEmployeeBranch(anyString(), anyString())).thenReturn(mockModel.getEmployeeBranch());
        when(obaAuthenticationProvider.getLoginSession(anyString(), anyString())).thenReturn(emptyDataLoginSession);
        when(obaAuthenticationProvider.authenticate(anyString(), anyString(), anyString(), any())).thenReturn(mockModel.getObaAuthentication());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        List<DataItem> expect = new ArrayList<>();
        DataItem dataItem = new DataItem();
        dataItem.setText("เทส");
        dataItem.setCode("0");
        expect.add(dataItem);
        assertEquals(expect, userLoginCommandHandler.login(mockModel.loginRequest(), response, request));
    }
}
