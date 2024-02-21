package th.co.scb.onboardingapp.service.plugin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.AppForm;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.common.AppFormDocumentType;
import th.co.scb.onboardingapp.model.common.AppFormRenderMode;
import th.co.scb.onboardingapp.service.ApplicationFormService;
import th.co.scb.onboardingapp.service.common.AppFormHeaderFooterRenderService;
import th.co.scb.onboardingapp.service.helper.AppFormHelper;
import th.co.scb.onboardingapp.service.submission.BaseSubmissionPlugin;
import th.co.scb.onboardingapp.service.submission.SubmissionState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class GenerateDocumentForSecuritiesPlugin implements BaseSubmissionPlugin {
    @Autowired
    private ApplicationFormService applicationFormService;

    private final Map<AppFormDocumentType, String> filterDoctypeFileName;

    public GenerateDocumentForSecuritiesPlugin() {
        filterDoctypeFileName = new HashMap<>();
//        filterDoctypeFileName.put(AppFormDocumentType.P020_SECURITIES_ID_CARD, "_IdCard");
//        filterDoctypeFileName.put(AppFormDocumentType.P019_SECURITIES_SIGNATURE, "_SignatureCard");
//        filterDoctypeFileName.put(AppFormDocumentType.P017_SECURITIES_IWEALTH_TERM_AND_CONDITION, "_iOnboardTC");
//        filterDoctypeFileName.put(AppFormDocumentType.S007_SECURITIES_SET_CUSTOMER_PRODUCT_INFO, "_ApplicationForm");
//        filterDoctypeFileName.put(AppFormDocumentType.P018_SECURITIES_RISK_AND_BAA, "_ClientSuitability");
//        filterDoctypeFileName.put(AppFormDocumentType.P016_DIRECT_DEBIT_TERM_AND_CONDITION, "_new_ddscbs_xxxx");
    }
    public static final String NAME = "generateDocumentForSecurities";

    @Value("${securities.document.filepath}") // Should change value matches w/ directory
    private String targetFilePath;

    @Autowired
    private AppFormHeaderFooterRenderService formHeaderFooterRenderService;

    @Autowired
    @Qualifier("fixedThreadExecutor")
    private Executor executor;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isApply(CaseInfo caseInfo) {
        return false;
    }


    @Override
    public boolean isHardStop() {
        return true;
    }

    @Override
    public void submit(CaseInfo caseInfo, SubmissionState state) {
        state.submit(CompletableFuture.runAsync(() -> generateAppForms(caseInfo), executor));
    }

    private void generateAppForms(CaseInfo caseInfo) {
        for (Map.Entry<AppFormDocumentType, String> mapEntry : filterDoctypeFileName.entrySet()) {
            try {
                List<AppForm> appForms = applicationFormService.generateAppForm(caseInfo, mapEntry.getKey(), AppFormRenderMode.SUBMISSION_MODE, null);
                formHeaderFooterRenderService.renderForm(appForms, AppFormRenderMode.CHECKER_PREVIEW_MODE);
                if (mapEntry.getValue().contains("new_ddscbs")) {
                    String securitiesAccountNo = caseInfo.getCustomerInfo().getInvestment().getSecurities().getAtsAccountNumber();
                    if (securitiesAccountNo != null) {
                        securitiesAccountNo = securitiesAccountNo.substring(securitiesAccountNo.length() - 6);
                    }
                    AppFormHelper.writeFileToPath(targetFilePath, caseInfo.getAppFormNo() + filterDoctypeFileName.get(mapEntry.getKey()) + securitiesAccountNo, AppFormHelper.mergeAppFormPages(appForms));

                } else {
                    AppFormHelper.writeFileToPath(targetFilePath, caseInfo.getAppFormNo() + filterDoctypeFileName.get(mapEntry.getKey()), AppFormHelper.mergeAppFormPages(appForms));
//                    AppFormHelper.writeFileToPath(targetFilePath, caseInfo.getAppFormNo() + filterDoctypeFileName.get(mapEntry.getKey()), AppFormHelper.mergeAppFormPages(appForms));
                }
            } catch (Exception e) {
                log.error("Cannot Write File :" + caseInfo.getAppFormNo() + " : " + mapEntry.getValue(), e);
            }
        }
    }
}
