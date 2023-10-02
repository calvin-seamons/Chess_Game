package chess;

public class Chess_Move implements ChessMove{
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public Chess_Move(ChessPosition startPosition, ChessPosition endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = null;
    }

    // Constructor for pawn promotion
    public Chess_Move(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }
    /**
     * @return ChessPosition of starting location
     */
    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }
}
