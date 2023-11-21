package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Chess_Board implements ChessBoard {
    private final ChessPiece[][] board;
    private ChessMove lastMove;

    public Chess_Board() {
        board = new ChessPiece[8][8];
    }

    public Chess_Board(Chess_Board board) {
        this.board = new ChessPiece[8][8];
        for(int i = 0; i < 8; i++){
            System.arraycopy(board.board[i], 0, this.board[i], 0, 8);
        }
        // TODO: This could be a problem
        this.lastMove = board.lastMove;
    }

    // NOT IN OG INTERFACE
    public void setLastMove(ChessMove lastMove) {
        this.lastMove = lastMove;
    }

    // NOT IN OG INTERFACE
    public ChessMove getLastMove() {
        return lastMove;
    }



    // NOT IN OG INTERFACE
    public Collection<ChessPiece> getOpponentPieces(ChessGame.TeamColor teamColor){
        Collection<ChessPiece> opponentPieces = new ArrayList<ChessPiece>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null && board[i][j].getTeamColor() != teamColor){
                    opponentPieces.add(board[i][j]);
                }
            }
        }
        return opponentPieces;
    }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if(piece == null) {
            board[position.getRow() - 1][position.getColumn() - 1] = null;
            return;
        }
        piece.setPosition(position);
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that position
     */
    @Override
    public ChessPiece getPiece(ChessPosition position) {
        if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8) {
            throw new IllegalArgumentException("Invalid position");
        }
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    @Override
    public void resetBoard() {
        // Clear the board first
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        // Set white pieces
        board[0][0] = new Chess_Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK, new Chess_Position(1, 1));
        board[0][1] = new Chess_Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT, new Chess_Position(1, 2));
        board[0][2] = new Chess_Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP, new Chess_Position(1, 3));
        board[0][3] = new Chess_Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN, new Chess_Position(1, 4));
        board[0][4] = new Chess_Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING, new Chess_Position(1, 5));
        board[0][5] = new Chess_Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP, new Chess_Position(1, 6));
        board[0][6] = new Chess_Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT, new Chess_Position(1, 7));
        board[0][7] = new Chess_Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK, new Chess_Position(1, 8));

        for (int j = 0; j < 8; j++) {
            board[1][j] = new Chess_Piece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN, new Chess_Position(2, j + 1));
        }

        // Set black pieces
        board[7][0] = new Chess_Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK, new Chess_Position(8, 1));
        board[7][1] = new Chess_Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT, new Chess_Position(8, 2));
        board[7][2] = new Chess_Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP, new Chess_Position(8, 3));
        board[7][3] = new Chess_Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN, new Chess_Position(8, 4));
        board[7][4] = new Chess_Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING, new Chess_Position(8, 5));
        board[7][5] = new Chess_Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP, new Chess_Position(8, 6));
        board[7][6] = new Chess_Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT, new Chess_Position(8, 7));
        board[7][7] = new Chess_Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK, new Chess_Position(8, 8));

        for (int j = 0; j < 8; j++) {
            board[6][j] = new Chess_Piece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN, new Chess_Position(7, j + 1));
        }
    }
}

