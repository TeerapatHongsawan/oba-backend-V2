package th.co.scb.onboardingapp.service.cseretrieval;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.detica.model.DeticaValidationRequest;
import th.co.scb.entapi.detica.model.DeticaValidationResponse;
import th.co.scb.onboardingapp.helper.ForeignerHelper;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.enums.CaseStatus;
import th.co.scb.onboardingapp.service.api.DeticaApiService;
import th.co.scb.onboardingapp.service.TJLogProcessor;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@CaseCreateNewQualifier
@ForeignerCaseCreateNewQualifier
public class DeticaRetrievalPlugin extends BaseRetrievalPlugin<DeticaValidationResponse[]> {


    @Autowired
    DeticaApiService deticaApiService;

    @Autowired
    TJLogProcessor tjLogProcessor;

    @Override
    protected CompletableFuture<DeticaValidationResponse[]> retrieveCase(CaseInfo caseInfo) {
        CustomerInfo customerInfo = caseInfo.getCustomerInfo();

        DeticaValidationRequest deticaValidationRequest = new DeticaValidationRequest();
        if (customerInfo != null) {
            String thaiName;
            String engName;
            if (ForeignerHelper.isForeigner(caseInfo)) {
                thaiName = customerInfo.getThaiFirstName() + " " + (customerInfo.getThaiMiddleName() != null ? customerInfo.getThaiMiddleName() + " " : "") + customerInfo.getThaiLastName();
                engName = customerInfo.getEngFirstName() + " " + (customerInfo.getEngMiddleName() != null ? customerInfo.getEngMiddleName() + " " : "") + customerInfo.getEngLastName();
                if (StringUtils.isNotEmpty(caseInfo.getCustomerInfo().getCardInfo().getOldDocNo())) {
                    deticaValidationRequest.setIdNumber(caseInfo.getCustomerInfo().getCardInfo().getOldDocNo());
                } else {
                    deticaValidationRequest.setIdNumber(caseInfo.getCustomerInfo().getDocNo());
                }
            } else {
                deticaValidationRequest.setIdNumber(caseInfo.getCustomerInfo().getDocNo());
                thaiName = customerInfo.getThaiFirstName() + " " + customerInfo.getThaiLastName();
                engName = customerInfo.getEngFirstName() + " " + customerInfo.getEngLastName();
            }

            deticaValidationRequest.setThaiName(thaiName);
            deticaValidationRequest.setEngName(engName);
        }

        DeticaValidationRequest[] deticaValidationRequests = new DeticaValidationRequest[]{
                deticaValidationRequest
        };

        return  deticaApiService.validateBlacklistAsync(deticaValidationRequests);
    }

    @Override
    protected void updateCase(CaseInfo caseInfo, DeticaValidationResponse[] deticaValidationResponses) {
        DeticaValidationResponse deticaValidationResponse = deticaValidationResponses[0];

        IdentificationsItem identificationsItem = new IdentificationsItem();
        identificationsItem.setIdenType("cdd");

        DeticaCheck deticaCheck = new DeticaCheck();
        if (deticaValidationResponse.getHitResults() != null && "hit".equalsIgnoreCase(deticaValidationResponse.getResultCode())) {
            identificationsItem.setIdenStatus("hit");
            log.info("CDD Status : HIT");

            if (deticaCheck.worldCheck(deticaValidationResponse.getHitResults())) {
                log.info("CDD Status : HIT World Check");
                tjLogProcessor.logTxnDetica(caseInfo, "TXN079");
            }

            if (deticaCheck.checkAMLO(deticaValidationResponse.getHitResults())) {
                log.info("CDD Status : Hit on AMLO ");
                tjLogProcessor.logTxnDetica(caseInfo, "TXN078");
            }

            if (deticaCheck.checkAMLOFreeze(deticaValidationResponse.getHitResults())) {
                log.info("CDD Status : Hit on AMLO Frreeze");
                tjLogProcessor.logTxnDetica(caseInfo, "TXN080");
                caseInfo.setCaseStatus(CaseStatus.REJECTED.getValue());
            }
        } else {
            identificationsItem.setIdenStatus("nohit");
            log.info("CDD Status : NO HIT");
            tjLogProcessor.logTxnDetica(caseInfo, "TXN077");
        }

        IdenDetail idenDetail = new IdenDetail();
        idenDetail.setCdd(deticaValidationResponse.getHitResults());
        identificationsItem.setIdenDetail(idenDetail);

        Monitoring monitoring = new Monitoring();
        monitoring.setCode("");
        monitoring.setDesc("N/A");
        IdenDetail monitorIdenDetail = new IdenDetail();
        monitorIdenDetail.setMonitoring(monitoring);

        IdentificationsItem monitorIdentificationsItem = new IdentificationsItem();
        monitorIdentificationsItem.setIdenDetail(monitorIdenDetail);
        monitorIdentificationsItem.setIdenType("monitoring");
        monitorIdentificationsItem.setIdenStatus("");

        List<IdentificationsItem> identifications = caseInfo.getCustomerInfo().getIdentifications();
        identifications.add(identificationsItem);
        identifications.add(monitorIdentificationsItem);
    }
}
