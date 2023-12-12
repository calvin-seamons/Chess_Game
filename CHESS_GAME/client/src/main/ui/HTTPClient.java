package ui;

import Requests.CreateGameRequest;
import Requests.JoinGameRequest;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPClient {
    private static String BASE_URL;
    private static final Gson gson = new Gson();

    public HTTPClient(String URL) {
        BASE_URL = URL;
    }

    public RegisterResult register(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        String jsonRequest = gson.toJson(request);

        try {
            var url = new URL(BASE_URL + "/user");
            HttpURLConnection conn = getHttpURLConnection(jsonRequest, url, "POST", null);

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
            return new RegisterResult("ERROR: " + ex);
        }
    }

    public LoginResult login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
        String jsonRequest = gson.toJson(request);

        try {
            var url = new URL(BASE_URL + "/session");
            HttpURLConnection conn = getHttpURLConnection(jsonRequest, url, "POST", null);

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
            return new LoginResult("ERROR: " + ex);
        }
    }

    public CreateGameResult createGame(String gameName, String currentUserAuthToken) {
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        String jsonRequest = gson.toJson(createGameRequest);

        try {
            var url = new URL(BASE_URL + "/game");
            HttpURLConnection conn = getHttpURLConnection(jsonRequest, url, "POST", currentUserAuthToken);
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
            return new CreateGameResult("ERROR: " + ex);
        }
    }

    public ListGamesResult listGames(String currentUserAuthToken) {
        try {
            var url = new URL(BASE_URL + "/game");
            HttpURLConnection conn = getHttpURLConnection(null, url, "GET", currentUserAuthToken);
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                return gson.fromJson(response.toString(), ListGamesResult.class);
            }
        } catch (Exception ex) {
            return new ListGamesResult("ERROR: " + ex);
        }
    }

    public JoinGameResult joinGame(int gameID, String teamColor, String currentUserAuthToken) {
        JoinGameRequest joinGameRequest = new JoinGameRequest(null, gameID, teamColor);
        String jsonRequest = gson.toJson(joinGameRequest);

        try {
            var url = new URL(BASE_URL + "/game");
            HttpURLConnection conn = getHttpURLConnection(jsonRequest, url, "PUT", currentUserAuthToken);
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                return gson.fromJson(response.toString(), JoinGameResult.class);
            }
        } catch (Exception ex) {
            return new JoinGameResult("ERROR: " + ex);
        }
    }

    public LogoutResult logout(String currentUserAuthToken) {
        try {
            var url = new URL(BASE_URL + "/session");
            HttpURLConnection conn = getHttpURLConnection(null, url, "DELETE", currentUserAuthToken);
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                return gson.fromJson(response.toString(), LogoutResult.class);
            }
        } catch (Exception ex) {
            return new LogoutResult("Error: Unauthorized\n");
        }
    }

    public String clear() {
        try {
            var url = new URL(BASE_URL + "/db");
            HttpURLConnection conn = getHttpURLConnection(null, url, "DELETE", null);
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                System.out.println(response.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
            return "ERROR: " + ex;
        }
    }

    private static HttpURLConnection getHttpURLConnection(String jsonRequest, URL url, String method, String currentUserAuthToken) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        // Set common headers
        conn.setRequestProperty("Accept", "application/json");

        // Add Authorization header only if authToken is provided
        if (currentUserAuthToken != null && !currentUserAuthToken.isEmpty()) {
            conn.setRequestProperty("Authorization", currentUserAuthToken);
        }

        // For POST and PUT requests, set content type and write the request body
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true); // Required to send a request body

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        return conn;
    }

}
