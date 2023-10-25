package Requests;

/**
 * CreateGameRequest object that has gameName stored in it
 */
public class CreateGameRequest {
    String gameName;

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
