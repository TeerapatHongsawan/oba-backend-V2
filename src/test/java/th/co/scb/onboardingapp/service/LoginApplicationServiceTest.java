package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import th.co.scb.onboardingapp.model.DataItem;
import th.co.scb.onboardingapp.utility.MockModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginApplicationServiceTest {
    @InjectMocks
    private LoginApplicationService loginApplicationService;

    @Mock
    private UserLoginCommandHandler userLoginCommandHandler;

    MockModel mockModel = new MockModel();

    @Test
    public void shouldReturnDataItemListWhenLogin() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        List<DataItem> expect = new ArrayList<>();
        DataItem dataItem = new DataItem();
        dataItem.setText("เทส");
        dataItem.setCode("0");
        expect.add(dataItem);
        when(userLoginCommandHandler.login(any(), any(), any())).thenReturn(expect);
        assertEquals(expect, loginApplicationService.login(mockModel.loginRequest(), response, request));
    }
}
