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

    public Chess_Game(Chess_Game game) {
        this.teamTurn = game.teamTurn;
        this.board = new Chess_Board((Chess_Board) game.board);
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
        boolean castleKingSideTried = false;
        boolean castleQueenSideTried = false;

        Collection<ChessMove> chessMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection<ChessMove> invalidChessMoves = new ArrayList<ChessMove>();

        if(board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING){
            if(startPosition.getColumn()<7) {
                if (chessMoves.contains(new Chess_Move(startPosition, new Chess_Position(startPosition.getRow(), startPosition.getColumn() + 2)))) {
                    castleKingSideTried = true;
                }
            }
            if(startPosition.getColumn()>2) {
                if (chessMoves.contains(new Chess_Move(startPosition, new Chess_Position(startPosition.getRow(), startPosition.getColumn() - 2)))) {
                    castleQueenSideTried = true;
                }
            }
        }

        for (ChessMove chessMove : chessMoves) {
            Chess_Game tempGame = new Chess_Game(this);
            tempGame.setBoard(new Chess_Board((Chess_Board) this.board));
            ChessGame.TeamColor tempTeamTurn = tempGame.board.getPiece(chessMove.getStartPosition()).getTeamColor();
            tempGame.applyMove(chessMove, true);
            if(tempGame.isInCheck(tempTeamTurn)){
                invalidChessMoves.add(chessMove); // Move results in check, add to invalid moves list
                System.out.println("Invalid move: " + chessMove.getEndPosition().getRow() + ", " + chessMove.getEndPosition().getColumn());
            }
        }
        chessMoves.removeAll(invalidChessMoves);
        this.board.getPiece(startPosition).setPosition(startPosition);
        // If a castle was tried, reset the position in the rook to the original position
        if(castleKingSideTried){
            ChessPosition rookStart = new Chess_Position(startPosition.getRow(), 8);
            this.board.getPiece(rookStart).setPosition(rookStart);
        }
        if(castleQueenSideTried){
            ChessPosition rookStart = new Chess_Position(startPosition.getRow(), 1);
            this.board.getPiece(rookStart).setPosition(rookStart);
        }


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
//        if(board.getPiece(move.getStartPosition()).getTeamColor() != board.getPiece(board.getLastMove().getEndPosition()).getTeamColor()){
//            throw new InvalidMoveException("Not your turn");
//        }
        if(board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn){
            throw new InvalidMoveException("Not your turn");
        }
        applyMove(move, false);
        this.board.setLastMove(move);
        if(teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    private void applyMove(ChessMove move, boolean tempGame) {
        // Check if move is a castle
        if(board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING && !board.getPiece(move.getStartPosition()).getHasMoved()){
            if(move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == 2){
                // Castle kingside
                ChessPosition rookStart = new Chess_Position(move.getStartPosition().getRow(), 8);
                ChessPosition rookEnd = new Chess_Position(move.getStartPosition().getRow(), 6);
                castleMove(move, rookStart, rookEnd, tempGame);
                return;

            } else if(move.getEndPosition().getColumn() - move.getStartPosition().getColumn() == -2){
                // Castle queenside
                ChessPosition rookStart = new Chess_Position(move.getStartPosition().getRow(), 1);
                ChessPosition rookEnd = new Chess_Position(move.getStartPosition().getRow(), 4);
                castleMove(move, rookStart, rookEnd, tempGame);
                return;
            }
        }

        // Check if move is an en passant
        if(board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN){
            if(move.getEndPosition().getColumn() != move.getStartPosition().getColumn()){
                if(board.getPiece(move.getEndPosition()) == null){
                    ChessPosition enPassantPosition;
                    if(board.getPiece(move.getStartPosition()).getTeamColor() == ChessGame.TeamColor.WHITE){
                        enPassantPosition = new Chess_Position(move.getEndPosition().getRow() - 1, move.getEndPosition().getColumn());
                    } else {
                        enPassantPosition = new Chess_Position(move.getEndPosition().getRow() + 1, move.getEndPosition().getColumn());
                    }
                    board.addPiece(enPassantPosition, null);
                }
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

        board.getPiece(move.getStartPosition()).setPosition(move.getEndPosition());
        if(!tempGame)
            board.getPiece(move.getStartPosition()).setHasMoved(true);
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.addPiece(move.getStartPosition(), null);
    }

    private void castleMove(ChessMove move, ChessPosition rookStart, ChessPosition rookEnd, boolean tempGame) {
        ChessMove rookMove = new Chess_Move(rookStart, rookEnd);
        board.addPiece(rookEnd, board.getPiece(rookStart));
        board.addPiece(rookStart, null);
        if(!tempGame)
            board.getPiece(rookEnd).setHasMoved(true);
        board.getPiece(move.getStartPosition()).setPosition(move.getEndPosition());
        if(!tempGame)
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
            Collection<ChessMove> validMoves = piece.pieceMoves(board, piece.getPosition());
            for (ChessMove move : validMoves) {
                if(board.getPiece(move.getEndPosition()) == null){
                    continue;
                }else if (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
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
        Collection<ChessMove> totalValidMoves = new ArrayList<>();
        ChessGame.TeamColor opponentColor;
        if(teamColor == ChessGame.TeamColor.WHITE){
            opponentColor = ChessGame.TeamColor.BLACK;
        } else {
            opponentColor = ChessGame.TeamColor.WHITE;
        }

        if(isInCheck(teamColor)){
            ArrayList<ChessPiece> teamPieces = (ArrayList<ChessPiece>) board.getOpponentPieces(opponentColor);
            for (ChessPiece piece : teamPieces) {
                totalValidMoves.addAll(validMoves(piece.getPosition()));
            }
            return totalValidMoves.isEmpty();
        }
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
        Collection<ChessMove> totalValidMoves = new ArrayList<>();
        ChessGame.TeamColor opponentColor;
        if(teamColor == ChessGame.TeamColor.WHITE){
            opponentColor = ChessGame.TeamColor.BLACK;
        } else {
            opponentColor = ChessGame.TeamColor.WHITE;
        }

        if(!isInCheck(teamColor)){
            ArrayList<ChessPiece> teamPieces = (ArrayList<ChessPiece>) board.getOpponentPieces(opponentColor);
            for (ChessPiece piece : teamPieces) {
                totalValidMoves.addAll(validMoves(piece.getPosition()));
            }
            return totalValidMoves.isEmpty();
        }
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
