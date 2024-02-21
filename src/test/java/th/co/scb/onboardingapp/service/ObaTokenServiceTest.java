package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.TokenInfo;
import th.co.scb.onboardingapp.utility.MockModel;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ObaTokenServiceTest {

    @InjectMocks
    private ObaTokenService obaTokenService;

//    @Mock
//    private String signingKey;
//
//    @Mock
//    private Long expirationSeconds;


    MockModel mockModel = new MockModel();


    @Test
    public void shouldNotNullWhenCreateToken() throws NoSuchFieldException, IllegalAccessException {
        Field field = ObaTokenService.class.getDeclaredField("signingKey");
        field.setAccessible(true);
        field.set(obaTokenService, "gv625szx8UnAtcsXVup3Q2YaUFOPZdQyDPoIXSoy0kESXKrR+6hFJVUWONcAhXWC");
        field = ObaTokenService.class.getDeclaredField("expirationSeconds");
        field.setAccessible(true);
        field.set(obaTokenService, 1200l);

        ObaAuthentication authentication = mockModel.getObaAuthentication();
        TokenInfo token = obaTokenService.create(authentication);
        assertNotNull(token);
    }
}
