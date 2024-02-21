package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.DebitCardInfo;
import th.co.scb.onboardingapp.util.CreditCardUtil;


import java.util.List;

@Component
public class GetDebitCardCommandHandler {

    @Autowired
    private ProductService productService;

    public List<DebitCardInfo> getDebitCards(ObaAuthentication authentication) {
        /**
         * Filtered out inactive debit cards & inactive fee codes
         */
        List<DebitCardInfo> debitCardInfos = productService.getDebitCards();

        /**
         *  additionally filtered fee codes by user roles
         */
        CreditCardUtil.filterFeeCodesByRoles(debitCardInfos, authentication.getRoles());

        return debitCardInfos;
    }
}
