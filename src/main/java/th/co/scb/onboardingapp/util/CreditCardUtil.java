package th.co.scb.onboardingapp.util;

import org.springframework.util.CollectionUtils;
import th.co.scb.onboardingapp.model.DebitCardFeeInfo;
import th.co.scb.onboardingapp.model.DebitCardInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Optional.of;

public class CreditCardUtil {

    private CreditCardUtil() {
    }

    public static void filterFeeCodesByRoles(List<DebitCardInfo> debitCardInfos, Set<String> roles) {
        for (DebitCardInfo debitCardInfo : debitCardInfos) {
            debitCardInfo.setFeeCodes(getAllowedFeeCodesForRoles(debitCardInfo.getFeeCodes(), roles));
        }
    }

    private static List<DebitCardFeeInfo> getAllowedFeeCodesForRoles(List<DebitCardFeeInfo> debitCardFeeInfos, Set<String> roles) {
        List<DebitCardFeeInfo> filtered = new ArrayList<>();

        if (!CollectionUtils.isEmpty(debitCardFeeInfos)) {
            for (DebitCardFeeInfo debitCardFeeInfo : debitCardFeeInfos) {
                List<String> userType = of(debitCardFeeInfo).map(DebitCardFeeInfo::getUserType).orElseGet(Collections::emptyList);

                if (userType.stream().anyMatch(roles::contains)) {
                    filtered.add(debitCardFeeInfo);
                }
            }
        }
        return filtered;
    }

}
