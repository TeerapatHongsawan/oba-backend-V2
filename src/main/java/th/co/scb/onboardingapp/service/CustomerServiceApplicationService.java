package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.sales_services.model.PromptPayLookupResponseV2;
import th.co.scb.onboardingapp.model.PromptPayProxy;


import java.util.List;

@Service
public class CustomerServiceApplicationService {

//    @Autowired
//    private CustomerPromptPayLookupCommandHandler customerPromptPayLookupCommandHandler;

    @Autowired
    private PromptPayLookupCommandHandler promptPayLookupCommandHandler;

//    public List<PromptPayProxyItem> lookupPromptPayByCustomer(String cid, String docType) {
//        return customerPromptPayLookupCommandHandler.lookupPromptPayByCustomer(cid, docType);
//    }

    public PromptPayLookupResponseV2 lookupPromptPay(PromptPayProxy proxies) {
        return promptPayLookupCommandHandler.lookupPromptPay(proxies);
    }
}
