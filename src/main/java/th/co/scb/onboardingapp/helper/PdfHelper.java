package th.co.scb.onboardingapp.helper;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import th.co.scb.onboardingapp.exception.ApplicationException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class PdfHelper {

    private PdfHelper() {
    }

    public static byte[] convertPDFToJPG(byte[] pdfData, int dpi, ImageType color) throws IOException {
        long start = System.currentTimeMillis();
        byte[] output = null;
        PDDocument pdfDocument = PDDocument.load(pdfData);
        PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(pdfRenderer.renderImageWithDPI(0, dpi, color), "jpg", out);
            output = out.toByteArray();
            out.close();
            pdfDocument.close();
            log.debug("Convert Pdf to JPG Time: {}", (System.currentTimeMillis() - start));
            return output;
        } finally {
            if (pdfDocument != null) {
                pdfDocument.close();
            }
        }
    }

    public static byte[] mergePDF(List<byte[]> listOfPDF) {
        long start = System.currentTimeMillis();
        if (CollectionUtils.isEmpty(listOfPDF)) {
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = null;
        Document document = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            document = new Document();
            PdfCopy copy = new PdfCopy(document, byteArrayOutputStream);
            document.open();
            PdfReader reader;
            int numberOfPage;
            if (CollectionUtils.isNotEmpty(listOfPDF)) {
                for (int i = 0; i < listOfPDF.size(); i++) {
                    if (listOfPDF.get(i) != null) {
                        reader = new PdfReader(listOfPDF.get(i));
                        numberOfPage = reader.getNumberOfPages();
                        for (int page = 0; page < numberOfPage; ) {
                            copy.addPage(copy.getImportedPage(reader, ++page));
                        }
                        copy.freeReader(reader);
                        reader.close();
                    }
                }
            }
        } catch (Exception ex) {
            throw new ApplicationException("mergePDF Error: ", ex);
        } finally {
            if (document != null) {
                document.close();
            }
        }

        log.debug("Merge Pdf Time: {}", (System.currentTimeMillis() - start));
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] encryptPdf(byte[] pdf, String password) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            PdfReader reader = new PdfReader(pdf);
            PdfStamper stamper = new PdfStamper(reader, bos);
            stamper.setEncryption(password.getBytes(), "OBIN".getBytes(),
                    PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            stamper.close();
            reader.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return bos.toByteArray();
    }

    public static byte[] resize(byte[] bytes, int width) {
        InputStream in = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            BufferedImage sourceImage = ImageIO.read(in);
            Image image = sourceImage.getScaledInstance(width, -1, Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(image.getWidth(null),
                    image.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            resized.getGraphics().drawImage(image, 0, 0, null);
            ImageIO.write(resized, "jpeg", out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return out.toByteArray();
    }

    public static int getPdfTotalPage(byte[] pdfByteArray) throws IOException {
        PdfReader reader = new PdfReader(pdfByteArray);
        return reader.getNumberOfPages();
    }

}
