package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.model.ConsentContentInfo;
import th.co.scb.onboardingapp.model.ConsentContentRequest;
import th.co.scb.onboardingapp.service.ConsentApplicationService;
import java.io.IOException;
import java.util.List;

@RestController
public class ConsentController {
    @Autowired
    ConsentApplicationService consentApplicationService;

    @PostMapping("/api/consent/initiateConsentContent")
    public List<ConsentContentInfo> initiateConsentContent(@RequestBody List<ConsentContentRequest> consentContentRequests) {
        return consentApplicationService.initConsentContent(consentContentRequests);
    }

    @PostMapping("/api/consent/gethtml")
    public String getHtml(@RequestBody String url) throws IOException {
        return consentApplicationService.getHtmlContent(url);
    }
}
