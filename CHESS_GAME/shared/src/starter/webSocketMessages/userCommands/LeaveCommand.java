package webSocketMessages.userCommands;

import chess.ChessGame;

public class LeaveCommand extends UserGameCommand{
    String whiteOrBlack;

    public LeaveCommand(String authToken, int gameID) {
        super(CommandType.LEAVE, authToken, gameID);
    }

    public String getWhiteOrBlack() {
        return whiteOrBlack;
    }
}
