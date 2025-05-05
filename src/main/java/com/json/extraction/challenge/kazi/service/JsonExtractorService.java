package com.json.extraction.challenge.kazi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Map;

@Service
public class JsonExtractorService {

    public Map<String, Object> extractJsonFromImage(String base64Image) throws Exception {

        String cleanBase64 = base64Image.replaceFirst("^data:image/[^;]+;base64,", "");

        byte[] imageBytes = Base64.getDecoder().decode(cleanBase64);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("eng");

        String rawText = tesseract.doOCR(image);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(rawText, Map.class);
    }
}