package ui;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPClient {
    //Takes the command line arguments and passes them into the Chess Client

    public static void get(String msg) {
        try {
            var url = new URL("http://localhost:8080/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
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
