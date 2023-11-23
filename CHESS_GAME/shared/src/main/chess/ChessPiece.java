package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 */
public interface ChessPiece {
    // NOT IN OG INTERFACE
    void setPosition(ChessPosition endPosition);

    //NOT IN OG INTERFACE
    boolean getHasMoved();

    //NOT IN OG INTERFACE
    void setHasMoved(boolean hasMoved);

    Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, boolean tempTest);

    /**
     * The various different chess piece options
     */
    enum PieceType{
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN;
    }

    enum Team{
        WHITE,
        BLACK,
        SPECTATOR
    }

    /**
     * @return Which team this chess piece belongs to
     */
    ChessGame.TeamColor getTeamColor();

    /**
     * @return which type of chess piece this piece is
     */
    PieceType getPieceType();

    // NOT PART OF THE OG INTERFACE
    ChessPosition getPosition();

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in danger
     * @return Collection of valid moves
     */
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
