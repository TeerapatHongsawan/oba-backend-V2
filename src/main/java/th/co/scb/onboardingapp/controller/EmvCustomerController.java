package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.EmvInquiryRedListRequest;
import th.co.scb.onboardingapp.model.EmvInquiryRedListResponse;
import th.co.scb.onboardingapp.model.PayloadResponseErrorT;
import th.co.scb.onboardingapp.model.PayloadResponseT;
import th.co.scb.onboardingapp.service.EmvInquiryRedListCommandHandler;

import java.util.Collections;
import java.util.List;


@RestController
public class EmvCustomerController {

    @Autowired
    private EmvInquiryRedListCommandHandler emvInquiryRedListCommandHandler;


    @PostMapping("/api/emv/customer/inquiry-red-list")
    public EmvInquiryRedListResponse inquiryRedList(@RequestBody EmvInquiryRedListRequest request, ObaAuthentication auth) {
        return emvInquiryRedListCommandHandler.inquiryRedList(request, auth);
    }

    @PostMapping("/v2/api/emv/customer/inquiry-red-list")
    public ResponseEntity<?> inquiryRedListV2(@RequestBody EmvInquiryRedListRequest request, ObaAuthentication auth) {
        try {
            EmvInquiryRedListResponse responseData = emvInquiryRedListCommandHandler.inquiryRedList(request, auth);
            List<EmvInquiryRedListResponse> dataList = Collections.singletonList(responseData);
            return PayloadResponseT.buildResponse(dataList);
        } catch (HttpClientErrorException e) {
            return PayloadResponseErrorT.buildResponseError(e.getResponseBodyAsString());
        }

    }


}

