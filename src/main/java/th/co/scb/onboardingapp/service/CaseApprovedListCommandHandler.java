package th.co.scb.onboardingapp.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CaseItem;
import th.co.scb.onboardingapp.model.CompletedListSearchEvent;


import java.util.List;

@Component
public class CaseApprovedListCommandHandler {
    @Autowired
    private CaseService caseService;

    @Autowired
    private CaseApprovedService caseApprovedService;

    @Autowired
    private ApplicationEventPublisher publisher;

    public List<CaseItem> getCaseApprovedList(ObaAuthentication authentication) {
        List<CaseItem> caseItems = caseApprovedService.getApprovedCaseList(authentication.getName(), authentication.getBranchId());
        logApprovedListSearchEvent(caseItems);
        return caseItems;
    }


    private void logApprovedListSearchEvent(List<CaseItem> caseItems) {
        for (CaseItem caseItem : caseItems) {
            // Replication of Front-end logic for filtering ECed cases
            // The list in PII log table will be less than or equal to the list return from this route
            if (CollectionUtils.isNotEmpty(caseItem.getProductList()) ||
                    caseItem.getMutualFund() != null || caseItem.getFasteasy() != null ||
                    caseItem.getSecurities() != null || caseItem.getDebitCardService() != null ||
                    caseItem.getOmnibus() != null || caseItem.getTravelCard() != null) {
                CaseInfo caseInfo = caseService.getCase(caseItem.getCaseId());
                publisher.publishEvent(new CompletedListSearchEvent("SEARCH", "Maker Application List", caseInfo));
            }
        }
    }
}
