package th.co.scb.onboardingapp.service.submission;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.helper.DocumentHelper;
import th.co.scb.onboardingapp.helper.ForeignerHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.DmsInfo;
import th.co.scb.onboardingapp.model.common.AppFormProductType;
import th.co.scb.onboardingapp.model.common.AppFormTypeEnum;
import th.co.scb.onboardingapp.model.common.SubmissionApplicationForm;
import th.co.scb.onboardingapp.model.entity.DocumentBlobEntity;
import th.co.scb.onboardingapp.service.DocumentService;
import th.co.scb.onboardingapp.service.common.AppFormProductService;
import th.co.scb.onboardingapp.service.common.DocumentInfoService;
import th.co.scb.onboardingapp.service.helper.AppFormHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@Order(40)
public class GenerateApplicationFormPlugin implements BaseSubmissionPlugin {

    public static final String NAME = "generateApplicationForm";
    @Autowired
    private DocumentInfoService documentInfoService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    @Qualifier("fixedThreadExecutor")
    private Executor executor;

//    @Autowired
//    private ApplicationFormSubmissionService applicationFormSubmissionService;

    @Autowired
    private AppFormProductService appFormProductService;

    @Value("${application.name}")
    private String appName;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isHardStop() {
        return true;
    }

    @Override
    public boolean isApply(CaseInfo caseInfo) {
        return true;
    }

    @Override
    public void submit(CaseInfo caseInfo, SubmissionState state) {
        state.submit(CompletableFuture.runAsync(() -> generateAppForms(caseInfo), executor));
    }

    public void generateAppForms(CaseInfo caseInfo) {
        AppFormProductType appFormProductType = appFormProductService.getAppFormProductType(caseInfo);
        log.info("AppForm Product Type : " + appFormProductType.name());

        List<SubmissionApplicationForm> submissionApplicationFormList = new ArrayList<>();

        appFormProductType.getDocs().forEach(type -> submissionApplicationFormList.addAll(buildForm(type, caseInfo)));

//        if (!ForeignerHelper.isForeigner(caseInfo)) {
//            submissionApplicationFormList.addAll(applicationFormSubmissionService.buildIDCardForms(caseInfo));
//        }
        saveDocumentToDocBlob(caseInfo, submissionApplicationFormList);
        updateDocumentInfo(caseInfo, submissionApplicationFormList);
    }

    private List<SubmissionApplicationForm> buildForm(AppFormTypeEnum type, CaseInfo caseInfo) {
        switch (type) {
//            case S002:
//                return applicationFormSubmissionService.buildS002Forms(caseInfo);
//            case S003:
//                return applicationFormSubmissionService.buildS003Forms(caseInfo);
//            case S005:
//                return applicationFormSubmissionService.buildS005Forms(caseInfo);
//            case S006:
//                return applicationFormSubmissionService.buildS006Forms(caseInfo);
//            case S008:
//                return applicationFormSubmissionService.buildS008Forms(caseInfo);
//            case S009:
//                return applicationFormSubmissionService.buildS009Forms(caseInfo);
//            case S010:
//                return applicationFormSubmissionService.buildS010Forms(caseInfo);
//            case S013:
//                return applicationFormSubmissionService.buildS013Forms(caseInfo);
//            case S014:
//                return applicationFormSubmissionService.buildS014Forms(caseInfo);
//            case S303:
//                return applicationFormSubmissionService.buildS303Forms(caseInfo);
//            case S303_1:
//                return applicationFormSubmissionService.buildS303AForms(caseInfo);
//            case S304:
//                return applicationFormSubmissionService.buildS304Forms(caseInfo);
//            case S306:
//                return applicationFormSubmissionService.buildS306Forms(caseInfo);
//            case P006:
//                return applicationFormSubmissionService.buildP006JakjaiConsentForms(caseInfo);
//            case P008_1:
//                return applicationFormSubmissionService.buildP008AFatcaW8Forms(caseInfo);
//            case P008_2:
//                return applicationFormSubmissionService.buildP008BFatcaW9Forms(caseInfo);
//            case P009:
//                return applicationFormSubmissionService.buildP009MarketingConsentForms(caseInfo);
//            case P022:
//                return applicationFormSubmissionService.buildP022KycForms(caseInfo);
//            case P023:
//                return applicationFormSubmissionService.buildP023SignatureCardForms(caseInfo);
//            case S016:
//                return applicationFormSubmissionService.buildS016Forms(caseInfo);
            default:
                return Collections.emptyList();
        }
    }

    private void saveDocumentToDocBlob(CaseInfo caseInfo, List<SubmissionApplicationForm> submissionApplicationFormList) {
        for (SubmissionApplicationForm submissionApplicationForm : submissionApplicationFormList) {
//            if (AppFormDocumentType.P025_SIGNATURE_CARD_EPASSBOOK.toString().equals(submissionApplicationForm.getDocType()) || AppFormDocumentType.P026_SIGNATURE_CARD_PASSBOOK.toString().equals(submissionApplicationForm.getDocType())) {
//                List<AppForm> appForms = submissionApplicationForm.getAppForms();
//                for (AppForm appForm : appForms) {
//                    String uploadSessionId = getUploadSessionId(caseInfo.getBranchId());
//                    DocumentBlobEntity documentBlob = new DocumentBlobEntity();
//                    documentBlob.setUploadSessionId(uploadSessionId);
//                    documentBlob.setMimeType(submissionApplicationForm.getMimeType());
//                    documentBlob.setCreatedTime(LocalDateTime.now());
//                    documentBlob.setUpdatedTime(LocalDateTime.now());
//                    documentBlob.setBinaryData(appForm.getPdfFormPage());
//                    documentService.saveDocument(documentBlob);
//                    submissionApplicationForm.setUploadSessionId(uploadSessionId);
//                }
//            } else {
                String uploadSessionId = getUploadSessionId(caseInfo.getBranchId());
                DocumentBlobEntity documentBlob = new DocumentBlobEntity();
                documentBlob.setUploadSessionId(uploadSessionId);
                documentBlob.setMimeType(submissionApplicationForm.getMimeType());
                documentBlob.setCreatedTime(LocalDateTime.now());
                documentBlob.setUpdatedTime(LocalDateTime.now());
                documentBlob.setBinaryData(AppFormHelper.mergeAppFormPages(submissionApplicationForm.getAppForms()));
                documentService.saveDocument(documentBlob);
                submissionApplicationForm.setUploadSessionId(uploadSessionId);
//            }
        }
    }

    private void updateDocumentInfo(CaseInfo caseInfo, List<SubmissionApplicationForm> submissionApplicationFormList) {
        List<DmsInfo> dmsInfoList = caseInfo.getDocumentInfo();
        dmsInfoList = documentInfoService.buildDmsInfoList(dmsInfoList, submissionApplicationFormList);
        caseInfo.setDocumentInfo(dmsInfoList);
    }

    private String getUploadSessionId(String branchId) {
        String nextVal = documentService.nextVal(branchId);
        return DocumentHelper.buildUploadSessionId(nextVal, branchId, appName);
    }
}
