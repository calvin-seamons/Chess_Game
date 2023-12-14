package webSocketMessages.userCommands;

import chess.ChessMove;
import chess.ChessPosition;
import chess.Chess_Move;
import chess.Chess_Position;

public class MakeMoveCommand extends UserGameCommand{
    int row;
    int col;
    int newRow;
    int newCol;

    public MakeMoveCommand(int row, int col, int newRow, int newCol, String authToken, int gameID) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.row = row;
        this.col = col;
        this.newRow = newRow;
        this.newCol = newCol;
    }

    public ChessMove getMove() {
        ChessPosition position = new Chess_Position(row, col);
        ChessPosition newPosition = new Chess_Position(newRow, newCol);

        return new Chess_Move(position, newPosition);
    }

}
