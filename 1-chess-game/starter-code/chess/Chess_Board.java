package chess;

public class Chess_Board implements ChessBoard {
    private ChessPiece[][] board;

    public Chess_Board() {
        board = new ChessPiece[8][8];
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
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

