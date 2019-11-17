package pl.projects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpConnection {

    public final String URL = "https://api.exchangeratesapi.io/latest";
    public final String BASE = "?base=";
    public final String EXCHANGE_RATE ="?symbols=";

    public String connect(String url) throws IOException {

        URLConnection httpConnection = new URL(url).openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();

        String line = "";
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}