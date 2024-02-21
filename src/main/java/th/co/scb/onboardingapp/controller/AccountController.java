package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.scb.entapi.deposits.model.ATMDebitCardInqRs;
import th.co.scb.entapi.deposits.model.GenDepAcctRequest;
import th.co.scb.entapi.deposits.model.GenDepAcctResponse;
import th.co.scb.onboardingapp.service.AccountApplicationService;


import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountApplicationService accountApplicationService;

    @PostMapping("/api/account")
    public GenDepAcctResponse generate(@RequestBody GenDepAcctRequest request) {
        return accountApplicationService.generateAccount(request);
    }

}

