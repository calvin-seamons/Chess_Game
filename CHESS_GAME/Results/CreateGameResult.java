package Results;

/**
 * CreateGameResult contains the message that the create game service returns
 * and the gameID of the game that was created
 */
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
