package webSocketMessages.serverMessages;

import Models.Game;
import chess.Chess_Game;

public class LoadMessage extends ServerMessage{
    public final Game game;
    public String gameString;

    public LoadMessage(Game game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.gameString = game.getGameImplementation();
    }

    public LoadMessage(Game game, String gameString){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.gameString = gameString;
    }
}
