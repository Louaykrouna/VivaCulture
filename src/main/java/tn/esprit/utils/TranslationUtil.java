package tn.esprit.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslationUtil {
    private static final String API_KEY = "02b729331ee65655ef2e";
    private static final String API_URL = "https://api.mymemory.translated.net/get";

    public static String translateToEnglish(String frenchText) throws IOException {
        String encodedText = URLEncoder.encode(frenchText, StandardCharsets.UTF_8);
        String urlStr = String.format("%s?q=%s&langpair=fr|en&key=%s", API_URL, encodedText, API_KEY);

        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            JsonObject response = JsonParser.parseString(new String(conn.getInputStream().readAllBytes()))
                    .getAsJsonObject();
            return response.get("responseData").getAsJsonObject().get("translatedText").getAsString();
        }
        throw new IOException("Erreur de traduction: " + conn.getResponseCode());
    }
}