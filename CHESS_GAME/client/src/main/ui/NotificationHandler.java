package ui;

import chess.ChessBoard;

public interface NotificationHandler {
    void message(String message);
    void updateBoard(ChessBoard board);
    void error(String error);
}
