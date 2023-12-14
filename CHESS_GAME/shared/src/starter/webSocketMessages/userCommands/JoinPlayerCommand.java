package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    public final String playerColor;

    public JoinPlayerCommand(int gameID, ChessGame.TeamColor teamColor, String authToken) {
        super(CommandType.JOIN_PLAYER, authToken, gameID);
        this.playerColor = teamColor.toString();
    }

    public ChessGame.TeamColor getPlayerColor() {
        if(playerColor.equals("WHITE")){
            return ChessGame.TeamColor.WHITE;
        } else if(playerColor.equals("BLACK")) {
            return ChessGame.TeamColor.BLACK;
        }else{
            return null;
        }
    }
}
