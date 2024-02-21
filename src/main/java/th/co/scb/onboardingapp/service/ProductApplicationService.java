package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.AccountTypeInfo;
import th.co.scb.onboardingapp.model.DebitCardInfo;

import java.util.List;

@Service
public class ProductApplicationService {

    @Autowired
    private GetAccountCommandHandler getAccountCommandHandler;

    @Autowired
    private GetDebitCardCommandHandler getDebitCardCommandHandler;
    public List<AccountTypeInfo> getAccounts() {
        return getAccountCommandHandler.getAccounts();
    }
    public List<DebitCardInfo> getDebitCards(ObaAuthentication authentication) {
        return getDebitCardCommandHandler.getDebitCards(authentication);
    }
}
