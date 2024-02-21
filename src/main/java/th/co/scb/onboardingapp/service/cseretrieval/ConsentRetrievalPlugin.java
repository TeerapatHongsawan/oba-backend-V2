package th.co.scb.onboardingapp.service.cseretrieval;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.csent_customer_consent.CustomerConsentsV2Api;
import th.co.scb.entapi.csent_customer_consent.model.ConsentInfoList;
import th.co.scb.entapi.csent_customer_consent.model.InitiateRequestv2;
import th.co.scb.entapi.csent_customer_consent.model.InitiateResponsev2;
import th.co.scb.entapi.csent_customer_consent.model.ScopesList;
import th.co.scb.onboardingapp.helper.ForeignerHelper;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.ConsentContentEntity;
import th.co.scb.onboardingapp.service.ConsentService;
import th.co.scb.onboardingapp.service.api.CustomerConsentsApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static th.co.scb.onboardingapp.constant.Constants.OBIN;

@Slf4j
@Service
@CaseCreateNewQualifier
@CaseContinueExistingQualifier
@ForeignerCaseCreateNewQualifier
@ForeignerCaseContinueExistingQualifier
public class ConsentRetrievalPlugin extends BaseRetrievalPlugin<InitiateResponsev2> {


    // TODO : no longer use
//    @Autowired
//    private CustomerConsentsV2Api customerConsentsV2Api;

    @Autowired
    private CustomerConsentsApiService customerConsentsV2Api;

    @Autowired
    private ConsentService consentService;

    @Override
    protected CompletableFuture<InitiateResponsev2> retrieveCase(CaseInfo caseInfo) {
        InitiateRequestv2 consentInitiateRequest = new InitiateRequestv2();

        if (ForeignerHelper.isForeigner(caseInfo)) {
            if (!StringUtils.isEmpty(caseInfo.getCustomerInfo().getCardInfo().getOldDocNo())) {
                consentInitiateRequest.setCustomerId(caseInfo.getCustomerInfo().getCardInfo().getOldDocNo());
            } else {
                consentInitiateRequest.setCustomerId(caseInfo.getCustomerInfo().getCardInfo().getDocNo());
            }
        } else {
            consentInitiateRequest.setCustomerId(caseInfo.getCustomerInfo().getDocNo());
        }

        consentInitiateRequest.setReferenceType(caseInfo.getCustomerInfo().getDocType());
        consentInitiateRequest.setChannel(OBIN);

        String[] consentTypeList = new String[]{ConsentContentEntity.MARKETING_CONSENT_TYPE, ConsentContentEntity.CROSS_SELL_CONSENT_TYPE};
        consentInitiateRequest.setConsentType(consentTypeList);

        return customerConsentsV2Api.initiateV2Async("th", consentInitiateRequest);
    }

    @Override
    protected void updateCase(CaseInfo caseInfo, InitiateResponsev2 initiateConsent) {
        IdentificationsItem identificationsItem = caseInfo.getCustomerInfo().getIdentifications().stream()
                .filter(it -> "consent".equalsIgnoreCase(it.getIdenType()))
                .findAny().orElse(null);

        if (identificationsItem != null) {
            IdenDetail idenDetail = identificationsItem.getIdenDetail();
            idenDetail.setStatusCode(initiateConsent.getStatus().getCode());
            idenDetail.setStatusDesc(initiateConsent.getStatus().getDescription());

            if ("3005".equalsIgnoreCase(initiateConsent.getStatus().getCode()) && (initiateConsent.getData() != null)) {
                List<ConsentData> consentDataList = prepareConsentData(initiateConsent);
                idenDetail.getData().addAll(consentDataList);
            }
        }
    }

    @NotNull
    private List<ConsentData> prepareConsentData(InitiateResponsev2 initiateConsent) {
        List<ConsentData> consentDataList = new ArrayList<>();
        for (ConsentInfoList consentInfo : initiateConsent.getData().getConsentInfo()) {

            saveConsentCrossSellAndMarketing(consentInfo);

            ConsentData consentData = new ConsentData();
            consentData.setConsentType(consentInfo.getConsentType());
            consentData.setConsentVersion(consentInfo.getConsentVersion());
            consentData.setVendor("SCB");

            List<Scopes> scopeList = getScopes(consentInfo);
            consentData.setScopes(scopeList);

            consentDataList.add(consentData);
        }

        return consentDataList;
    }

    @NotNull
    private static List<Scopes> getScopes(ConsentInfoList consentInfo) {
        List<Scopes> scopeList = new ArrayList<>();
        for (ScopesList scope : consentInfo.getScopes()) {
            Display display = new Display();
            display.setName(scope.getDisplay().getName());
            display.setVisible(scope.getDisplay().getVisible());

            Scopes scopes = new Scopes();
            scopes.setKey(scope.getKey());
            scopes.setDisplay(display);

            scopeList.add(scopes);
        }

        return scopeList;
    }

    @Override
    protected void handleError(CaseInfo caseInfo, Throwable ex) {
        IdentificationsItem identificationsItem = caseInfo.getCustomerInfo().getIdentifications().stream()
                .filter(it -> "consent".equalsIgnoreCase(it.getIdenType()))
                .findAny().orElse(null);
        if (identificationsItem != null) {
            identificationsItem.setIdenStatus("Initial Consent API Fail");
            identificationsItem.getIdenDetail().setStatusDesc(ex.getMessage());
        }
        log.error("Consent ERROR", ex);
    }

    private void saveConsentCrossSellAndMarketing(ConsentInfoList consentInfo) {
        consentService.saveConsentCrossSellAndMarketing(consentInfo.getConsentType(), consentInfo.getConsentVersion(), "", "SCB", consentInfo.getContent().getTitle(), consentInfo.getContent().getBody());
    }
}
