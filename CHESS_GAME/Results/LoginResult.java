package Results;

/**
 * LoginResult contains the message, authToken, and username that the login service returns
 */
public class LoginResult {
    private String username;
    private String authToken;
    private String message;
    public LoginResult() {}

    public String getMessage() {
        return message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
