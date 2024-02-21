package th.co.scb.onboardingapp.service.common;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.reflections.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.model.AcroField;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class AcroFieldService {

    @Autowired
    private FontService fontService;

    public void addImage(PdfStamper stamper, AcroFields form, String field, String fieldValue) throws DocumentException, IOException {
        List<AcroFields.FieldPosition> photograph = form.getFieldPositions(field);
        if (CollectionUtils.isNotEmpty(photograph)) {
            Rectangle rect = photograph.get(0).position;

            Image img = Image.getInstance(Base64.getDecoder().decode(fieldValue));
            img.scaleToFit(rect.getWidth(), rect.getHeight());
            img.setBorder(2);

            float x = (photograph.get(0).position.getLeft() + photograph.get(0).position.getRight()) / 2 - img.getScaledWidth() / 2;
            float y = (photograph.get(0).position.getTop() + photograph.get(0).position.getBottom()) / 2 - rect.getHeight() / 2;
            img.setAbsolutePosition(x, y);

            PdfContentByte cb = stamper.getOverContent(photograph.get(0).page);
            cb.addImage(img);
        }
    }

    public byte[] prefillPdfByteArray(byte[] pdfByteArray, List<AcroField> list) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfReader reader = new PdfReader(pdfByteArray);
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            AcroFields form = stamper.getAcroFields();
            form.setGenerateAppearances(true);
            form.addSubstitutionFont(getDefaultBaseFont());
            for (AcroField acroField : list) {
                form.setFieldProperty(acroField.getKey(), "textsize", getFontSize(acroField.getFontSize()), null);
                form.setFieldProperty(acroField.getKey(), "textfont", getBaseFont(acroField.getFont()), null);
                if (isImage(acroField.getValue())) {
                    addImage(stamper, form, acroField.getKey(), acroField.getValue());
                } else {
                    if (form.getFieldType(acroField.getKey()) == AcroFields.FIELD_TYPE_CHECKBOX) {
                        form.setField(acroField.getKey(), acroField.getValue(), true);
                    } else {
                        form.setField(acroField.getKey(), acroField.getValue());
                    }
                }
            }

            stamper.setFormFlattening(true);
            stamper.close();
            reader.close();
            return outputStream.toByteArray();
        } catch (Exception ex) {
            throw new ApplicationException("AcroFieldService prefillPdfByteArray Error: ", ex);
        }
    }

    public BaseFont getDefaultBaseFont() throws DocumentException, IOException {
        String normalFont = fontService.getAcrofieldBaseFontPath();
        return BaseFont.createFont(normalFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }
    public List<AcroField> mapClassToAcrofield(Object obj) {
        List<AcroField> acroFields = new ArrayList<>();
        Set<Field> fields = ReflectionUtils.getAllFields(obj.getClass());
        for (Field field : fields) {
            MappingFormField annotation = field.getAnnotation(MappingFormField.class);
            if(annotation != null && org.apache.commons.lang.StringUtils.isNotEmpty(annotation.value())){
                acroFields.add(new AcroField(annotation.value(), AcroFieldService.callGetter(obj,  field.getName())));
            }else{
                acroFields.add(new AcroField(field.getName(), AcroFieldService.callGetter(obj, field.getName())));
            }
        }
        return acroFields;
    }

    public static String callGetter(Object obj, String fieldName) {
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            Object object = pd.getReadMethod().invoke(obj);
            if (object instanceof String) {
                return (String) object;
            }
            if (object instanceof Boolean) {
                return object.toString();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public BaseFont getBaseFont(BaseFont bf) throws DocumentException, IOException {
        return (bf == null) ? getDefaultBaseFont() : bf;
    }

    public Float getFontSize(Float fs) {
        float defaultFontSize = 16F;
        return (fs == null || fs < 0) ? defaultFontSize : fs;
    }

    public boolean isImage(String base64) {
        try {
            Image.getInstance(Base64.getDecoder().decode(base64));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
