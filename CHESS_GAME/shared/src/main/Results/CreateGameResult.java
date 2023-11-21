package Results;

/**
 * CreateGameResult contains the message that the create game service returns
 * and the gameID of the game that was created
 */
public class CreateGameResult {
    Integer gameID; // Changed from int to Integer to allow null
    String message;

    public CreateGameResult(Integer gameID, String message) {
        this.gameID = gameID;
        this.message = message;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) { // Parameter type changed to Integer
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
