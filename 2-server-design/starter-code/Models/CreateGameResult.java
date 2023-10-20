package Models;

public class CreateGameResult {
    String gameID;
    String message;

    public CreateGameResult(String gameID, String message) {
        this.gameID = gameID;
        this.message = message;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
