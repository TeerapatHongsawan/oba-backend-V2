package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.entapi.deposits.model.GenDepAcctRequest;
import th.co.scb.entapi.deposits.model.GenDepAcctResponse;
import th.co.scb.onboardingapp.service.AccountApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountApplicationService accountApplicationService;

    MockModel mockModel = new MockModel();
    @Test
    public void generateMatchTest() {
        GenDepAcctRequest request = mockModel.getAccountRequest();
        GenDepAcctResponse response = new GenDepAcctResponse();
        response.setAccountNumber("4160059618");
        when(accountApplicationService.generateAccount(any())).thenReturn(response);
        assertEquals(accountController.generate(request),response);
    }
    @Test
    public void generateNotMatchTest() {
        GenDepAcctRequest request = mockModel.getAccountRequest();
        GenDepAcctResponse expect = new GenDepAcctResponse();
        expect.setAccountNumber("5420059576");
        GenDepAcctResponse response = new GenDepAcctResponse();
        response.setAccountNumber("4160059618");
        when(accountApplicationService.generateAccount(any())).thenReturn(response);
        assertNotEquals(accountController.generate(request),expect);
    }



}