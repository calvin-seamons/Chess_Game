package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

public class Chess_Piece implements ChessPiece{
    private final ChessPiece.PieceType PieceType;
    private final ChessGame.TeamColor teamColor;
    private Chess_Position position;

    private boolean hasMoved = false;

    public Chess_Piece(ChessGame.TeamColor teamColor, ChessPiece.PieceType PieceType, Chess_Position position) {
        this.PieceType = PieceType;
        this.teamColor = teamColor;
        this.position = position;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    @Override
    public void setPosition(ChessPosition endPosition) {

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
     * @return The position of this piece on the board
     */
    @Override
    public ChessPosition getPosition() {
        return this.position;
    }

    public void setPosition(Chess_Position position) {
        this.position = position;
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
            return pawnMoves(board, myPosition, board.getLastMove());
        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.ROOK)
            return rookMoves(board, myPosition);
        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KNIGHT)
            return knightMoves(board, myPosition);
        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.BISHOP)
            return bishopMoves(board, myPosition);
        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.QUEEN)
            return queenMoves(board, myPosition);
        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KING)
            return kingMoves(board, myPosition);
        else
            return null;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        ChessGame.TeamColor opponentTeam;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE)
            opponentTeam = ChessGame.TeamColor.BLACK;
        else
            opponentTeam = ChessGame.TeamColor.WHITE;

        // Check all the positions above the king
        if(myPosition.getRow() + 1 < 9) {
            for(int x=-1; x<2; x++){
                if(myPosition.getColumn() + x > 0 && myPosition.getColumn() + x < 9) {
                    if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + x)) == null)
                        chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + x)));
                    else if(board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + x)).getTeamColor() == opponentTeam)
                        chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn() + x)));
                }
            }
        }

        // Check all the positions below the king
        if(myPosition.getRow() - 1 > 0) {
            for(int x=-1; x<2; x++){
                if(myPosition.getColumn() + x > 0 && myPosition.getColumn() + x < 9) {
                    if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + x)) == null)
                        chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + x)));
                    else if(board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + x)).getTeamColor() == opponentTeam)
                        chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn() + x)));
                }
            }
        }

        // Check all the positions to the left of the king
        if(myPosition.getColumn() - 1 > 0) {
            if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() - 1)) == null)
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() - 1)));
            else if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() - 1)).getTeamColor() == opponentTeam)
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() - 1)));
        }

        // Check all the positions to the right of the king
        if(myPosition.getColumn() + 1 < 9) {
            if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() + 1)) == null)
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() + 1)));
            else if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() + 1)).getTeamColor() == opponentTeam)
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() + 1)));
        }

        // Check if the king can castle
        // TODO Make sure that all the positions between the king and rook are not in check
        if(!board.getPiece(myPosition).getHasMoved()){
            // Check if the king can castle kingside
            if(board.getPiece(new Chess_Position(myPosition.getRow(), 8)) != null && !board.getPiece(new Chess_Position(myPosition.getRow(), 8)).getHasMoved()){
                if(board.getPiece(new Chess_Position(myPosition.getRow(), 7)) == null && board.getPiece(new Chess_Position(myPosition.getRow(), 6)) == null){
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), 7)));
                }
            }
            // Check if the king can castle queenside
            if(board.getPiece(new Chess_Position(myPosition.getRow(), 1)) != null && !board.getPiece(new Chess_Position(myPosition.getRow(), 1)).getHasMoved()){
                if(board.getPiece(new Chess_Position(myPosition.getRow(), 2)) == null && board.getPiece(new Chess_Position(myPosition.getRow(), 3)) == null && board.getPiece(new Chess_Position(myPosition.getRow(), 4)) == null){
                    chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), 3)));
                }
            }
        }

        return chessMoves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        chessMoves.addAll(rookMoves(board, myPosition));
        chessMoves.addAll(bishopMoves(board, myPosition));
        return chessMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> chessMoves = new ArrayList<>();
        int counter = 1;
        ChessGame.TeamColor opponentTeam;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE)
            opponentTeam = ChessGame.TeamColor.BLACK;
        else
            opponentTeam = ChessGame.TeamColor.WHITE;

        // Check all the positions above and to the right of the bishop
        while(myPosition.getRow() + counter < 9 && myPosition.getColumn() + counter < 9) {
            if(board.getPiece(new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn() + counter)) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn() + counter)));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn() + counter)).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn() + counter)));
                counter = 1;
                break;
            }
            else {
                counter = 1;
                break;
            }
        }

        // Check all the positions below and to the right of the bishop
        while(myPosition.getRow() - counter > 0 && myPosition.getColumn() + counter < 9) {
            if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() + counter)) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() + counter)));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() + counter)).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() + counter)));
                counter = 1;
                break;
            }
            else {
                counter = 1;
                break;
            }
        }

        // Check all the positions below and to the left of the bishop
        while(myPosition.getRow() - counter > 0 && myPosition.getColumn() - counter > 0) {
            if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() - counter)) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() - counter)));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() - counter)).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() - counter)));
                counter = 1;
                break;
            }
            else {
                counter = 1;
                break;
            }
        }

        // Check all the positions above and to the left of the bishop
        while(myPosition.getRow() + counter < 9 && myPosition.getColumn() - counter > 0) {
            if(board.getPiece(new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn() - counter)) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn() - counter)));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn() - counter)).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + counter, myPosition.getColumn() - counter)));
                break;
            }
            else {
                break;
            }
        }

        return chessMoves;
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

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessMove lastMove) {
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

        // Check to see if pawn can be promoted
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            if(myPosition.getRow() == 7 && board.getPiece(new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn())) == null){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
            }
        }
        else{
            if(myPosition.getRow() == 2 && board.getPiece(new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn())) == null){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
            }
        }

        // TODO Check to see if pawn can be captured en passant
        // Not sure if this works
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            if(Objects.equals(lastMove.getStartPosition(), new Chess_Position(7, myPosition.getColumn() + 1)) && Objects.equals(lastMove.getEndPosition(), new Chess_Position(5, myPosition.getColumn() + 1))){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(6, myPosition.getColumn() + 1)));
            } else if (Objects.equals(lastMove.getStartPosition(), new Chess_Position(7, myPosition.getColumn() - 1)) && Objects.equals(lastMove.getEndPosition(), new Chess_Position(5, myPosition.getColumn() - 1))){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(6, myPosition.getColumn() - 1)));
            }

        } else{
            if(Objects.equals(lastMove.getStartPosition(), new Chess_Position(2, myPosition.getColumn() + 1)) && Objects.equals(lastMove.getEndPosition(), new Chess_Position(4, myPosition.getColumn() + 1))){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(3, myPosition.getColumn() + 1)));
            } else if (Objects.equals(lastMove.getStartPosition(), new Chess_Position(2, myPosition.getColumn() - 1)) && Objects.equals(lastMove.getEndPosition(), new Chess_Position(4, myPosition.getColumn() - 1))){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(3, myPosition.getColumn() - 1)));
            }
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
