package th.co.scb.onboardingapp.service.deposite;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.common.AppFormRenderMode;
import th.co.scb.onboardingapp.model.deposite.KYC101Form;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.service.EmployeeService;
import th.co.scb.onboardingapp.service.FormBuilder;
import th.co.scb.onboardingapp.service.common.AcroFieldService;

import java.util.*;

@Slf4j
@Order(1501)
@Service
public class KYC101FormBuilder extends FormBuilder<KYC101Form> {

    @Autowired
    private AcroFieldService acroFieldService;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public String getFileName(CaseInfo caseInfo) {
        return "01.1.12 HR02 HR08 101.pdf";
    }

    @Override
    public boolean isApply(CaseInfo caseInfo) {
        IdentificationsItem kycItem = caseInfo.getCustomerInfo().getIdentifications().stream()
                .filter(it -> "m101_purpose".equalsIgnoreCase(it.getIdenType()))
                .findFirst().orElse(null);

        if (kycItem != null) {
            return kycItem.getIdenDetail().getPurpose() != null;
        }

        return false;
    }

    @Override
    protected List<KYC101Form> internalBuildForm(CaseInfo caseInfo, String additional) {
        log.info("Build Form : KYC101 FormBuilder");
        KYC101Form kyc101Form = new KYC101Form();

        Optional<IdentificationsItem> identificationsItem = caseInfo.getCustomerInfo().getIdentifications().stream()
                .filter(it -> "m101_purpose".equalsIgnoreCase(it.getIdenType()))
                .findFirst();

        if (identificationsItem.isPresent()) {
            IdenDetail idenDetail = identificationsItem.get().getIdenDetail();
            kyc101Form.setPurposeSalary(idenDetail.getPurpose().isAnswer1() ? "Y" : "");
            kyc101Form.setPurposeBusiness(idenDetail.getPurpose().isAnswer2() ? "Y" : "");
            kyc101Form.setPurposeConsumption(idenDetail.getPurpose().isAnswer3() ? "Y" : "");
        }

        List<String> functionCodes = new ArrayList<>(Arrays.asList("hr_02", "hr_08", "m_101", "m_102"));
        for (String functionCode : functionCodes) {
            AppFormApprover appFormApprover = new AppFormApprover("getApproverId","test","test");
            //TODO: Need to Create AppFormCaseHelper
//            AppFormApprover appFormApprover = AppFormCaseHelper.getApprover(caseInfo, functionCode);
            if (appFormApprover != null) {
                System.out.println("appFormApprover.getApproverId() -> "+ appFormApprover.getApproverId());
                EmployeeEntity approver = new EmployeeEntity();
//                EmployeeEntity approver = employeeService.getEmployee(appFormApprover.getApproverId()).orElse(null);
                approver.setEmployeeId("123");
                if (approver != null) {
                    kyc101Form.setApproverName(appFormApprover.getApproverId() + " - " + approver.getFirstNameThai() + " " + approver.getLastNameThai());
                    break;
                }
            }
        }

        return Collections.singletonList(kyc101Form);
    }

    @Override
    protected List<AppForm> internalRenderForm(List<KYC101Form> formModels, byte[] fileTemplate, AppFormRenderMode appFormRenderMode, CaseInfo caseInfo) {
        log.info("Render Form : KYC101 Form");

        if (CollectionUtils.isEmpty(formModels)) {
            return Collections.emptyList();
        }

        AppForm appForm = new AppForm();
        KYC101Form formModel = formModels.get(0);
        appForm.setFormName(formModel.getClass().getSimpleName());
        try {
            appForm.setPdfFormPage(acroFieldService.prefillPdfByteArray(fileTemplate, acroFieldService.mapClassToAcrofield(formModel)));
        } catch (Exception e) {
            log.error("Render fail", e);
        }

        return Collections.singletonList(appForm);
    }
}
