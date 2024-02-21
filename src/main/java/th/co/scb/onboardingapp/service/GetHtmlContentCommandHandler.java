package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class GetHtmlContentCommandHandler {

    @Autowired
    ConsentService consentService;

    public String getHtmlContent(String url) throws IOException {
        return consentService.gethtml(url);
    }
}
