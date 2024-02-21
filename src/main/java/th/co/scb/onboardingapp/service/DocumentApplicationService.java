package th.co.scb.onboardingapp.service;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import th.co.scb.captiva.CaptivaApi;
import th.co.scb.captiva.model.TicketResponse;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.helper.DocumentHelper;
import th.co.scb.onboardingapp.model.CaptureResponse;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.UploadRequest;
import th.co.scb.onboardingapp.model.entity.DocumentBlobEntity;
import th.co.scb.onboardingapp.repository.DocumentBlobJpaRepository;


import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class DocumentApplicationService {

    @Autowired
    private DocumentBlobJpaRepository documentBlobRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CaptivaApi captivaApi;


    @Value("${application.name}")
    private String appName;





    private DocumentBlobEntity buildDocumentBlobToSave(UploadRequest data, String uploadSessionId) {
        try {
            DocumentBlobEntity documentBlob = new DocumentBlobEntity();
            documentBlob.setUploadSessionId(uploadSessionId);
            documentBlob.setMimeType(data.getMimeType());
            documentBlob.setCreatedTime(LocalDateTime.now());
            documentBlob.setUpdatedTime(LocalDateTime.now());
            documentBlob.setBinaryData(Base64.getDecoder().decode(data.getDocObj()));
           // documentBlob.setJsonData("{}");
            return documentBlob;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid base64 uploadSessionId: {} docObj: {}", uploadSessionId, data.getDocObj());
            throw ex;
        }
    }


    public CaptureResponse uploadDocument(UploadRequest data, ObaAuthentication auth) {
        String uploadSessionId = getUploadSessionId(auth);
        CaseInfo caseInfo = data.getCaseInfo();
        // caseInfo use to test log when send base64 in data request
        if (caseInfo == null) {
            documentService.saveDocument(buildDocumentBlobToSave(data, uploadSessionId));
        }
        return CaptureResponse.builder()
                .uploadSessionId(uploadSessionId)
                .build();
    }



    private String getUploadSessionId(ObaAuthentication auth) {
        String branchId = auth.getBranchId();
        String nextVal = documentService.nextVal(branchId);
        return DocumentHelper.buildUploadSessionId(nextVal, branchId, appName);
    }

    private String buildFileName(String uploadSessionId, String mimeType) {
        String[] split = mimeType.split("/");
        return String.format("%s.%s", uploadSessionId, split[1]);
    }


}
