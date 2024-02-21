package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.ConsentContentInfo;
import th.co.scb.onboardingapp.model.ConsentContentRequest;
import java.io.IOException;
import java.util.List;

@Service
public class ConsentApplicationService {

    @Autowired
    InitConsentContentCommandHandler initConsentContentCommandHandler;

    @Autowired
    GetHtmlContentCommandHandler getHtmlContentCommandHandler;

    public List<ConsentContentInfo> initConsentContent(List<ConsentContentRequest> consentContentRequests) {
        return initConsentContentCommandHandler.initConsentContent(consentContentRequests);
    }

    public String getHtmlContent(String url) throws IOException {
        return getHtmlContentCommandHandler.getHtmlContent(url);
    }


}
