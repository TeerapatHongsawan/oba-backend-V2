package th.co.scb.onboardingapp.model;

import com.itextpdf.text.pdf.BaseFont;
import lombok.Data;

@Data
public class AcroField {

    private String key;
    private String value;
    private BaseFont font;
    private Float fontSize;

    public AcroField(String key, String value) {
        this.key = key;
        this.value = value;
        this.fontSize = 12f;
    }

    public AcroField(String key, String value, Float fontSize) {
        this.key = key;
        this.value = value;
        this.fontSize = fontSize;
    }
}
