package th.co.scb.onboardingapp.service;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.model.ConsentContentInfo;
import th.co.scb.onboardingapp.model.ConsentContentRequest;
import th.co.scb.onboardingapp.model.entity.ConsentContentEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InitConsentContentCommandHandler {

    @Autowired
    ConsentService consentService;

    public List<ConsentContentInfo> initConsentContent(List<ConsentContentRequest> consentContentRequests) {
        List<ConsentContentInfo> consents = new ArrayList<>();

        for (ConsentContentRequest consentContentRequest : consentContentRequests) {
            if (ConsentContentEntity.CROSS_SELL_CONSENT_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType()) || ConsentContentEntity.MARKETING_CONSENT_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType())) {

                ConsentContentInfo consent = consentService.getConsentCrossSellAndMarketing(consentContentRequest.getConsentType(), consentContentRequest.getConsentVersion());
                consents.add(consent);

            } else if (ConsentContentEntity.PRIVACY_NOTICE_SHORT_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType()) || ConsentContentEntity.PRIVACY_NOTICE_FULL_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType())) {

                ConsentContentInfo consentNotice = consentService.getConsentNotice(consentContentRequest.getConsentType());
                consents.add(consentNotice);

            } else if (ConsentContentEntity.CROSS_SELL_PARTNER_CONSENT_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType())) {

                ConsentContentInfo consentNotice = consentService.getConsentCrossSellPartner(consentContentRequest.getConsentType(), consentContentRequest.getConsentLink());
                consents.add(consentNotice);

            } else if (ConsentContentEntity.SENSITIVE_CONSENT_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType()) || ConsentContentEntity.SCBS_MARKETING_CONSENT_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType()) || ConsentContentEntity.SCBS_CROSSELL_CONSENT_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType())) {

                ConsentContentInfo sensitiveConsent = consentService.getConsentByTypeAndVendor(consentContentRequest.getConsentType(), consentContentRequest.getVendor());
                consents.add(sensitiveConsent);

            } else if (ConsentContentEntity.DOPA_CONSENT_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType()) || ConsentContentEntity.DOPA_CONSENT_FULL_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType())) {

                ConsentContentInfo dopaConsent = consentService.getConsentByTypeAndVendor(consentContentRequest.getConsentType(), consentContentRequest.getVendor());
                consents.add(dopaConsent);

            } else if (ConsentContentEntity.CROSSMATCH_CONSENT_TYPE.equalsIgnoreCase(consentContentRequest.getConsentType())) {
                ConsentContentInfo crossMatchConsent = consentService.getConsentCrossMatch(consentContentRequest.getConsentType(), consentContentRequest.getReferenceId());
                consents.add(crossMatchConsent);
            }
            else {
                throw new ApplicationException("Consent Type Not Found");
            }
        }


        List<ConsentContentInfo> consentTypeNulls = consents.stream().filter(i -> Strings.isNullOrEmpty(i.getContent())).collect(Collectors.toList());
        List<String> contentTypes = consents.stream().filter(i -> !Strings.isNullOrEmpty(i.getContent())).map(ConsentContentInfo::getConsentType).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(consentTypeNulls)) {
            List<String> contentTypesRequest = consentContentRequests.stream().map(ConsentContentRequest::getConsentType).collect(Collectors.toList());
            contentTypesRequest.removeAll(contentTypes);
            throw new NotFoundException("Data not Found:" + contentTypesRequest.stream().collect(Collectors.joining(",")));
        }

        return consents;
    }
}
