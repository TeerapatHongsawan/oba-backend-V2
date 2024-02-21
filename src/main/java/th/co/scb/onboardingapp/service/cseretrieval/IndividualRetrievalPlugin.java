package th.co.scb.onboardingapp.service.cseretrieval;


import io.github.openunirest.http.async.Callback;
import io.github.openunirest.request.HttpRequestWithBody;
import kotlin.jvm.internal.Intrinsics;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.csent_customer_consent.model.InitiateRequestv2;
import th.co.scb.entapi.csent_customer_consent.model.InitiateResponsev2;
import th.co.scb.entapi.individuals.CustomerProfileIndividualsV3Api;
import th.co.scb.entapi.individuals.model.IndividualV3;
import th.co.scb.indi.infrastructure.client.RequestHeaderConfig;
import th.co.scb.indi.infrastructure.client.UnirestFuture;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.helper.ObaHelper;
import th.co.scb.onboardingapp.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import static th.co.scb.onboardingapp.constant.Constants.*;


@Slf4j
@Service
@CaseCreateExistingQualifier
@ForeignerCaseCreateExistingQualifier
public class IndividualRetrievalPlugin extends BaseRetrievalPlugin<IndividualV3> {

    @Autowired
    private MappingHelper mappingHelper;

    @Autowired
    private CustomerProfileIndividualsV3Api customerProfileIndividualsV3Api;

    @Autowired
    RestTemplate restTemplate;



    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;

    @Override
    public CompletableFuture<IndividualV3> retrieveCase(CaseInfo caseInfo) {
        return getIndividual2Async(caseInfo);
    }

    @Override
    public void updateCase(CaseInfo caseInfo, IndividualV3 individual) {
        CustomerInfo customerInfo = caseInfo.getCustomerInfo();

        mappingHelper.copy(individual, customerInfo);

        setDocNo(caseInfo, individual);

        customerInfo.setDob(individual.getBirthDate());
        customerInfo.setThaiSuffix(individual.getThaiSuffixTitle());
        customerInfo.setEngSuffix(individual.getEngSuffixTitle());
        customerInfo.setOccupationDescription(individual.getOccupationDetail());
        customerInfo.setOccupationIsicCode(individual.getISICCode());
        customerInfo.setSourceOfIncome(individual.getCountrySourceOfIncome());

        checkMonitoringCode(caseInfo, individual);

        if (individual.getRiskLevel() != null) {
            checkKyc(caseInfo, individual);
        }
    }

    private void checkMonitoringCode(CaseInfo caseInfo, IndividualV3 individual) {
        IdentificationsItem identificationsItem = new IdentificationsItem();
        identificationsItem.setIdenType("monitoring");

        if (StringUtils.isEmpty(individual.getMonitorCode())) {
            identificationsItem.setIdenStatus("pass");
            log.info("Monitoring Status : Pass");
        } else {
            identificationsItem.setIdenStatus("fail");
            log.info("Monitoring Status : Fail " + individual.getMonitorCode());
            checkRequireApproval(caseInfo, individual.getMonitorCode());
        }

        Monitoring monitoring = new Monitoring();
        monitoring.setCode(individual.getMonitorCode());
        monitoring.setDesc("N/A");

        IdenDetail mornitoringIdenDetail = new IdenDetail();
        mornitoringIdenDetail.setMonitoring(monitoring);
        identificationsItem.setIdenDetail(mornitoringIdenDetail);

        caseInfo.getCustomerInfo().getIdentifications().add(identificationsItem);
    }

    private void setDocNo(CaseInfo caseInfo, IndividualV3 individual) {
        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        if (DOCTYPE_ALIEN.equalsIgnoreCase(customerInfo.getDocType())) {
            customerInfo.setDocNo(individual.getAlienID());
        } else if (DOCTYPE_PASSPORT.equalsIgnoreCase(customerInfo.getDocType())) {
            customerInfo.setDocNo(individual.getPassportNumber());
        } else {
            customerInfo.setDocNo(individual.getCitizenID());
        }
    }

    private void checkKyc(CaseInfo caseInfo, IndividualV3 individual) {
        String score = String.format("%s%s", individual.getRiskLevel().getLevel(), individual.getRiskLevel().getReason());

        IdentificationsItem kycIdentificationsItem = new IdentificationsItem();
        kycIdentificationsItem.setIdenType("kyc");
        kycIdentificationsItem.setIdenStatus(score);

        IdenDetail kycIdenDetail = new IdenDetail();
        Kyc kyc = new Kyc();
        kyc.setOriginKYCScore(score);
        kyc.setKycDateTime(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));

        kycIdenDetail.setKyc(kyc);
        kycIdentificationsItem.setIdenDetail(kycIdenDetail);

        caseInfo.getCustomerInfo().getIdentifications().add(kycIdentificationsItem);
    }

    private void checkRequireApproval(CaseInfo caseInfo, String monitorCode) {
        switch (monitorCode) {
            case MONITOR_CODE_101:
            case MONITOR_CODE_102:
                ApprovalInfo approvalInfo = new ApprovalInfo();
                approvalInfo.setFunctionCode("M_" + monitorCode);
                approvalInfo.setApprovalStatus("P");

                caseInfo.getApprovalInfo().add(approvalInfo);
                log.info("Monitoring : " + monitorCode + " Require approval");
                break;
            default:
                break;
        }
    }
    private CompletableFuture<IndividualV3> getIndividual2Async(CaseInfo caseInfo){
        if(isEntApiMode) {
            return customerProfileIndividualsV3Api.getIndividual2Async(caseInfo.getReferenceId());
        }else{

            String partnerID = String.valueOf(caseInfo.getReferenceId());

                 CompletableFuture<IndividualV3> future = new CompletableFuture<>();

            CompletableFuture.runAsync(() -> {
                try {
                    ResponseEntity<IndividualV3> result = restTemplate.exchange(
                            customerProfileIndividualsV3Api.getApiConfig().getBasePath() +  "/v3/customer/profile/individuals/{partnerID}",
                            HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(customerProfileIndividualsV3Api.getApiConfig().getApisecret(), customerProfileIndividualsV3Api.getApiConfig().getSourceSystem(), customerProfileIndividualsV3Api.getApiConfig().getApikey(), null,null))
                            ,IndividualV3.class,partnerID);

                    future.complete(result.getBody());
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });

            return future;
        }

    }
}
