package th.co.scb.onboardingapp.helper;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class ImageResizer {

    private ImageResizer() {
    }

    public static byte[] resizeImage(byte[] inputImage, int newWidth, int newHeight) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(inputImage));

        Image resultingImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        ImageIO.write(outputImage, "jpg", outputStream);

        return outputStream.toByteArray();
    }

    public static int getNewHeight(byte[] inputImage, int newWidth) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(inputImage));
        double width = (newWidth / (double) originalImage.getWidth());
        int newHeight = (int) (originalImage.getHeight() * width);

        log.info("Original image: {} x {} pixels. Resize image: {} x {} pixels.",
                originalImage.getWidth(), originalImage.getHeight(), newWidth, newHeight);

        return newHeight;
    }
}