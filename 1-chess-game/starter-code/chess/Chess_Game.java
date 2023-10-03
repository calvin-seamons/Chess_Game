package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Chess_Game implements ChessGame{
    private TeamColor teamTurn;
    private ChessBoard board;

    public Chess_Game() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new Chess_Board();
    }

    /**
     * @return Which team's turn it is
     */
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    @Override
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at startPosition
     */
    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(board.getPiece(startPosition) == null){
            return null;
        }

        Collection<ChessMove> chessMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);

        // TODO Loop through all the moves and remove any that would put the team in check
//        for (ChessMove chessMove : chessMoves) {
//            ChessBoard realBoard = this.board;
//            ChessBoard tempBoard = this.board;
//            this.setBoard(tempBoard);
//            try {
//                makeMove(chessMove);
//            } catch (InvalidMoveException e) {
//                e.printStackTrace();
//            }
//            if(isInCheck(board.getPiece(startPosition).getTeamColor())){
//                chessMoves.remove(chessMove);
//            }
//            this.setBoard(realBoard);
//        }

        // Print out all the moves
        System.out.println("Valid moves for piece at row: " + startPosition.getRow() + ", column: " + startPosition.getColumn());
        for (ChessMove chessMove : chessMoves) {
            System.out.println("Row: " + chessMove.getEndPosition().getRow() + ", Column: " +chessMove.getEndPosition().getColumn());
        }
        return chessMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException("No piece at start position");
        }
        if(!validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException("Invalid move");
        }

        // TODO Check if move is a castle
        if(board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING && !board.getPiece(move.getStartPosition()).getHasMoved()){
            if(move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == 2){
                // Castle kingside
                ChessPosition rookStart = new Chess_Position(move.getStartPosition().getRow(), 8);
                ChessPosition rookEnd = new Chess_Position(move.getStartPosition().getRow(), 6);
                castleMove(move, rookStart, rookEnd);

            } else if(move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == -2){
                // Castle queenside
                ChessPosition rookStart = new Chess_Position(move.getStartPosition().getRow(), 1);
                ChessPosition rookEnd = new Chess_Position(move.getStartPosition().getRow(), 4);
                castleMove(move, rookStart, rookEnd);
            }
        }

        if(board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN){
            if(move.getPromotionPiece() != null){
                board.addPiece(move.getEndPosition(), new Chess_Piece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece(), move.getEndPosition()));
                board.addPiece(move.getStartPosition(), null);
                board.setLastMove(move);
                return;
            }
        }

        // Make normal move
        board.getPiece(move.getStartPosition()).setPosition(move.getEndPosition());
        board.getPiece(move.getStartPosition()).setHasMoved(true);
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.addPiece(move.getStartPosition(), null);

        this.board.setLastMove(move);
    }

    private void castleMove(ChessMove move, ChessPosition rookStart, ChessPosition rookEnd) {
        ChessMove rookMove = new Chess_Move(rookStart, rookEnd);
        board.addPiece(rookEnd, board.getPiece(rookStart));
        board.addPiece(rookStart, null);
        board.getPiece(rookEnd).setHasMoved(true);
        board.getPiece(move.getStartPosition()).setPosition(move.getEndPosition());
        board.getPiece(move.getStartPosition()).setHasMoved(true);
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    @Override
    public boolean isInCheck(TeamColor teamColor) {
        ArrayList<ChessPiece> opponentPieces = (ArrayList<ChessPiece>) board.getOpponentPieces(teamColor);
        for (ChessPiece piece : opponentPieces) {
            Collection<ChessMove> validMoves = validMoves(piece.getPosition());
            for (ChessMove move : validMoves) {
                if (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
