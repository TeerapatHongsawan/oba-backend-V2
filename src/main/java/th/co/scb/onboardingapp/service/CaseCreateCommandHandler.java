package th.co.scb.onboardingapp.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.helper.CaseHelper;
import th.co.scb.onboardingapp.helper.IdCardHelper;
import th.co.scb.onboardingapp.helper.TokenHelper;
import th.co.scb.onboardingapp.helper.UUIDHelper;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import static java.util.Objects.nonNull;
import static th.co.scb.onboardingapp.reqctx.logback.EventMarker.markEvent;

@Slf4j
@Component
public class CaseCreateCommandHandler {

    @Autowired(required = false)
    DopaService dopaService;

    @Autowired
    CaseService caseService;

    @Autowired
    RetrievalService retrievalService;

    @Autowired
    TJLogProcessor tjLogProcessor;

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    IdCardHelper idCardHelper;

    @Autowired
    TokenHelper tokenHelper;

    public CaseInfo createCase(CaseRequest createCaseRequest, ObaAuthentication authentication, HttpServletResponse httpServletResponse) {

        String caseId = UUIDHelper.generateUUID();
        MDC.put("caseId", caseId);

        CardInfo cardInfo = createCaseRequest.getCardInfo();
        IdentificationsItem identificationsItem = createCaseRequest.getIdentificationsItem();
        ChaiyoLoanDetail chaiyoLoanDetail = createCaseRequest.getChaiyoLoanDetail();

        Function<CaseInfo, CompletableFuture<?>> dopaCall = null;
        List<TJLogActivity> tjLogActivities = new ArrayList<>();

        if (nonNull(this.dopaService)) {
            log.info(markEvent("CHECK_DOPA"), "Validate customer information with DOPA");
            dopaCall = this.dopaService.call(cardInfo, identificationsItem, tjLogActivities);
        }

        CaseInfo caseInfo = this.caseService.buildCase(cardInfo, identificationsItem, authentication, caseId);
        if (nonNull(chaiyoLoanDetail)) {
            this.caseService.setChaiYoCaseLoanDetails(caseInfo, chaiyoLoanDetail);
        }

        this.caseService.applyCardInfo(caseInfo, cardInfo);

        if (idCardHelper.isIdCardExpired(cardInfo)) {
            log.error("ID card is expired");
            throw new ConflictException(CaseErrorCodes.CASE_IDCARD_EXPIRE.name(), CaseErrorCodes.CASE_IDCARD_EXPIRE.getMessage());
        }

        if ("Y".equals(caseInfo.getCustomerInfo().getIsExistingCustomer())) {
            log.info(markEvent("CHECK_CUSTOMER_TYPE"), "Customer is ETB (EXISTING TO BANK)");
            this.retrievalService.fetchExistingCustomerData(caseInfo).join();
            CaseHelper.setDefaultData(caseInfo);

            if (idCardHelper.isCardIssuedDateBeforeVChannelIssuedDate(caseInfo, cardInfo)) {
                log.error("ID card issued date is before vchannel's issued date");
                throw new ConflictException(CaseErrorCodes.CASE_IDCARD_ISSUEDDATE_BEFORE_VCHANNEL.name(), CaseErrorCodes.CASE_IDCARD_ISSUEDDATE_BEFORE_VCHANNEL.getMessage());
            }
        } else {
            log.info(markEvent("CHECK_CUSTOMER_TYPE"), "Customer is NTB (NEW TO BANK)");
            this.retrievalService.fetchNewCustomerData(caseInfo).join();
            CaseHelper.setDefaultData(caseInfo);
        }

        if (dopaCall != null) {
            dopaCall.apply(caseInfo).join();
        }

        this.tjLogProcessor.logCaseCreateTxn(caseInfo, tjLogActivities);
        this.caseService.createCase(caseInfo);
        this.tokenHelper.setToken(authentication, httpServletResponse, caseInfo);
        this.publisher.publishEvent(new OnboardingProductsAndServicesEvent(caseInfo));

        return caseInfo;
    }
}
