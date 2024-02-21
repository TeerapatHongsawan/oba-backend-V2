package th.co.scb.onboardingapp.service.cseretrieval;

import io.github.openunirest.http.async.Callback;
import io.github.openunirest.request.HttpRequestWithBody;
import kotlin.jvm.internal.Intrinsics;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import th.co.scb.entapi.docs_vchan.DocumentManagementVChannelApi;
import th.co.scb.entapi.docs_vchan.model.IDCard;
import th.co.scb.entapi.individuals.model.IndividualV3;
import th.co.scb.indi.infrastructure.client.ApiException;
import th.co.scb.indi.infrastructure.client.UnirestFuture;
import th.co.scb.onboardingapp.helper.ObaHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CustomerInfo;

import java.util.concurrent.CompletableFuture;

@Service
@CaseCreateExistingQualifier
public class VChannelRetrievalPlugin extends BaseRetrievalPlugin<IDCard> {

    @Autowired
    private DocumentManagementVChannelApi documentManagementVChannelApi;

    @Autowired
    RestTemplate restTemplate;



    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    @Override
    protected CompletableFuture<IDCard> retrieveCase(CaseInfo caseInfo) {
        return getIDCardAsync(caseInfo.getCustomerInfo().getDocNo(), caseInfo.getCustomerInfo().getDocType());
    }

    @Override
    protected void updateCase(CaseInfo caseInfo, IDCard idCard) {
        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        String issueDate = convertDateFormat(idCard.getIssueDate());
        customerInfo.setIssueDate(issueDate);

        String expiryDate = convertDateFormat(idCard.getExpiryDate());
        customerInfo.setExpDate(expiryDate);

        if (caseInfo.getCustomerInfo().getCardInfo().isManualKeyIn() && idCard.getIdCardCustomerPicture() != null) {
            caseInfo.getCustomerInfo().setPhoto("data:image/jpeg;base64," + Base64.encodeBase64String(idCard.getIdCardCustomerPicture()));
        }
    }

    @Override
    protected void handleError(CaseInfo caseInfo, Throwable ex) {
        if (ex instanceof ApiException && ((ApiException) ex).getStatusCode() == 404) {
            caseInfo.getCustomerInfo().setIssueDate("");
            caseInfo.getCustomerInfo().setExpDate("");
        } else {
            super.handleError(caseInfo, ex);
        }
    }

    public String convertDateFormat(String sourceDate) {
        if (sourceDate == null || sourceDate.length() != 8) {
            return "";
        } else {
            int year = Integer.parseInt(sourceDate.substring(0, 4));
            year = (year == 9999) ? year : (year - 543);

            String formatYear = String.valueOf(year);
            String formatMonth = sourceDate.substring(4, 6);
            String formatDay = sourceDate.substring(6, 8);

            return formatYear + "-" + formatMonth + "-" + formatDay;
        }
    }

    private CompletableFuture<IDCard> getIDCardAsync(String idNumber,String idType ){
        if(isEntApiMode) {
            return this.documentManagementVChannelApi.getIDCardAsync(idNumber, idType);
        }else{

            String url = documentManagementVChannelApi.getApiConfig().getBasePath() +  "/v1/support/docmgnt/idCards/retrieveById";
            CompletableFuture<IDCard> future = new CompletableFuture<>();
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("idNumber", idNumber)
                    .queryParam("idType", idType);
            CompletableFuture.runAsync(() -> {
                try {
                    ResponseEntity<IDCard> result = restTemplate.exchange(builder.toUriString(),
                            HttpMethod.GET, new HttpEntity<>(ObaHelper.getHttpHeaders(documentManagementVChannelApi.getApiConfig().getApisecret(), documentManagementVChannelApi.getApiConfig().getSourceSystem(), documentManagementVChannelApi.getApiConfig().getApikey(), null,null))
                            ,IDCard.class);

                    future.complete(result.getBody());
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });

            return future;
        }

    }

}

