package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    public final ChessGame.TeamColor teamColor;

    public JoinPlayerCommand(int gameID, ChessGame.TeamColor teamColor, String authToken) {
        super(CommandType.JOIN_PLAYER, authToken, gameID);
        this.teamColor = teamColor;
    }
}
