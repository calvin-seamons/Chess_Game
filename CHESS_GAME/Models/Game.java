package Models;


/**
 * Game class that has getters and setters
 * for gameId, whiteUsername, blackUsername, gameName, and gameImplementation
 */
public class Game {
    private String gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private String gameImplementation;

    public String getGameId() {
        return gameID;
    }

    public void setGameID(String gameId) {
        this.gameID = gameId;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameImplementation() {
        return gameImplementation;
    }

    public void setGameImplementation(String gameImplementation) {
        this.gameImplementation = gameImplementation;
    }
}
