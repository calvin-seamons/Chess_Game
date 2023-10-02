package chess;

public class main {
    public static void main(String[] args) throws InvalidMoveException {
        ChessGame game = new Chess_Game();
        ChessBoard board = new Chess_Board();
        // System.out.println(board.getPiece(new Chess_Position(8, 3)).getPieceType());
        printBoard(board);
        game.validMoves(new Chess_Position(3, 1));
    }

    public static void printBoard(ChessBoard board) {
        for (int i = 0; i < 8; i++) {
            System.out.print(8-i + " ");
            for (int j = 0; j < 8; j++) {
                Chess_Piece piece = (Chess_Piece) board.getPiece(new Chess_Position(8-i, j + 1));
                if (piece == null) {
                    System.out.print("| ");
                } else {
                    System.out.print("|" + piece.getSymbol(piece.getPieceType()));
                }
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h\n");
    }
}
