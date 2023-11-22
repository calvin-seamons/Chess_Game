package Requests;



import chess.ChessGame;
import chess.ChessGame.TeamColor;

/**
 * JoinGameRequest object that has authToken, gameID, and team stored in it
 */
public class JoinGameRequest {
    private String authToken = null;
    private int gameID;
    private ChessGame.TeamColor playerColor = TeamColor.SPECTATOR;

    public JoinGameRequest() {}


    public String getAuthToken() {
        return authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getTeam() {
        return playerColor;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setTeam(String teamString) {
        if ("WHITE".equalsIgnoreCase(teamString)) {
            this.playerColor = ChessGame.TeamColor.WHITE;
        } else if ("BLACK".equalsIgnoreCase(teamString)) {
            this.playerColor = ChessGame.TeamColor.BLACK;
        } else {
            this.playerColor = ChessGame.TeamColor.SPECTATOR;
        }
    }
}
