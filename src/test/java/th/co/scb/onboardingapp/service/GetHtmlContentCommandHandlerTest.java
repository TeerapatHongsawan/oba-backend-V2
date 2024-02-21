package th.co.scb.onboardingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetHtmlContentCommandHandlerTest {

    @InjectMocks
    private GetHtmlContentCommandHandler getHtmlContentCommandHandler;

    @Mock
    private ConsentService consentService;

    @Test
    public void testGetHtmlContent() throws IOException {
        String url = "www.google.com";

        when(consentService.gethtml(any())).thenReturn("www.google.com");

        String getHtml = getHtmlContentCommandHandler.getHtmlContent(url);

        assertEquals(url, getHtml);

    }
}