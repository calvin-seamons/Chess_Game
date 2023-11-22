package ui;

import Requests.CreateGameRequest;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.CreateGameResult;
import Results.LoginResult;
import Results.RegisterResult;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static final Gson gson = new Gson();

    public RegisterResult register(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        String jsonRequest = gson.toJson(request);

        try {
            var url = new URL(BASE_URL + "/user");
            HttpURLConnection conn = getHttpURLConnection(jsonRequest, url);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
//                System.out.println(response);
//                System.out.println(gson.fromJson(response.toString(), RegisterResult.class));
                return gson.fromJson(response.toString(), RegisterResult.class);
            }
        } catch (Exception ex) {
            System.out.printf("ERROR: %s\n", ex);
            return null;
        }
    }

    public LoginResult login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
        String jsonRequest = gson.toJson(request);

        try {
            var url = new URL(BASE_URL + "/session");
            HttpURLConnection conn = getHttpURLConnection(jsonRequest, url);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                return gson.fromJson(response.toString(), LoginResult.class);
            }
        } catch (Exception ex) {
            System.out.printf("ERROR: %s\n", ex);
            return null;
        }
    }

    public CreateGameResult createGame(String gameName) {
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        String jsonRequest = gson.toJson(createGameRequest);

        try {
            var url = new URL(BASE_URL + "/games/" + gameName);
            HttpURLConnection conn = getHttpURLConnection(jsonRequest, url);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                return gson.fromJson(response.toString(), CreateGameResult.class);
            }
        } catch (Exception ex) {
            System.out.printf("ERROR: %s\n", ex);
            return null;
        }
    }

    private static HttpURLConnection getHttpURLConnection(String jsonRequest, URL url) throws IOException {
        HttpURLConnection conn;
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return conn;
    }
}
