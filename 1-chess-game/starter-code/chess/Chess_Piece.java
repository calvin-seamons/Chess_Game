package chess;

import java.util.Collection;

public class Chess_Piece implements ChessPiece{
    private ChessPiece.PieceType PieceType;
    private ChessGame.TeamColor teamColor;
    private Chess_Position position;

    public Chess_Piece(ChessGame.TeamColor teamColor, ChessPiece.PieceType PieceType, Chess_Position position) {
        this.PieceType = PieceType;
        this.teamColor = teamColor;
        this.position = position;
    }
    /**
     * @return Which team this chess piece belongs to
     */
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return this.teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    @Override
    public PieceType getPieceType() {
        return this.PieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in danger
     *
     * @param board
     * @param myPosition
     * @return Collection of valid moves
     */
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        return null;
    }
}
