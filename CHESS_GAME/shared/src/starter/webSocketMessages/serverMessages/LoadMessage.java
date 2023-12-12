package webSocketMessages.serverMessages;

import chess.Chess_Game;

public class LoadMessage extends ServerMessage{
    public final Chess_Game game;

    public LoadMessage(Chess_Game game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
}
