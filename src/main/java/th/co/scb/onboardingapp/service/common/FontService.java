package th.co.scb.onboardingapp.service.common;


import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.service.StaticPathService;

import java.io.IOException;

@Service
public class FontService {

    @Autowired
    private StaticPathService staticPathService;

    // Should change normalFont matches w/ directory
    @Cacheable(value = "fontNormal")
    public Font createFontNormal(int fontSize) throws DocumentException, IOException {
        String normalFont = staticPathService.getFontsPath().resolve("THSarabunNew.ttf").toString();
        return new Font(BaseFont.createFont(normalFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), fontSize, Font.NORMAL);
    }

    @Cacheable(value = "fontBold")
    public Font createFontBold(int fontSize) throws DocumentException, IOException {
        String normalFont = staticPathService.getFontsPath().resolve("THSarabunNew.ttf").toString();
        return new Font(BaseFont.createFont(normalFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), fontSize, Font.BOLD);
    }
    @Cacheable(value = "fontBullet")
    public Font createFontBullet(int fontSize) throws DocumentException, IOException {
        String bulletFont = staticPathService.getFontsPath().resolve("adf.bullet.ttf").toString();
        return new Font(BaseFont.createFont(bulletFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), fontSize, Font.BOLD);
    }

    @Cacheable(value = "fontCheckMark")
    public Font createFontCheckMark(int fontSize) throws DocumentException, IOException {
        String checkMarkFont = staticPathService.getFontsPath().resolve("DejaVuSans.ttf").toString();
        return new Font(BaseFont.createFont(checkMarkFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), fontSize, Font.BOLD);
    }

    public String getAcrofieldBaseFontPath() {
        return staticPathService.getFontsPath().resolve("THSarabunNewBold.ttf").toString();
    }
}
