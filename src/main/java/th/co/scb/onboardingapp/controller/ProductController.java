package th.co.scb.onboardingapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.AccountTypeInfo;
import th.co.scb.onboardingapp.model.DebitCardInfo;
import th.co.scb.onboardingapp.service.ProductApplicationService;


import java.util.List;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    private ProductApplicationService productApplicationService;

    @GetMapping("/api/product/debitcards")
    public List<DebitCardInfo> debitcards(ObaAuthentication auth) {
        return productApplicationService.getDebitCards(auth);
    }
    @GetMapping("/api/product/accounts")
    public List<AccountTypeInfo> accounts() {
        return productApplicationService.getAccounts();
    }
}