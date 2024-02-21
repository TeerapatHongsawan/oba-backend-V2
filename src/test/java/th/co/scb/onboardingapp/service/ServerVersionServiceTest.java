package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.exception.BadRequestException;
import th.co.scb.onboardingapp.exception.UnauthorizedException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ServerVersionServiceTest {

    @InjectMocks
    private ServerVersionService serverVersionService;



    @Test
    public void shouldSuccessWhenCheckVersion() throws NoSuchFieldException, IllegalAccessException {
        Field field = ServerVersionService.class.getDeclaredField("version");
        field.setAccessible(true);
        field.set(serverVersionService, "---SERVER-VERSION---");
        serverVersionService.checkVersion("---SERVER-VERSION---");
    }

    @Test
    public void shouldFailWhenCheckVersion() throws NoSuchFieldException, IllegalAccessException {
        Field field = ServerVersionService.class.getDeclaredField("version");
        field.setAccessible(true);
        field.set(serverVersionService, "---SERVER-VERSION---");
        assertThrows(BadRequestException.class, () -> serverVersionService.checkVersion("test"));
    }
}
