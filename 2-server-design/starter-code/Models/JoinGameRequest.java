package Models;

public class JoinGameRequest {
    private String authToken;
    private String gameID;
    private playerColor team;

    public JoinGameRequest() {}

    private enum playerColor{
        WHITE,
        BLACK
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGameID() {
        return gameID;
    }

    public playerColor getTeam() {
        return team;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setTeam(playerColor team) {
        this.team = team;
    }
}
