package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.model.AccountTypeInfo;

import java.util.List;

@Component
public class GetAccountCommandHandler {

    @Autowired
    private ProductService productService;

    public List<AccountTypeInfo> getAccounts() {
        /**
         * Filtered out inactive account
         */
        return productService.getProducts();
    }
}
