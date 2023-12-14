package webSocketMessages.userCommands;

import chess.*;

public class MakeMoveCommand extends UserGameCommand{
    int row;
    int col;
    int newRow;
    int newCol;

    ChessGame.TeamColor teamColor;

    String gameImplementation;

    public MakeMoveCommand(int row, int col, int newRow, int newCol, String authToken, int gameID, String gameImplementation, ChessGame.TeamColor teamColor) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.row = row;
        this.col = col;
        this.newRow = newRow;
        this.newCol = newCol;
        this.gameImplementation = gameImplementation;
        this.teamColor = teamColor;
    }

    public ChessMove getMove() {
        ChessPosition position = new Chess_Position(row, col);
        ChessPosition newPosition = new Chess_Position(newRow, newCol);

        return new Chess_Move(position, newPosition);
    }

    public String getGameImplementation() {
        return gameImplementation;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

}
