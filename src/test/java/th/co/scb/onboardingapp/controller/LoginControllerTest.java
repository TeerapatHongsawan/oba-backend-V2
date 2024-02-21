package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import th.co.scb.onboardingapp.model.DataItem;
import th.co.scb.onboardingapp.service.LoginApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginApplicationService loginApplicationService;

    MockModel mockModel = new MockModel();

    @Test
    public void shouldReturnExpectWhenLogin() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        List<DataItem> expect = new ArrayList<>();
        DataItem dataItem = new DataItem();
        dataItem.setText("เทส");
        dataItem.setCode("0");
        expect.add(dataItem);
        when(loginApplicationService.login(any(), any(), any())).thenReturn(expect);
        assertEquals(expect, loginController.login(mockModel.loginRequest(),response, request));
    }

    @Test
    public void shouldReturnTrueWhenLogout() {
        when(loginApplicationService.logout(any(), any(), any(), any())).thenReturn(true);
        assertTrue(loginController.logout("s99999", "web", "ONBD", mockModel.getObaAuthentication()));
    }

    @Test
    public void shouldReturnFalseWhenLogout() {
        when(loginApplicationService.logout(any(), any(), any(), any())).thenReturn(false);
        assertFalse(loginController.logout("s99999", "web", "ONBD", mockModel.getObaAuthentication()));
    }
}
