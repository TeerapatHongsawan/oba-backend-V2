package th.co.scb.onboardingapp.service.common;

import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.*;


import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Service
public class AppFormProductHelper {

    public boolean hasDepositProduct(CaseInfo caseInfo) {
        List<ProductInfo> productsInfo = caseInfo.getProductInfo();
        return isNotEmpty(productsInfo);
    }

    public boolean hasMutualFundProduct(CaseInfo caseInfo) {

        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        if (customerInfo == null) {
            return false;
        }

        Investment investment = customerInfo.getInvestment();
        if (investment == null) {
            return false;
        }

        MutualFund mutualFund = investment.getMutualFund();
        return mutualFund != null;
    }

    public boolean hasOmnibusProduct(CaseInfo caseInfo) {

        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        if (customerInfo == null) {
            return false;
        }

        Investment investment = customerInfo.getInvestment();
        if (investment == null) {
            return false;
        }

        Omnibus omnibus = investment.getOmnibus();
        return omnibus != null;
    }

    public boolean hasSecuritiesProduct(CaseInfo caseInfo) {
        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        if (customerInfo == null) {
            return false;
        }

        Investment investment = customerInfo.getInvestment();
        if (investment == null) {
            return false;
        }

        Securities securities = investment.getSecurities();
        return securities != null;
    }

    public boolean isServiceOnly(CaseInfo caseInfo) {

        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        if (customerInfo == null) {
            return false;
        }

        return isEmpty(caseInfo.getProductInfo()) && customerInfo.getInvestment() == null;
    }

    public boolean hasChaiyoProduct(CaseInfo caseInfo) {
        return caseInfo.getAdditionalInfo().getChaiyoLoanDetail() != null
                && ((!caseInfo.getAdditionalInfo().getChaiyoLoanDetail().getLoanType().equalsIgnoreCase("reissue")) || (!caseInfo.getAdditionalInfo().getChaiyoLoanDetail().getLoanType().equalsIgnoreCase("newcard")));
    }

    public boolean isChaiyoServiceOnly(CaseInfo caseInfo) {
        return caseInfo.getAdditionalInfo() != null
                && caseInfo.getAdditionalInfo().getChaiyoLoanDetail() != null
                && ("reissue".equalsIgnoreCase(caseInfo.getAdditionalInfo().getChaiyoLoanDetail().getLoanType())
                || "newcard".equalsIgnoreCase(caseInfo.getAdditionalInfo().getChaiyoLoanDetail().getLoanType()));
    }

    public boolean hasService(CaseInfo caseInfo) {
        return caseInfo.getCustomerInfo().getFasteasy() != null
                || caseInfo.getCustomerInfo().getDebitCardService() != null
                || caseInfo.getCustomerInfo().getTravelCard() != null;
    }
}
