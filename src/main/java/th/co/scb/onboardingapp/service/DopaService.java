package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.external_agency_dopa.model.CardStatusOutManual;
import th.co.scb.entapi.external_agency_dopa.model.FindByCardInfoRequest;
import th.co.scb.entapi.external_agency_dopa.model.FindByCardInfoResponse;
import th.co.scb.onboardingapp.helper.DateHelper;
import th.co.scb.onboardingapp.model.ApprovalInfo;
import th.co.scb.onboardingapp.model.CardInfo;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.Dopa;
import th.co.scb.onboardingapp.model.IdenDetail;
import th.co.scb.onboardingapp.model.IdentificationsItem;
import th.co.scb.onboardingapp.model.TJLogActivity;
import th.co.scb.onboardingapp.model.enums.CaseStatus;
import th.co.scb.onboardingapp.service.api.DopaApiService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Slf4j
@Service
public class DopaService {

    @Autowired
    TJLogProcessor tjLogProcessor;

//    @Autowired
//    ExternalAgencyDOPAApi dopaApi;

    @Autowired(required = false)
    JmxFlags featureFlags;

    @Autowired
    DopaApiService dopaApiService;


    public Function<CaseInfo, CompletableFuture<?>> call(CardInfo cardInfo, IdentificationsItem item, List<TJLogActivity> tjlogList) {
        CompletableFuture<FindByCardInfoResponse> async = retrieveCase(cardInfo);
        return cse -> retrieve(cse, item, async, tjlogList);
    }

    protected CompletableFuture<FindByCardInfoResponse> retrieveCase(CardInfo cardInfo) {
        if (featureFlags != null && !featureFlags.has("DopaReal")) {
            FindByCardInfoResponse findByCardInfoResponse = new FindByCardInfoResponse();
            CardStatusOutManual cardStatus = new CardStatusOutManual();
            cardStatus.setIsError(false);
            cardStatus.setCode("0");
            cardStatus.setDesc("สถานะปกติ");
            findByCardInfoResponse.setCardStatusOut(cardStatus);

            return CompletableFuture.completedFuture(findByCardInfoResponse);
        }

        String pID = cardInfo.getDocNo();
        String firstName = cardInfo.getThaiFirstName();
        String lastName = cardInfo.getThaiLastName();
        String birthDate = DateHelper.convertBirthDate(cardInfo.getDob());
        String laser = cardInfo.getLaserCode();

        FindByCardInfoRequest findByCardInfoRequest = new FindByCardInfoRequest();
        findByCardInfoRequest.setBirthDate(birthDate);
        findByCardInfoRequest.setFirstName(firstName);
        findByCardInfoRequest.setLastName(lastName);
        findByCardInfoRequest.setPID(pID);
        findByCardInfoRequest.setLaser(laser);

        return dopaApiService.dOPAFindByCardInfoV2Async(findByCardInfoRequest);
    }


    protected CompletableFuture<?> retrieve(CaseInfo cse, IdentificationsItem item, CompletableFuture<FindByCardInfoResponse> dopaCall, List<TJLogActivity> tjlogList) {
        return dopaCall.handle((ok, ko) -> {
            if (ko == null) {
                tjLogProcessor.logTxnPrivacy(cse, item);
                this.updateCase(cse, ok, tjlogList);
            } else {
                tjLogProcessor.getTxnDopaFail(cse, tjlogList);
                this.handleError(cse);
            }
            return null;
        });
    }

    protected void updateCase(CaseInfo cse, FindByCardInfoResponse result, List<TJLogActivity> tjlogList) {
        CardStatusOutManual cardStatusOut = result.getCardStatusOut();
        tjLogProcessor.getTxnDopa(cse, tjlogList, cardStatusOut);

        Dopa dopa = new Dopa();
        dopa.setResult(cardStatusOut);

        IdenDetail detail = new IdenDetail();
        detail.setDopa(dopa);

        IdentificationsItem identificationsItem = new IdentificationsItem();
        identificationsItem.setIdenType("dopa");
        identificationsItem.setIdenDetail(detail);

        if ("0".equalsIgnoreCase(cardStatusOut.getCode())) {
            log.info("DOPA Status : Valid");
            dopa.setStatus("Y");
            identificationsItem.setIdenStatus("pass");
        } else {
            log.info("DOPA Status : Invalid");
            dopa.setStatus("N");
            identificationsItem.setIdenStatus("rejected");

            if ("6".equalsIgnoreCase(cardStatusOut.getCode())) {
                cardStatusOut.setCode("DP00001");
                log.info("DOPA Status : return error http status: 500 from dopa the system");
            } else if ("4".equals(cardStatusOut.getCode()) && cardStatusOut.getDesc().contains("Laser")) {
                cardStatusOut.setCode("DP00003");
                log.info("DOPA Status : Invalid (Wrong Laser ID)");
            } else {
                cardStatusOut.setCode("DP00002");
                log.info("DOPA Status : Invalid");
            }
            cse.setCaseStatus(CaseStatus.REJECTED.getValue());
        }

        List<IdentificationsItem> identifications = cse.getCustomerInfo().getIdentifications();
        identifications.add(identificationsItem);
    }

    protected void handleError(CaseInfo cse) {
        log.info("DOPA Connection Failed : " + "Require approval");
        ApprovalInfo approvalInfo = new ApprovalInfo();
        approvalInfo.setFunctionCode("DOPA");
        approvalInfo.setApprovalStatus("P");
        cse.getApprovalInfo().add(approvalInfo);

        Dopa dopa = new Dopa();
        dopa.setStatus("N");

        IdenDetail detail = new IdenDetail();
        detail.setDopa(dopa);

        IdentificationsItem identificationsItem = new IdentificationsItem();
        identificationsItem.setIdenType("dopa");
        identificationsItem.setIdenStatus("rerun");
        identificationsItem.setIdenDetail(detail);

        cse.getCustomerInfo().getIdentifications().add(identificationsItem);
    }
}