package Models;

/**
 * AuthToken class that stores the authToken and username
 */
public class Authtoken {
    private String authToken;
    private String username;

    public Authtoken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public Authtoken() {
        this.authToken = null;
        this.username = null;
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
