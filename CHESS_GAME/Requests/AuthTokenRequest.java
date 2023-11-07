package Requests;

public class AuthTokenRequest {
    private String authToken;

    private String username;

    public AuthTokenRequest() {
        this.authToken = null;
        this.username = null;
    }

    public AuthTokenRequest(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
