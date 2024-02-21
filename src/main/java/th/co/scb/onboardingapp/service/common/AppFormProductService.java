package th.co.scb.onboardingapp.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CustomerInfo;
import th.co.scb.onboardingapp.model.common.AppFormProductType;

@Service
public class AppFormProductService {

    @Autowired
    private AppFormProductHelper appFormProductHelper;

    public AppFormProductType getAppFormProductType(CaseInfo caseInfo) {

        if (caseInfo == null) {
            return AppFormProductType.NONE;
        }

        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        if (customerInfo == null) {
            return AppFormProductType.NONE;
        }

        boolean hasDeposit = appFormProductHelper.hasDepositProduct(caseInfo);
        boolean hasMutualFund = appFormProductHelper.hasMutualFundProduct(caseInfo);
        boolean hasSecurities = appFormProductHelper.hasSecuritiesProduct(caseInfo);
        boolean hasService = appFormProductHelper.hasService(caseInfo);
        boolean hasOmnibus = appFormProductHelper.hasOmnibusProduct(caseInfo);
        boolean hasChaiyo = appFormProductHelper.hasChaiyoProduct(caseInfo);
        boolean isChaiyoServiceOnly = appFormProductHelper.isChaiyoServiceOnly(caseInfo);

        if (hasDeposit && !hasMutualFund && !hasSecurities && !hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT;
        }

        if (hasDeposit && !hasMutualFund && !hasSecurities && hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_SERVICES;
        }

        if (!hasDeposit && hasMutualFund && !hasSecurities && !hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.MUTUALFUND;
        }

        if (!hasDeposit && !hasMutualFund && hasSecurities && !hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.SECURITIES;
        }

        if (!hasDeposit && !hasMutualFund && !hasSecurities && hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.SERVICE_ONLY;
        }

        if (!hasDeposit && hasMutualFund && hasSecurities && !hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.MUTUALFUND_SECURITIES;
        }

        if (hasDeposit && hasMutualFund && !hasSecurities && !hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_MUTUALFUND;
        }

        if (hasDeposit && hasMutualFund && !hasSecurities && hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_MUTUALFUND_SERVICES;
        }

        if (!hasDeposit && !hasMutualFund && hasSecurities && hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.SECURITIES_SERVICES;
        }

        if (hasDeposit && !hasMutualFund && hasSecurities && !hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_SECURITIES;
        }

        if (hasDeposit && !hasMutualFund && hasSecurities && hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_SECURITIES_SERVICES;
        }

        if (!hasDeposit && hasMutualFund && !hasSecurities && hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.MUTUALFUND_SERVICES;
        }

        if (!hasDeposit && hasMutualFund && hasSecurities && hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.MUTUALFUND_SECURITIES_SERVICES;
        }

        if (hasDeposit && hasMutualFund && hasSecurities && !hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_MUTUALFUND_SECURITIES;
        }

        if (hasDeposit && hasMutualFund && hasSecurities && hasService && !hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_MUTUALFUND_SECURITIES_SERVICES;
        }
        if (!hasDeposit && !hasMutualFund && !hasSecurities && !hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.OMNIBUS;
        }
        if (hasDeposit && !hasMutualFund && !hasSecurities && !hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_OMNIBUS;
        }
        if (!hasDeposit && hasMutualFund && !hasSecurities && !hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.MUTUALFUND_OMNIBUS;
        }
        if (!hasDeposit && !hasMutualFund && hasSecurities && !hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.OMNIBUS_SECURITIES;
        }
        if (hasDeposit && hasMutualFund && !hasSecurities && !hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_MUTUALFUND_OMNIBUS;
        }
        if (hasDeposit && !hasMutualFund && hasSecurities && !hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_OMNIBUS_SECURITIES;
        }
        if (!hasDeposit && hasMutualFund && hasSecurities && !hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.MUTUALFUND_OMNIBUS_SECURITIES;
        }
        if (hasDeposit && hasMutualFund && hasSecurities && !hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_MUTUALFUND_OMNIBUS_SECURITIES;
        }
        if (!hasDeposit && !hasMutualFund && hasSecurities && hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.OMNIBUS_SECURITIES_SERVICES;
        }
        if (!hasDeposit && hasMutualFund && !hasSecurities && hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.MUTUALFUND_OMNIBUS_SERVICES;
        }
        if (!hasDeposit && hasMutualFund && hasSecurities && hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.MUTUALFUND_OMNIBUS_SECURITIES_SERVICES;
        }
        if (hasDeposit && !hasMutualFund && !hasSecurities && hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_OMNIBUS_SERVICES;
        }
        if (hasDeposit && hasMutualFund && !hasSecurities && hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_MUTUALFUND_OMNIBUS_SERVICES;
        }
        if (hasDeposit && hasMutualFund && hasSecurities && hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_MUTUALFUND_OMNIBUS_SECURITIES_SERVICES;
        }
        if (!hasDeposit && !hasMutualFund && !hasSecurities && hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.OMNIBUS_SERVICES;
        }
        if (hasDeposit && !hasMutualFund && hasSecurities && hasService && hasOmnibus && !hasChaiyo) {
            return AppFormProductType.DEPOSIT_OMNIBUS_SECURITIES_SERVICES;
        }
        // chaiyo normal flow
        if (hasDeposit && !hasMutualFund && !hasSecurities && hasService && !hasOmnibus && hasChaiyo) {
            return AppFormProductType.DEPOSIT_CHAIYO;
        }
        // chaiyo truck flow
        if (hasDeposit && !hasMutualFund && !hasSecurities && !hasService && !hasOmnibus && hasChaiyo) {
            return AppFormProductType.DEPOSIT_CHAIYO;
        }
        // chaiyo new card
        if (!hasDeposit && !hasMutualFund && !hasSecurities && hasService && !hasOmnibus && isChaiyoServiceOnly) {
            return AppFormProductType.SERVICE_ONLY;
        }
        return AppFormProductType.NONE;
    }
}
