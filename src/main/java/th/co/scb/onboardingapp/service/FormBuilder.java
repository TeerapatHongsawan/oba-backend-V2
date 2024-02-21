package th.co.scb.onboardingapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.model.AppForm;
import th.co.scb.onboardingapp.model.AppFormModel;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.common.AppFormRenderMode;
import th.co.scb.onboardingapp.service.common.FontService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Service
public abstract class FormBuilder<T extends AppFormModel> {

    @Autowired
    protected FontService fontService;


    public abstract String getFileName(CaseInfo caseInfo);

    public abstract boolean isApply(CaseInfo caseInfo);

    private byte[] fileByteCache;

    private HashMap<String, byte[]> fileByteCacheList = new HashMap<>();

    private byte[] getFileTemplate(CaseInfo caseInfo) {
        String fileName = getFileName(caseInfo);

        if (fileByteCacheList.get(fileName) != null) {
            return fileByteCacheList.get(fileName);
        }

        try {
            String templatePath = "src/main/resources/appform"; // Should change templatePath matches w/ directory
            fileByteCache = Files.readAllBytes(Paths.get(templatePath, getFileName(caseInfo)));
            fileByteCacheList.put(fileName, fileByteCache);
        } catch (Exception ex) {
            throw new ApplicationException("FormBuilder getFileTemplate Error: ", ex);
        }

        return fileByteCache;
    }

    public List<AppForm> buildForm(CaseInfo caseInfo, AppFormRenderMode appFormRenderMode, String additional) {
        try {
            return internalRenderForm(internalBuildForm(caseInfo, additional), getFileTemplate(caseInfo), appFormRenderMode, caseInfo);
        } catch (Exception ex) {
            throw new ApplicationException("FormBuilder buildForm Error: ", ex);
        }
    }

    protected abstract List<T> internalBuildForm(CaseInfo caseInfo, String additional);

    protected abstract List<AppForm> internalRenderForm(List<T> formModels, byte[] fileTemplate, AppFormRenderMode appFormRenderMode, CaseInfo caseInfo);
}