package ui;

import chess.ChessBoard;
import chess.Chess_Board;
import webSocketMessages.serverMessages.Notification;

public interface NotificationHandler {
    void message(String message);

    void updateBoard(chess.Chess_Game game);

    void error(String message);
}
