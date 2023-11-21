package ui;

import Requests.RegisterRequest;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPClient {
    private static final String BASE_URL = "http://localhost:8080";
    private final Gson gson = new Gson();

    public boolean register(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        String jsonRequest = gson.toJson(request);

        try {
            var url = new URL(BASE_URL + "/user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();
            try (InputStream respBody = conn.getInputStream()) {
                byte[] bytes = new byte[respBody.available()];
                respBody.read(bytes);
                System.out.println(new String(bytes));
                return true;
            }
        } catch (Exception ex) {
            System.out.printf("ERROR: %s\n", ex);
            return false;
        }
    }

    public static void post(String msg) {
        try {
            var url = new URL("http://localhost:8080/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();
            try (InputStream respBody = conn.getInputStream()) {
                byte[] bytes = new byte[respBody.available()];
                respBody.read(bytes);
                System.out.println(new String(bytes));
            }
        } catch (Exception ex) {
            System.out.printf("ERROR: %s\n", ex);
        }
    }
}
