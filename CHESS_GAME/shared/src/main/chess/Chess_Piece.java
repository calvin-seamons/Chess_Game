package chess;

import java.util.Collection;
import java.util.ArrayList;

public class Chess_Piece implements ChessPiece{
    private final ChessPiece.PieceType PieceType;
    private final ChessGame.TeamColor teamColor;
    private ChessPosition position;

    private boolean hasMoved = false;

    public Chess_Piece(ChessGame.TeamColor teamColor, ChessPiece.PieceType PieceType, ChessPosition position) {
        this.PieceType = PieceType;
        this.teamColor = teamColor;
        this.position = position;
    }

    public Chess_Piece(ChessGame.TeamColor teamColor, ChessPiece.PieceType PieceType) {
        this.PieceType = PieceType;
        this.teamColor = teamColor;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    @Override
    public void setPosition(ChessPosition endPosition) {
        this.position = endPosition;
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
        if(board.getPiece(myPosition) == null)
            return null;

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
        else if(board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.KING) {
//            board.setOpponentValidEndPositions(board.getPiece(myPosition).getTeamColor());
            return kingMoves(board, myPosition, false);
        }
        else
            return null;
    }

    @Override
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, boolean tempTest) {
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
        if(board.getPiece(myPosition).getHasMoved() || board.getPiece(myPosition).getPosition().getColumn() != 5)
            return chessMoves;

        if(!tempTest) {
            // Create duplicate game and board and move the king along the castling path and see if he is in check
            ChessBoard tempBoard = board;
            ChessGame tempGame = new Chess_Game();
            tempGame.setBoard(tempBoard);
            Collection<ChessPiece> opponentPieces = new ArrayList<>();
            opponentPieces = tempGame.getBoard().getOpponentPieces(board.getPiece(myPosition).getTeamColor());
            boolean canCastleKing = true;
            boolean canCastleQueen = true;

            for (ChessPiece piece : opponentPieces) {

                // If the piece is a king, run the kingMoves method to get the valid moves
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if(piece.kingMoves(tempBoard, piece.getPosition(), true).contains(new Chess_Move(piece.getPosition(), new Chess_Position(myPosition.getRow(), 6))) || piece.kingMoves(tempBoard, piece.getPosition(), true).contains(new Chess_Move(piece.getPosition(), new Chess_Position(myPosition.getRow(), 7)))) {
                        canCastleKing = false;
                    }
                    if(piece.kingMoves(tempBoard, piece.getPosition(), true).contains(new Chess_Move(piece.getPosition(), new Chess_Position(myPosition.getRow(), 4))) || piece.kingMoves(tempBoard, piece.getPosition(), true).contains(new Chess_Move(piece.getPosition(), new Chess_Position(myPosition.getRow(), 3))))
                        canCastleQueen = false;

                    continue;
                }

                if (piece.pieceMoves(tempBoard, piece.getPosition()).contains(new Chess_Move(piece.getPosition(), new Chess_Position(myPosition.getRow(), 6))) || piece.pieceMoves(tempBoard, piece.getPosition()).contains(new Chess_Move(piece.getPosition(), new Chess_Position(myPosition.getRow(), 7)))) {
                    canCastleKing = false;
                }

                if (piece.pieceMoves(tempBoard, piece.getPosition()).contains(new Chess_Move(piece.getPosition(), new Chess_Position(myPosition.getRow(), 4))) || piece.pieceMoves(tempBoard, piece.getPosition()).contains(new Chess_Move(piece.getPosition(), new Chess_Position(myPosition.getRow(), 3)))) {
                    canCastleQueen = false;
                }
                // Check to see if the king is in check
                if (piece.pieceMoves(tempBoard, piece.getPosition()).contains(new Chess_Move(piece.getPosition(), myPosition))) {
                    return chessMoves;
                }
            }

            // If the booleans are still true, add the castling moves to the list of valid moves
            if (canCastleKing) {
                // Make sure the spaces are null between the king and the rook
                if(board.getPiece(new Chess_Position(myPosition.getRow(), 6)) == null && board.getPiece(new Chess_Position(myPosition.getRow(), 7)) == null)
                    // Make sure that the rook hasn't moved on that side
                    if(board.getPiece(new Chess_Position(myPosition.getRow(), 8)) != null && !board.getPiece(new Chess_Position(myPosition.getRow(), 8)).getHasMoved())
                        chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), 7)));
//                    else{
//                        System.out.println("Rook has moved");
//                    }
            }
            if (canCastleQueen) {
                // Same thing here
                if(board.getPiece(new Chess_Position(myPosition.getRow(), 4)) == null && board.getPiece(new Chess_Position(myPosition.getRow(), 3)) == null)
                    if(board.getPiece(new Chess_Position(myPosition.getRow(), 1)) != null && !board.getPiece(new Chess_Position(myPosition.getRow(), 1)).getHasMoved())
                        chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), 3)));
//                    else{
//                        System.out.println("Rook has moved");
//                    }
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
                break;
            }
            else {
                break;
            }
        }

        // Check all the positions below and to the right of the bishop
        counter = 1;
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
        counter = 1;
        while(myPosition.getRow() - counter > 0 && myPosition.getColumn() - counter > 0) {
            if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() - counter)) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() - counter)));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() - counter)).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn() - counter)));
                break;
            }
            else {
                break;
            }
        }

        // Check all the positions above and to the left of the bishop
        counter = 1;
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
                break;
            }
            else {
                break;
            }
        }
        // Check all the positions to the right of the rook
        counter = 1;
        while(myPosition.getColumn() + counter < 9) {
            if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() + counter)) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() + counter)));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow(), myPosition.getColumn() + counter)).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow(), myPosition.getColumn() + counter)));
                break;
            }
            else {
                break;
            }
        }
        // Check all the positions below the rook
        counter = 1;
        while(myPosition.getRow() - counter > 0) {
            if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn())) == null) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn())));
                counter++;
            }
            else if(board.getPiece(new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn())).getTeamColor() == opponentTeam){
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() - counter, myPosition.getColumn())));
                break;
            }
            else {
                break;
            }
        }
        // Check all the positions to the left of the rook
        counter = 1;
        while(myPosition.getColumn() - counter > 0) {
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

        if(myPosition.getColumn() == 1 || myPosition.getColumn() == 8){
            edgePawnMoves(board, myPosition, chessMoves, lastMove);
        }
        else {
            centerPawnMoves(board, myPosition, chessMoves);
            enPassantPawnMoves(board, myPosition, chessMoves, lastMove);
            promotionPawnMoves(board, myPosition, chessMoves);
        }

        return chessMoves;
    }

    private void promotionPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> chessMoves) {
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
    }

    private void enPassantPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> chessMoves, ChessMove lastMove) {
        if(board.getLastMove() == null)
            return;
        boolean rightEdge = true;
        boolean leftEdge = true;
        if(myPosition.getColumn() != 8)
            rightEdge = false;
        if(myPosition.getColumn() != 1)
            leftEdge = false;

        int forwardDirection = (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;

        if (lastMove != null) {
            ChessPiece movedPiece = board.getPiece(lastMove.getEndPosition());
            if (movedPiece != null
                    && movedPiece.getPieceType() == ChessPiece.PieceType.PAWN
                    && movedPiece.getHasMoved()
                    && Math.abs(lastMove.getEndPosition().getRow() - lastMove.getStartPosition().getRow()) == 2) {

                // Check for left edge pawn trying to capture a pawn on its right
                if (!rightEdge && lastMove.getEndPosition().getColumn() == myPosition.getColumn() + 1
                        && lastMove.getEndPosition().getRow() == myPosition.getRow()) {
                    ChessPosition capturePosition = new Chess_Position(myPosition.getRow() + forwardDirection, myPosition.getColumn() + 1);
                    chessMoves.add(new Chess_Move(myPosition, capturePosition));
                }

                // Check for right edge pawn trying to capture a pawn on its left
                if (!leftEdge && lastMove.getEndPosition().getColumn() == myPosition.getColumn() - 1
                        && lastMove.getEndPosition().getRow() == myPosition.getRow()) {
                    ChessPosition capturePosition = new Chess_Position(myPosition.getRow() + forwardDirection, myPosition.getColumn() - 1);
                    chessMoves.add(new Chess_Move(myPosition, capturePosition));
                }
            }
        }
    }

    private void centerPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> chessMoves) {
        if(myPosition.getRow() == 1 || myPosition.getRow() == 8)
            return;
        boolean edgy = false;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE && (myPosition.getRow() == 7))
            edgy = true;
        else if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK && (myPosition.getRow() == 2))
            edgy = true;

        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int forwardDirection = (myColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        ChessPosition oneSquareAhead = new Chess_Position(myPosition.getRow() + forwardDirection, myPosition.getColumn());

        if (board.getPiece(oneSquareAhead) == null) {
            if(edgy){
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.QUEEN));
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.ROOK));
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.BISHOP));
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.KNIGHT));
            }
            else{
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead));
            }
            if (!edgy && myColor == ChessGame.TeamColor.WHITE && board.getPiece(new Chess_Position(myPosition.getRow() + 2 * forwardDirection, myPosition.getColumn())) == null && myPosition.getRow() == 2) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 2 * forwardDirection, myPosition.getColumn())));
            }
            if (!edgy && myColor == ChessGame.TeamColor.BLACK && board.getPiece(new Chess_Position(myPosition.getRow() + 2 * forwardDirection, myPosition.getColumn())) == null && myPosition.getRow() == 7) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 2 * forwardDirection, myPosition.getColumn())));
            }
        }

        ChessPosition capturePosition = new Chess_Position(myPosition.getRow() + forwardDirection, myPosition.getColumn() + 1); // Diagonal forward-right
        pawnCapture(board, myPosition, chessMoves, myColor, capturePosition);
        ChessPosition capturePosition2 = new Chess_Position(myPosition.getRow() + forwardDirection, myPosition.getColumn() - 1); // Diagonal forward-left
        pawnCapture(board, myPosition, chessMoves, myColor, capturePosition2);
    }

    private void edgePawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> chessMoves, ChessMove lastMove) {
        boolean rightEdge = true;
        boolean leftEdge = true;
        if(myPosition.getColumn() != 8)
            rightEdge = false;
        if(myPosition.getColumn() != 1)
            leftEdge = false;

        // If pawn is on the edge and about to promote itself
        boolean edgy = false;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE && (myPosition.getRow() == 7))
            edgy = true;
        else if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK && (myPosition.getRow() == 2))
            edgy = true;

        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        int forwardDirection = (myColor == ChessGame.TeamColor.WHITE) ? 1 : -1; // Depending on the color, pawns will move up or down the board.

        ChessPosition oneSquareAhead = new Chess_Position(myPosition.getRow() + forwardDirection, myPosition.getColumn());

        // Move Forward by One Square
        if (board.getPiece(oneSquareAhead) == null) {
            if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7){
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.QUEEN));
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.ROOK));
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.BISHOP));
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.KNIGHT));
            }
            else if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2){
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.QUEEN));
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.ROOK));
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.BISHOP));
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead, ChessPiece.PieceType.KNIGHT));
            }
            else
                chessMoves.add(new Chess_Move(myPosition, oneSquareAhead));

            // Move Forward by Two Squares (only if it's pawn's first move and the two squares ahead are empty)
            if (!edgy && myColor == ChessGame.TeamColor.WHITE && board.getPiece(new Chess_Position(myPosition.getRow() + 2 * forwardDirection, myPosition.getColumn())) == null && myPosition.getRow() == 2) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 2 * forwardDirection, myPosition.getColumn())));
            }
            if (!edgy && myColor == ChessGame.TeamColor.BLACK && board.getPiece(new Chess_Position(myPosition.getRow() + 2 * forwardDirection, myPosition.getColumn())) == null && myPosition.getRow() == 7) {
                chessMoves.add(new Chess_Move(myPosition, new Chess_Position(myPosition.getRow() + 2 * forwardDirection, myPosition.getColumn())));
            }
        }

        // Capturing Logic
        if (leftEdge) {
            ChessPosition capturePosition = new Chess_Position(myPosition.getRow() + forwardDirection, myPosition.getColumn() + 1); // Diagonal forward-right
            pawnCapture(board, myPosition, chessMoves, myColor, capturePosition);
        } else if (rightEdge) {
            ChessPosition capturePosition = new Chess_Position(myPosition.getRow() + forwardDirection, myPosition.getColumn() - 1); // Diagonal forward-left
            pawnCapture(board, myPosition, chessMoves, myColor, capturePosition);
        }

        enPassantPawnMoves(board, myPosition, chessMoves, lastMove);
    }

    private void pawnCapture(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> chessMoves, ChessGame.TeamColor myColor, ChessPosition capturePosition) {
        if (board.getPiece(capturePosition) != null && board.getPiece(capturePosition).getTeamColor() != myColor) {
            if(board.getPiece(capturePosition).getPosition().getRow() == 8 || board.getPiece(capturePosition).getPosition().getRow() == 1) {
                chessMoves.add(new Chess_Move(myPosition, capturePosition, ChessPiece.PieceType.QUEEN));
                chessMoves.add(new Chess_Move(myPosition, capturePosition, ChessPiece.PieceType.ROOK));
                chessMoves.add(new Chess_Move(myPosition, capturePosition, ChessPiece.PieceType.BISHOP));
                chessMoves.add(new Chess_Move(myPosition, capturePosition, ChessPiece.PieceType.KNIGHT));
            }
            else
                chessMoves.add(new Chess_Move(myPosition, capturePosition));
        }
    }


    public String getSymbol(ChessPiece.PieceType PieceType, ChessGame.TeamColor teamColor) {
        if (this.PieceType == ChessPiece.PieceType.PAWN) {
            if (teamColor == ChessGame.TeamColor.BLACK)
                return OtherEscapeSequences.BLACK_PAWN;
            else
                return OtherEscapeSequences.WHITE_PAWN;
        } else if (this.PieceType == ChessPiece.PieceType.ROOK) {
            if (teamColor == ChessGame.TeamColor.BLACK)
                return OtherEscapeSequences.BLACK_ROOK;
            else
                return OtherEscapeSequences.WHITE_ROOK;
        } else if (this.PieceType == ChessPiece.PieceType.KNIGHT) {
            if (teamColor == ChessGame.TeamColor.BLACK)
                return OtherEscapeSequences.BLACK_KNIGHT;
            else
                return OtherEscapeSequences.WHITE_KNIGHT;
        } else if (this.PieceType == ChessPiece.PieceType.BISHOP) {
            if (teamColor == ChessGame.TeamColor.BLACK)
                return OtherEscapeSequences.BLACK_BISHOP;
            else
                return OtherEscapeSequences.WHITE_BISHOP;
        } else if (this.PieceType == ChessPiece.PieceType.QUEEN) {
            if (teamColor == ChessGame.TeamColor.BLACK)
                return OtherEscapeSequences.BLACK_QUEEN;
            else
                return OtherEscapeSequences.WHITE_QUEEN;
        } else if (this.PieceType == ChessPiece.PieceType.KING) {
            if (teamColor == ChessGame.TeamColor.BLACK)
                return OtherEscapeSequences.BLACK_KING;
            else
                return OtherEscapeSequences.WHITE_KING;
        } else
            return " ";
    }
}
