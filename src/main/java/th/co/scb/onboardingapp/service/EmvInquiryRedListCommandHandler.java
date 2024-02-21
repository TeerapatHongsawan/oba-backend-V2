package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.EmvInquiryRedListRequest;
import th.co.scb.onboardingapp.model.EmvInquiryRedListResponse;


@Component
public class EmvInquiryRedListCommandHandler {

    @Autowired
    private EmvInquiryRedListService emvInquiryRedListService;

    public EmvInquiryRedListResponse inquiryRedList(EmvInquiryRedListRequest request, ObaAuthentication auth) {
        return emvInquiryRedListService.emvInquiryRedList(request, auth);
    }
}