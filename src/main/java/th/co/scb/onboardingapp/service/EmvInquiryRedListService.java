package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.emv_customer_inquiryredlist.EmvCustomerApi;
import th.co.scb.entapi.emv_customer_inquiryredlist.model.EmailAddressListReq;
import th.co.scb.entapi.emv_customer_inquiryredlist.model.EmailAddressListRes;
import th.co.scb.entapi.emv_customer_inquiryredlist.model.InquiryRedListServiceRequest;
import th.co.scb.entapi.emv_customer_inquiryredlist.model.InquiryRedListServiceResponse;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualRequest;
import th.co.scb.entapi.indikyc.model.PreCalculateKYCIndividualResponse;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.helper.ObaHelper;
import th.co.scb.onboardingapp.helper.RetryCommand;
import th.co.scb.onboardingapp.model.EmvEmailAddress;
import th.co.scb.onboardingapp.model.EmvInquiryRedListRequest;
import th.co.scb.onboardingapp.model.EmvInquiryRedListResponse;
import th.co.scb.onboardingapp.model.ToggleConfig;
import th.co.scb.onboardingapp.model.entity.RedListEntity;


import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class EmvInquiryRedListService {

    @Autowired
    private EmvCustomerApi emvCustomerApi;

    @Autowired
    private TJLogProcessor tjLogProcessor;

    @Autowired
    private ToggleService toggleService;

    @Autowired
    private RedListService redListService;
    @Autowired
    RestTemplate restTemplate;

    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    public EmvInquiryRedListResponse emvInquiryRedList(EmvInquiryRedListRequest request, ObaAuthentication auth) {

        InquiryRedListServiceRequest inquiryRedListServiceRequest = new InquiryRedListServiceRequest();
        inquiryRedListServiceRequest.setChannelCode("A");
        inquiryRedListServiceRequest.setIdType(request.getIdType());
        inquiryRedListServiceRequest.setIdNumber(request.getIdNumber());

        List<EmvEmailAddress> emvEmailAddress = request.getEmailAddressList();
        EmailAddressListReq[] emailAddressListReq = new EmailAddressListReq[emvEmailAddress.size()];
        for (int i = 0; i < emvEmailAddress.size(); i++) {
            EmailAddressListReq emailAddressReq = new EmailAddressListReq();
            emailAddressReq.setEmailAddress(emvEmailAddress.get(i).getEmailAddress());
            emailAddressListReq[i] = emailAddressReq;
        }
        inquiryRedListServiceRequest.setEmailAddressList(emailAddressListReq);

        EmvInquiryRedListResponse emvInquiryRedListResponse = new EmvInquiryRedListResponse();
        try {
           sendInquiryRedList(inquiryRedListServiceRequest, emvInquiryRedListResponse);
        } catch (Exception ex) {
            retrySendInquiryRedList(auth.getCaseId(), inquiryRedListServiceRequest, emvInquiryRedListResponse);
        }

        logTjLogCheckRedList(auth.getCaseId(), emvInquiryRedListResponse);
        return emvInquiryRedListResponse;
    }

    private void retrySendInquiryRedList(String caseId, InquiryRedListServiceRequest inquiryRedListServiceRequest, EmvInquiryRedListResponse emvInquiryRedListResponse) {
        int maxRetry = 3;

        try {
            RetryCommand retryCommand = new RetryCommand(maxRetry);
            retryCommand.retry(() -> sendInquiryRedList(inquiryRedListServiceRequest, emvInquiryRedListResponse));
        } catch (Exception ex) {
            logTjLogCheckRedListFail(caseId, inquiryRedListServiceRequest);
            throw new ApplicationException(ex.getMessage());
        }
    }

    private EmvInquiryRedListResponse sendInquiryRedList(InquiryRedListServiceRequest inquiryRedListServiceRequest, EmvInquiryRedListResponse emvInquiryRedListResponse) {
        InquiryRedListServiceResponse inquiryRedListServiceResponse = eMV(inquiryRedListServiceRequest);
        return getEmvInquiryRedListResponse(inquiryRedListServiceResponse, emvInquiryRedListResponse);
    }

    private EmvInquiryRedListResponse getEmvInquiryRedListResponse(InquiryRedListServiceResponse inquiryRedListServiceResponse, EmvInquiryRedListResponse emvInquiryRedListResponse) {
        if (nonNull(inquiryRedListServiceResponse.getData()) && nonNull(inquiryRedListServiceResponse.getData().getEmailAddressList())) {
            EmailAddressListRes[] emailAddressListRes = inquiryRedListServiceResponse.getData().getEmailAddressList();
            List<EmvEmailAddress> emvEmailAddress = new ArrayList<>();
            for (EmailAddressListRes emailAddressRes : emailAddressListRes) {
                EmvEmailAddress emailAddress = new EmvEmailAddress();
                emailAddress.setEmailAddress(emailAddressRes.getEmailAddress());
                emailAddress.setIsRedList(emailAddressRes.isRedList());
                emvEmailAddress.add(emailAddress);
            }
            emvInquiryRedListResponse.setEmailAddressList(emvEmailAddress);
        }
        return emvInquiryRedListResponse;
    }

    private void logTjLogCheckRedList(String caseId, EmvInquiryRedListResponse emvInquiryRedListResponse) {
        if (nonNull(emvInquiryRedListResponse) && nonNull(emvInquiryRedListResponse.getEmailAddressList())) {
            List<EmvEmailAddress> emvEmailAddress = emvInquiryRedListResponse.getEmailAddressList();
            for (EmvEmailAddress emailAddressRes : emvEmailAddress) {
                tjLogProcessor.logCheckRedList(caseId, emailAddressRes.getEmailAddress(), emailAddressRes.getIsRedList());
                insertRedList(caseId, emailAddressRes.getEmailAddress(), emailAddressRes.getIsRedList());
            }
        }
    }

    private void logTjLogCheckRedListFail(String caseId, InquiryRedListServiceRequest inquiryRedListServiceRequest) {
        if (nonNull(inquiryRedListServiceRequest) && nonNull(inquiryRedListServiceRequest.getEmailAddressList())) {
            EmailAddressListReq[] emailAddressListReq = inquiryRedListServiceRequest.getEmailAddressList();
            for (EmailAddressListReq emailAddressRes : emailAddressListReq) {
                tjLogProcessor.logCheckRedListFail(caseId, emailAddressRes.getEmailAddress());
            }
        }
    }

    private void insertRedList(String caseId,  String email, String redListFlag) {
        ToggleConfig toggleConfigs = toggleService.toggles().stream()
                .filter(i -> "VALIDATE_EMAIL_SUBMIT".equalsIgnoreCase(i.getToggleName()))
                .findFirst().orElse(null);

        if (nonNull(toggleConfigs) && "Y".equals(toggleConfigs.getToggleValue())) {
            RedListEntity redListEntity = new RedListEntity();
            redListEntity.setCaseId(caseId);
            redListEntity.setEmail(email);
            redListEntity.setRedListFlag("true".equalsIgnoreCase(redListFlag) ? "N" : "Y");
            redListService.insertRedList(redListEntity);
        }
    }

    public InquiryRedListServiceResponse eMV(InquiryRedListServiceRequest inquiryRedListServiceRequest) {
        if(isEntApiMode) {
            return emvCustomerApi.eMV(inquiryRedListServiceRequest);
        }else{
            org.springframework.http.HttpEntity<InquiryRedListServiceRequest> request = new HttpEntity<>(inquiryRedListServiceRequest,
                    ObaHelper.getHttpHeaders(emvCustomerApi.getApiConfig().getApisecret(), emvCustomerApi.getApiConfig().getSourceSystem(), emvCustomerApi.getApiConfig().getApikey(), null,null));
            return restTemplate.postForObject(
                    emvCustomerApi.getApiConfig().getBasePath() + "/v1/emv/customer/inquiryRedList",
                    request, InquiryRedListServiceResponse.class);

        }

    }
}
