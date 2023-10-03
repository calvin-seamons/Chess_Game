package chess;

import java.util.Collection;
import java.util.ArrayList;

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
        if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.PAWN)
            return pawnMoves(board, myPosition);
        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.ROOK)
            return rookMoves(board, myPosition);
        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KNIGHT)
            return knightMoves(board, myPosition);
//        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.BISHOP)
//            return bishopMoves(board, myPosition);
//        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.QUEEN)
//            return queenMoves(board, myPosition);
//        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KING)
//            return kingMoves(board, myPosition);
        else
            return null;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> chessMoves = new ArrayList<>();
        ChessGame.TeamColor sameTeam;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE)
            sameTeam = ChessGame.TeamColor.WHITE;
        else
            sameTeam = ChessGame.TeamColor.BLACK;


        if(myPosition.getRow() + 2 < 9) {
            if(myPosition.getColumn() + 1 < 9)
                if(board.getPiece(new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn() + 1)) == null)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn() + 1)));
                else if(board.getPiece(new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn() + 1)).getTeamColor() != sameTeam)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn() + 1)));
            if(myPosition.getColumn() - 1 > 0)
                if(board.getPiece(new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn() - 1)) == null)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn() - 1)));
                else if(board.getPiece(new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn() - 1)).getTeamColor() != sameTeam)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn() - 1)));
        }

        if(myPosition.getRow() - 2 > 0){
            if(myPosition.getColumn() + 1 < 9)
                if(board.getPiece(new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn() + 1)) == null)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn() + 1)));
                else if(board.getPiece(new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn() + 1)).getTeamColor() != sameTeam)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn() + 1)));
            if(myPosition.getColumn() - 1 > 0)
                if(board.getPiece(new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn() - 1)) == null)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn() - 1)));
                else if(board.getPiece(new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn() - 1)).getTeamColor() != sameTeam)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn() - 1)));
        }

        if(myPosition.getColumn() + 2 < 9){
            if(myPosition.getRow() + 1 < 9)
                if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + 2)) == null)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + 2)));
                else if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + 2)).getTeamColor() != sameTeam)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + 2)));
            if(myPosition.getRow() - 1 > 0)
                if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + 2)) == null)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + 2)));
                else if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + 2)).getTeamColor() != sameTeam)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + 2)));
        }

        if(myPosition.getColumn() - 2 > 0){
            if(myPosition.getRow() + 1 < 9)
                if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() - 2)) == null)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() - 2)));
                else if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() - 2)).getTeamColor() != sameTeam)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() - 2)));
            if(myPosition.getRow() - 1 > 0)
                if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() - 2)) == null)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() - 2)));
                else if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() - 2)).getTeamColor() != sameTeam)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() - 2)));
        }
        return chessMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        int counter = 1;
        ChessGame.TeamColor opponentTeam;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE)
            opponentTeam = ChessGame.TeamColor.BLACK;
        else
            opponentTeam = ChessGame.TeamColor.WHITE;
        // Check all the positions above the rook
        while(myPosition.getRow() + counter < 9) {
            if(board.getPiece(new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn())) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn())));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn())).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn())));
                counter = 1;
                break;
            }
            else {
                counter = 1;
                break;
            }
        }
        // Check all the positions to the right of the rook
        while(myPosition.getColumn() + counter < 9) {
            if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() + counter)) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() + counter)));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() + counter)).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() + counter)));
                counter = 1;
                break;
            }
            else {
                counter = 1;
                break;
            }
        }
        // Check all the positions below the rook
        while(myPosition.getRow() - counter > 1) {
            if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn())) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn())));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn())).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn())));
                counter = 1;
                break;
            }
            else {
                counter = 1;
                break;
            }
        }
        // Check all the positions to the left of the rook
        while(myPosition.getColumn() > 1) {
            if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() - counter)) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() - counter)));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() - counter)).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() - counter)));
                break;
            }
            else
                break;
        }
        return chessMoves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessMoves = new ArrayList<>();

        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if(myPosition.getRow() == 2) {
                if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn())) == null)
                    if(board.getPiece(new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn())) == null)
                        chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 2, myPosition.getColumn())));
            }
            if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn())) == null)
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn())));
        }
        else {
            if(myPosition.getRow() == 7) {
                if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn())) == null)
                    if(board.getPiece(new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn())) == null)
                        chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 2, myPosition.getColumn())));
            }
            if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn())) == null)
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn())));
        }
        //Check to see if there is a piece to capture
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + 1)).getTeamColor() == ChessGame.TeamColor.BLACK)
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + 1)));
            if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() - 1)).getTeamColor() == ChessGame.TeamColor.BLACK)
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() - 1)));
        }
        else {
            if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + 1)).getTeamColor() == ChessGame.TeamColor.WHITE)
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + 1)));
            if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() - 1)).getTeamColor() == ChessGame.TeamColor.WHITE)
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() - 1)));
        }
        return chessMoves;
    }

    public String getSymbol(ChessPiece.PieceType PieceType) {
        if (this.PieceType == ChessPiece.PieceType.PAWN)
            return "P";
        else if (this.PieceType == ChessPiece.PieceType.ROOK)
            return "R";
        else if (this.PieceType == ChessPiece.PieceType.KNIGHT)
            return "N";
        else if (this.PieceType == ChessPiece.PieceType.BISHOP)
            return "B";
        else if (this.PieceType == ChessPiece.PieceType.QUEEN)
            return "Q";
        else if (this.PieceType == ChessPiece.PieceType.KING)
            return "K";
        else
            return " ";
    }
}
