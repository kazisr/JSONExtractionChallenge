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
//        System.out.println(image);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("eng");

        String rawText = tesseract.doOCR(image);
        String cleaned = cleanOcrText(rawText);
        String finalJson = fixBrokenJson(cleaned);
        ObjectMapper mapper = new ObjectMapper();
        System.err.println(finalJson);
        return mapper.readValue(finalJson, Map.class);
    }

    public String cleanOcrText(String rawText) {
        return rawText
                .replaceAll("[^\\x00-\\x7F]", "")
                .replaceAll("[“”]", "\"")
                .replaceAll("[‘’]", "'")
                .replaceAll("[\\p{C}]", "")
                .replaceAll("^\\d+\\s*", "")
                .replaceAll("¥", "")
                .replaceAll("(?<=\\w)™", "\"")
                .trim();
    }

    public String fixBrokenJson(String raw) {
        if (raw.length() > 2) {
            raw = raw.substring(1, raw.length() - 1);
        }

        String[] parts = raw.split(",\\s*");

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            String[] kv = part.split(":", 2);
            if (kv.length != 2) continue;

            String key = kv[0].replaceAll("\"", "").trim();
            String value = kv[1].replaceAll("\"", "").trim();

            jsonBuilder
                    .append("\"").append(key).append("\": ")
                    .append("\"").append(value).append("\"");

            if (i < parts.length - 1) {
                jsonBuilder.append(", ");
            }
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }


}