package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.ProductInfo;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FCDCurrencyValidationService {

    public void fcdCurrencyValidation(CaseInfo caseInfo) {

        List<ProductInfo> productInfoList = caseInfo.getProductInfo();

        Map<String, Long> currencyToCount = productInfoList.stream()
                .map(ProductInfo::getCurrencyCode)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(productCode -> productCode, Collectors.counting()));

        checkCurrencyDuplicated(currencyToCount);
    }
    public void checkCurrencyDuplicated(Map<String, Long> currencyToCount) {
        boolean hasDuplicates = currencyToCount.values().stream().anyMatch(count -> count > 1);
        List<String> currencyDuplicated = new ArrayList<>();
        if (hasDuplicates){
            for (Map.Entry<String, Long> currency : currencyToCount.entrySet()) {
                if(currency.getValue() > 1){
                    currencyDuplicated.add(currency.getKey());
                }
            }
            log.info("Validate before submission: Currency Code Duplicated {}", String.join(", ", currencyDuplicated));
            throw new ValidationException("Invalid currency duplicated validation");
        }
    }
}