package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.helper.AppFormCaseHelper;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.AdditionalCaseInfo;
import th.co.scb.onboardingapp.model.AppForm;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.EmployeeProfile;
import th.co.scb.onboardingapp.model.common.AppFormDocumentType;
import th.co.scb.onboardingapp.model.common.AppFormRenderMode;
import th.co.scb.onboardingapp.model.common.predicate.AppFormBuilderPredicate;
import th.co.scb.onboardingapp.model.entity.AppFormConfigEntity;
import th.co.scb.onboardingapp.model.entity.OrganizationEntity;
import th.co.scb.onboardingapp.repository.AppformConfigJpaRepository;
import th.co.scb.onboardingapp.repository.OcJpaRepository;
import th.co.scb.onboardingapp.service.common.AppFormHeaderFooterRenderService;
import th.co.scb.onboardingapp.service.common.AppFormPredicateService;
import th.co.scb.onboardingapp.service.common.OtpDateService;
import th.co.scb.onboardingapp.service.service.AppformConfigService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApplicationFormService {

    @Autowired
    private List<FormBuilder> formBuilders;

    @Autowired
    private MappingHelper mappingHelper;

    @Autowired
    private OtpDateService otpDateService;

    @Autowired
    private AppformConfigService appformConfigService;


    @Autowired
    private AppFormHeaderFooterRenderService appFormHeaderFooterRenderService;

//    @Autowired
//    private AppFormSpecialHeaderFooterRenderService appFormSpecialHeaderFooterRenderService;

    @Autowired
    private OcJpaRepository ocRepository;

//    @Autowired
//    private AppformConfigService appformConfigService;


    public List<AppForm> generateAppForm(CaseInfo caseInfo, AppFormDocumentType appFormDocumentType, AppFormRenderMode appFormRenderMode, String additional) {
        log.info(String.format("Generate AppForm : %s", appFormDocumentType.name()));

        if (caseInfo == null || appFormDocumentType == AppFormDocumentType.NONE) {
            return Collections.emptyList();
        }

        AppFormBuilderPredicate appFormBuilderPredicate = AppFormPredicateService.filterForm(appFormDocumentType);

        if (appFormBuilderPredicate == null) {
            log.info("AppForm Predicate Not Found : " + appFormDocumentType.name());
            return Collections.emptyList();
        }


        List<FormBuilder> applicationFormBuilders = formBuilders.stream()
                .filter(appFormBuilderPredicate::buildForm)
                .collect(Collectors.toList());

        List<AppFormConfigEntity> appFormConfigs = appformConfigService.getAppformConfig();
        List<AppForm> appForms = applicationFormBuilders.stream()
                .filter(formBuilder -> formBuilder.isApply(caseInfo) && appformConfigService.isEffective(caseInfo.getCreatedDate().toLocalDate(), formBuilder.getFileName(caseInfo), appFormConfigs))
                .flatMap(formBuilder -> buildAndRender(caseInfo, formBuilder, appFormRenderMode,additional).stream())
                .collect(Collectors.toList());

        LocalDateTime otpDateTime = otpDateService.getOtpDateTime(caseInfo.getCaseId(), caseInfo.getCustomerInfo().getOtpPhoneNumber());
        for (AppForm appForm : appForms) {
            appForm.setApplicationNo(caseInfo.getAppFormNo());
            appForm.setBranchName(getBranchName(caseInfo));
            if (otpDateTime != null) {
                if (caseInfo.getCaseTypeID().equalsIgnoreCase("ONBD")) {
                    appForm.setDateTime(otpDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")));
                } else {
                    appForm.setDateTime(otpDateTime.format(DateTimeFormatter.ofPattern("วันที่ dd/MM/yy เวลา HH:mm")));
                }
            }
        }

//        if (appFormDocumentType == AppFormDocumentType.P006_JAKJAI_CONSENT ||
//                appFormDocumentType == AppFormDocumentType.P009_MARKETING_CONSENT ||
//                appFormDocumentType == AppFormDocumentType.P022_KYC ||
//                appFormDocumentType == AppFormDocumentType.P008_1_W8 ||
//                appFormDocumentType == AppFormDocumentType.P008_2_W9 ||
//                appFormDocumentType == AppFormDocumentType.P028_SCBM_DEBITCARD_CONSENT) {
//            appFormSpecialHeaderFooterRenderService.renderForm(appForms, appFormRenderMode, appFormDocumentType);
//        } else {
            appFormHeaderFooterRenderService.renderForm(appForms, appFormRenderMode);
//        }

        return appForms;
    }

    private String getBranchName(CaseInfo caseInfo) {
        String branchName = "";
        AdditionalCaseInfo additionalInfo = caseInfo.getAdditionalInfo();
        if (additionalInfo == null) {
            return "";
        }

        EmployeeProfile empProfile = additionalInfo.getEmpProfile();
        if (empProfile == null) {
            return "";
        }

        if (empProfile.getIsPersonalBanker()) {
            OrganizationEntity organization = ocRepository.findById(caseInfo.getBranchId()).orElse(null);
            if (organization != null) {
                branchName = "สังกัด " + organization.getOcNameTh();
            }
        } else {
            branchName = "สาขา " + AppFormCaseHelper.getBranchName(caseInfo);
        }

        return branchName;
    }

//    public List<AppForm> generateMutualFundEmailTCAppForm(CaseInfo caseInfo,String additional) {
//        return generateEmailTemplateAppForm(caseInfo, new AppFormMutualFundEmailTCPredicate(), AppFormRenderMode.CUSTOMER_PREVIEW_MODE,additional);
//    }
//
//    public List<AppForm> generateMutualFundEmailRiskAndBAAAppForm(CaseInfo caseInfo,String additional) {
//        return generateEmailTemplateAppForm(caseInfo, new AppFormMutualFundEmailRiskAndBAAPredicate(), AppFormRenderMode.CUSTOMER_PREVIEW_MODE,additional);
//    }
//
//    public List<AppForm> generateSecuritiesEmailTCAppForm(CaseInfo caseInfo,String additional) {
//        return generateEmailTemplateAppForm(caseInfo, new AppFormSecuritiesEmailTCPredicate(), AppFormRenderMode.CUSTOMER_PREVIEW_MODE,additional);
//    }
//
//    public List<AppForm> generateOMNIBUSEmailTCAppForm(CaseInfo caseInfo,String additional) {
//        return generateEmailTemplateAppForm(caseInfo, new AppFormOmnibusTCPredicate(), AppFormRenderMode.CUSTOMER_PREVIEW_MODE,additional);
//    }
//
//    public List<AppForm> generateSecuritiesEmailDirectDebitAppForm(CaseInfo caseInfo,String additional) {
//        return generateEmailTemplateAppForm(caseInfo, new AppFormSecuritiesEmailDirectDebitPredicate(), AppFormRenderMode.CUSTOMER_PREVIEW_MODE,additional);
//    }
//
//    public List<AppForm> generateEPassBookCoverPageEmailAppForm(CaseInfo caseInfo,String additional) {
//        return generateEmailTemplateAppForm(caseInfo, new AppFormEPassbookCoverPageEmailPredicate(), AppFormRenderMode.CUSTOMER_PREVIEW_MODE,additional);
//    }
//
//    public List<AppForm> generateJakjaiEPassBookCoverPageEmailAppForm(CaseInfo caseInfo,String additional) {
//        return generateEmailTemplateAppForm(caseInfo, new AppFormJakjaiEPassbookCoverPageEmailPredicate(), AppFormRenderMode.CUSTOMER_PREVIEW_MODE,additional);
//    }
//
//    public List<AppForm> generateJakjaiTCEmailAppForm(CaseInfo caseInfo,String additional) {
//        return generateEmailTemplateAppForm(caseInfo, new AppFormJakjaiTCEmailPredicate(), AppFormRenderMode.CUSTOMER_PREVIEW_MODE,additional);
//    }
//
//    public List<AppForm> generateEditFundAndLoanEmailAppForm(CaseInfo caseInfo,String additional) {
//        return generateEmailTemplateAppForm(caseInfo, new AppFormEditFundAndLoanPredicate(), AppFormRenderMode.SUBMISSION_MODE,additional);
//    }
//
//    private List<AppForm> generateEmailTemplateAppForm(CaseInfo caseInfo, AppFormBuilderPredicate appFormBuilderPredicate, AppFormRenderMode mode,String additional) {
//        List<AppFormConfigEntity> appFormConfigs = appformConfigService.getAppformConfig();
//        List<AppForm> appForms = formBuilders.stream()
//                .filter(formBuilder -> appformConfigService.isEffective(caseInfo.getCreatedDate().toLocalDate(), formBuilder.getFileName(caseInfo), appFormConfigs))
//                .filter(appFormBuilderPredicate::buildForm)
//                .flatMap(formBuilder -> buildAndRender(caseInfo, formBuilder, mode,additional).stream())
//                .collect(Collectors.toList());
//
//        LocalDateTime otpDateTime = otpDateService.getOtpDateTime(caseInfo.getCaseId(), caseInfo.getCustomerInfo().getOtpPhoneNumber());
//        for (AppForm appForm : appForms) {
//            appForm.setApplicationNo(caseInfo.getAppFormNo());
//            appForm.setBranchName(AppFormCaseHelper.getBranchName(caseInfo));
//            if (otpDateTime != null) {
//                if (caseInfo.getCaseTypeID().equalsIgnoreCase("ONBD")) {
//                    appForm.setDateTime(otpDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")));
//                } else {
//                    appForm.setDateTime(otpDateTime.format(DateTimeFormatter.ofPattern("วันที่ dd/MM/yy เวลา HH:mm")));
//                }
//            }
//        }
//        appFormHeaderFooterRenderService.renderForm(appForms, mode);
//
//        return appForms;
//
//    }
//
//    public List<AppFormAccount> generateEPassbookCoverPage(CaseInfo caseInfo, AppFormRenderMode appFormRenderMode,String additional) {
//
//        List<AppFormAccount> appFormAccountList = new ArrayList<>();
//
//        for (ProductInfo productInfo : caseInfo.getProductInfo()) {
//            if (!"EPASSBOOK".equals(productInfo.getProductType())) {
//                continue;
//            }
//
//            CaseInfo caseForGenerateAppForm = getNewCaseInfoWithOneAccount(caseInfo, productInfo);
//            FormBuilder<?> formBuilder;
//            if ("JJSN".equals(productInfo.getProductCode())) {
//                formBuilder = formBuilders.stream().filter(fb -> fb instanceof EPassbookJakjaiCoverPageFormBuilder).findFirst().orElse(null);
//            } else {
//                formBuilder = formBuilders.stream().filter(fb -> fb instanceof EPassbookCoverPageFormBuilder).findFirst().orElse(null);
//            }
//
//            if (formBuilder != null && formBuilder.isApply(caseForGenerateAppForm)) {
//                List<AppForm> appForms = buildAndRender(caseForGenerateAppForm, formBuilder, appFormRenderMode,additional);
//                for (AppForm appForm : appForms) {
//                    AppFormAccount appFormAccount = mappingHelper.map(appForm, AppFormAccount.class);
//                    appFormAccount.setAccountNumber(productInfo.getAccountNumber());
//                    appFormAccount.setApplicationNo(caseInfo.getAppFormNo());
//                    appFormAccount.setBranchName(AppFormCaseHelper.getBranchName(caseInfo));
//                    appFormAccountList.add(appFormAccount);
//                }
//            }
//        }
//
//        appFormHeaderFooterRenderService.renderForm(appFormAccountList, appFormRenderMode);
//
//        return appFormAccountList;
//    }

    private List<AppForm> buildAndRender(CaseInfo caseInfo, FormBuilder<?> formBuilder, AppFormRenderMode appFormRenderMode,String additional) {
        return formBuilder.buildForm(caseInfo, appFormRenderMode,additional);
    }

//    private CaseInfo getNewCaseInfoWithOneAccount(CaseInfo caseInfo, ProductInfo productInfo) {
//        CaseInfo ret = mappingHelper.map(caseInfo, CaseInfo.class);
//        ret.setProductInfo(new ArrayList<>());
//        ret.getProductInfo().add(productInfo);
//        return ret;
//    }
}