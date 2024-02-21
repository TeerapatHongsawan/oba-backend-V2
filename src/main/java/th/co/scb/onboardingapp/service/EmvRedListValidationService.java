package th.co.scb.onboardingapp.service;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.individuals.model.ContactChannel;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.RedListEntity;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class EmvRedListValidationService {
    private static final String CONTACT_STATUS_ADD = "A";
    private static final String CONTACT_TYPE_EMAIL = "005";

    @Autowired
    private RedListService redListService;

    public void emvRedListValidation(CaseInfo caseInfo) {
        List<String> productEmails = extractProductEmail(caseInfo);
        List<String> addedEmails = extractContactChannel(caseInfo);

        if (!matchContactChannel(caseInfo.getCustomerInfo().getContactChannels(), productEmails)
                || !matchRedList(caseInfo.getCaseId(), addedEmails)) {
            throw new ValidationException("Invalid an email validation");
        }

        redListService.deleteRedList(caseInfo.getCaseId());
    }

    private boolean matchContactChannel(List<CustomerContactChannel> contactChannels, List<String> productEmails) {
        for (String productEmail : productEmails) {
            if (!containsEmail(contactChannels, productEmail)) {
                log.info("Validate before submission: Not pass {}", productEmail);
                return false;
            }
        }
        return true;
    }

    private boolean matchRedList(String caseId, List<String> addedEmails) {
        for (String addedEmail : addedEmails) {
            List<RedListEntity> redList = redListService.getRedList(caseId);
            if (nonNull(redList) && notPassedRedList(redList, addedEmail)) {
                log.info("Validate before submission: Not pass {}", addedEmail);
                return false;
            }
        }

        return true;
    }

    private List<String> extractContactChannel(CaseInfo caseInfo) {
        return caseInfo.getCustomerInfo().getContactChannels().stream()
                .filter(it -> CONTACT_STATUS_ADD.equalsIgnoreCase(it.getTxnType())
                        && CONTACT_TYPE_EMAIL.equals(it.getContactTypeCode()))
                .map(ContactChannel::getContactNumber)
                .collect(Collectors.toList());
    }

    private List<String> extractProductEmail(CaseInfo caseInfo) {
        List<String> productEmails = new ArrayList<>();
        CustomerInfo customerInfo = caseInfo.getCustomerInfo();

        Investment investment = customerInfo.getInvestment();
        if (nonNull(investment) && !Strings.isNullOrEmpty(investment.getEmail())) {
            productEmails.add(investment.getEmail());
        }

        FastEasy fastEasy = customerInfo.getFasteasy();
        if (nonNull(fastEasy) && !Strings.isNullOrEmpty(fastEasy.getEmail())) {
            productEmails.add(fastEasy.getEmail());
        }

        DebitCardService debitCardService = customerInfo.getDebitCardService();
        if (nonNull(debitCardService) && !Strings.isNullOrEmpty(debitCardService.getEmail())) {
            productEmails.add(debitCardService.getEmail());
        }

        TravelCard travelCard = customerInfo.getTravelCard();
        if (nonNull(travelCard) && !Strings.isNullOrEmpty(travelCard.getEmail())) {
            productEmails.add(travelCard.getEmail());
        }

        List<ProductInfo> productInfoList = caseInfo.getProductInfo();
        for (ProductInfo productInfo : productInfoList) {
            if (!Strings.isNullOrEmpty(productInfo.getEmail())) {
                productEmails.add(productInfo.getEmail());
            }
        }

        return productEmails;
    }

    private boolean notPassedRedList(List<RedListEntity> redList, String email) {
        return redList.stream().anyMatch(i -> i.getEmail().equalsIgnoreCase(email) && "N".equalsIgnoreCase(i.getRedListFlag()));
    }

    private boolean containsEmail(List<CustomerContactChannel> contactChannels, String email) {
        return contactChannels.stream().anyMatch(it -> email.equalsIgnoreCase(it.getContactNumber()));
    }
}
