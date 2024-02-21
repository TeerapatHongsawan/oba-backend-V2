package th.co.scb.onboardingapp.service.common;

import com.google.common.base.Strings;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.model.AppForm;
import th.co.scb.onboardingapp.model.common.AppFormRenderMode;
import th.co.scb.onboardingapp.model.common.Location;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class AppFormHeaderFooterRenderService {

    @Autowired
    private FontService fontService;

    public void renderForm(List<? extends AppForm> appForms, AppFormRenderMode appFormRenderMode) {
        int pageNumber = 1;
        int totalPage = appForms.size();
        for (AppForm appForm : appForms) {
            if (appForm.getTotalPage() > 1) {
                totalPage += appForm.getTotalPage() - 1;
            }
        }
        log.info(String.format("AppForm TotalPage: %s", totalPage));

        for (AppForm appForm : appForms) {
            if ("IdCardForm".equalsIgnoreCase(appForm.getFormName()) || "SignatureCardForm".equalsIgnoreCase(appForm.getFormName())) {
                continue;
            }
            int oldTotalPage = appForm.getTotalPage();
            appForm.setPageNumber(pageNumber);
            appForm.setTotalPage(totalPage);
//            prepareHeaderAndFooter(appForm, appFormRenderMode, oldTotalPage);
            if (oldTotalPage > 1) {
                pageNumber += oldTotalPage;
                appForm.setTotalPage(oldTotalPage);
            } else {
                pageNumber++;
            }
            appForm.setTotalPage(oldTotalPage);
        }
    }

    private void prepareHeaderAndFooter(AppForm appForm, AppFormRenderMode appFormRenderMode, int totalPage) {

        ByteArrayOutputStream byteArrayOutputStream = null;
        PdfReader pdfReader = null;
        PdfStamper pdfStamper = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            pdfReader = new PdfReader(appForm.getPdfFormPage());
            float pageHeight = pdfReader.getPageSize(1).getHeight();
            pdfStamper = new PdfStamper(pdfReader, byteArrayOutputStream);
            PdfContentByte canvas = pdfStamper.getOverContent(1);
            renderHeaderAndFooter(canvas, appForm, appFormRenderMode, pageHeight);
            if (totalPage > 1) {
                for (int i = 2; i <= totalPage; i++) {
                    appForm.setPageNumber(appForm.getPageNumber() + 1);
                    canvas = pdfStamper.getOverContent(i);
                    renderHeaderAndFooter(canvas, appForm, appFormRenderMode, pageHeight);
                }
            }
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        } finally {
            if (pdfStamper != null) {
                try {
                    pdfStamper.close();
                } catch (Exception ignored) {
                    log.info("AppForm prepareHeaderAndFooter fail.");
                }
            }
            if (pdfReader != null) {
                pdfReader.close();
            }

        }

        byte[] formWithPageNumber = byteArrayOutputStream.toByteArray();
        appForm.setPdfFormPage(formWithPageNumber);
    }

    private void renderHeaderAndFooter(PdfContentByte canvas, AppForm appForm, AppFormRenderMode appFormRenderMode, float pageHeight) {
        if (appFormRenderMode == AppFormRenderMode.SUBMISSION_MODE) {
            renderHeader(canvas, appForm.getDateTime(), appForm.getBranchName(), appForm.getApplicationNo(), new Location(20, pageHeight - 20));
        }
    }

    private void renderHeader(PdfContentByte canvas, String dateTime, String branchName, String applicationNo, Location headerLocation) {
        try {
            Font fontNormalSize16 = fontService.createFontNormal(16);

            float x = headerLocation.getX();
            float y = headerLocation.getY();
            if (Strings.isNullOrEmpty(dateTime)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
                dateTime = LocalDateTime.now().format(formatter);
            }

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(dateTime + " " + branchName, fontNormalSize16), x, y, 0);
            x = headerLocation.offsetX(530);
            ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase("เลขที่ใบคำขอ " + applicationNo, fontNormalSize16), x, y, 0);
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

}
