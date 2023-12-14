package webSocketMessages.userCommands;

import chess.ChessGame;

public class LeaveCommand extends UserGameCommand{
    String whiteOrBlack;

    public LeaveCommand(String authToken, int gameID, ChessGame.TeamColor teamColor) {
        super(CommandType.LEAVE, authToken, gameID);
        this.whiteOrBlack = teamColor.toString();
    }

    public String getWhiteOrBlack() {
        return whiteOrBlack;
    }
}
