package th.co.scb.onboardingapp.service.helper;

import org.springframework.util.CollectionUtils;
import th.co.scb.onboardingapp.helper.PdfHelper;
import th.co.scb.onboardingapp.model.AppForm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AppFormHelper {

    public static final String BANGKOK = "กรุงเทพ";
    public static final String DASH = "-";

    private AppFormHelper() {
    }


    public static void writeFileToPath(String targetFilePath, String templateFileName, byte[] resultFile) throws IOException {
        File theDir = new File(targetFilePath);
        if (!theDir.exists()) {
            theDir.mkdir();
        }

        Files.write(Paths.get(targetFilePath + "/" + templateFileName + ".pdf"), resultFile);
    }

    public static void writeFileToPathNotCreateDirectory(String targetFilePath, String templateFileName, byte[] resultFile) throws IOException {
        Files.write(Paths.get(targetFilePath + "/" + templateFileName + ".pdf"), resultFile);
    }
    public static byte[] mergeAppFormPages(List<? extends AppForm> appForms) {
        if (CollectionUtils.isEmpty(appForms)) {
            return null;
        }

        if (appForms.size() == 1) {
            return appForms.get(0).getPdfFormPage();
        }

        ArrayList<byte[]> sourcePdfs = new ArrayList<>();
        for (AppForm appForm : appForms) {
            if (appForm.getPdfFormPage() != null) {
                sourcePdfs.add(appForm.getPdfFormPage());
            }
        }

        if (CollectionUtils.isEmpty(sourcePdfs)) {
            return null;
        }

        return PdfHelper.mergePDF(sourcePdfs);
    }

}