package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.repository.LoginSessionRepository;
import th.co.scb.onboardingapp.utility.MockModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserLogoutCommandHandlerTest {

    @InjectMocks
    private UserLogoutCommandHandler userLogoutCommandHandler;

    @Mock
    private LoginSessionRepository loginSessionRepository;

    @Mock
    private ObaAuthenticationProvider authenticationProvider;

    MockModel mockModel = new MockModel();
    @Test
    public void shouldReturnTrueWhenLogout() {
        when(loginSessionRepository.deleteDataLoginSession(anyString(), anyString(), anyString())).thenReturn(true);
        assertTrue(userLogoutCommandHandler.logout("s99999", "web", "ONBD", mockModel.getObaAuthentication()));
    }

    @Test
    public void shouldReturnFalseWhenLogout() {
        when(loginSessionRepository.deleteDataLoginSession(anyString(), anyString(), anyString())).thenReturn(false);
        assertFalse(userLogoutCommandHandler.logout("s99999", "web", "ONBD", mockModel.getObaAuthentication()));
    }

}
