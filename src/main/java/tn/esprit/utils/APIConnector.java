package tn.esprit.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class APIConnector {

    private final String urlString;

    public APIConnector(String urlString) {
        this.urlString = urlString;
    }

    public JSONArray getJSONArray(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            URL url = new URL(urlString + encodedQuery);            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HttpResponseCode: " + conn.getResponseCode());
            }

            StringBuilder sb = new StringBuilder();
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) sb.append(sc.nextLine());
            sc.close();

            JSONParser parser = new JSONParser();
            return (JSONArray) parser.parse(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getJSONObject(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            URL url = new URL(urlString + encodedQuery);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HttpResponseCode: " + conn.getResponseCode());
            }

            StringBuilder sb = new StringBuilder();
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) sb.append(sc.nextLine());
            sc.close();

            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}