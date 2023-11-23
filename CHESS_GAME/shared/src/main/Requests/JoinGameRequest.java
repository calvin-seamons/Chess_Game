package Requests;



import chess.ChessGame;
import chess.ChessGame.TeamColor;
import chess.ChessPiece;

/**
 * JoinGameRequest object that has authToken, gameID, and team stored in it
 */
public class JoinGameRequest {
    private String authToken = null;
    private int gameID;
    private ChessGame.TeamColor playerColor = null;

    public JoinGameRequest() {}

    public JoinGameRequest(int gameID){
        this.gameID = gameID;
    }

    public JoinGameRequest(String authToken, int gameID, String teamString) {
        this.authToken = authToken;
        this.gameID = gameID;
        setTeam(teamString);
    }


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
//        } else {
//            this.playerColor = ChessGame.TeamColor.SPECTATOR;
        }
    }
}
