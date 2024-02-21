package th.co.scb.onboardingapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import th.co.scb.entapi.sales_services.model.PromptPayLookupResponseV2;
import th.co.scb.onboardingapp.model.EmvInquiryRedListResponse;
import th.co.scb.onboardingapp.model.PayloadResponseErrorT;
import th.co.scb.onboardingapp.model.PayloadResponseT;
import th.co.scb.onboardingapp.model.PromptPayProxy;
import th.co.scb.onboardingapp.service.CustomerServiceApplicationService;


import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class CustomerServiceController {

    @Autowired
    private CustomerServiceApplicationService customerServiceApplicationService;

    @PostMapping("/api/customer-service/promptpay-lookup")
    public PromptPayLookupResponseV2 prompayLookup(@RequestBody PromptPayProxy proxies) {
        return customerServiceApplicationService.lookupPromptPay(proxies);
    }

    @PostMapping("/v2/api/customer-service/promptpay-lookup")
    public ResponseEntity<?> prompayLookupV2(@RequestBody PromptPayProxy proxies) {

        try {
            PromptPayLookupResponseV2 responseData = customerServiceApplicationService.lookupPromptPay(proxies);
            List<PromptPayLookupResponseV2> dataList = Collections.singletonList(responseData);
            return PayloadResponseT.buildResponse(dataList);
        } catch (HttpClientErrorException e) {
            return PayloadResponseErrorT.buildResponseError(e.getResponseBodyAsString());
        }
    }
//    @PostMapping("/api/customer-service/promptpay-lookup-by-customer")
//    public List<PromptPayProxyItem> prompayLookupByCustomer(@RequestBody PromptPayLookupByCustomerRequest request) {
//        return customerServiceApplicationService.lookupPromptPayByCustomer(request.getCid(), request.getDocType());
//    }
}


