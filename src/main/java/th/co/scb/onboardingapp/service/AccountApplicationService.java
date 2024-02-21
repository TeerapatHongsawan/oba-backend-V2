package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.deposits.model.ATMDebitCardInqRs;
import th.co.scb.entapi.deposits.model.GenDepAcctRequest;
import th.co.scb.entapi.deposits.model.GenDepAcctResponse;
import th.co.scb.entapi.referencedata_databases_reference_bank_code.model.Item_reference_BANK_CODE;

import java.util.List;

@Service
public class AccountApplicationService {

    @Autowired
    private AccountGenerateCommandHandler accountGenerateCommandHandler;



    public GenDepAcctResponse generateAccount(GenDepAcctRequest genDepAcctRequest) {
        return accountGenerateCommandHandler.generateAccount(genDepAcctRequest);
    }

}
