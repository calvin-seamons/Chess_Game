package Requests;

/**
 * CreateGameRequest object that has gameName stored in it
 */
public class CreateGameRequest {
    String gameName;
    String authToken;

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
        this.authToken = null;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
