package webSocketMessages.userCommands;

import chess.*;

public class MakeMoveCommand extends UserGameCommand{
    ChessMove move;

//    ChessGame.TeamColor playerColor;

    String gameImplementation;

    public MakeMoveCommand(int row, int col, int newRow, int newCol, String authToken, int gameID, String gameImplementation) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = new Chess_Move(new Chess_Position(row, col), new Chess_Position(newRow, newCol));
        this.gameImplementation = gameImplementation;
//        this.playerColor = playerColor;
    }

    public ChessMove getMove() {
        return move;
    }

    public String getGameImplementation() {
        return gameImplementation;
    }


}
