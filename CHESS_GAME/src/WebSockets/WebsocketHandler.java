package WebSockets;

import Models.Game;
import Requests.JoinGameRequest;
import chess.ChessGame;
import chess.ChessPosition;
import chess.Chess_Game;
import chess.Chess_Move;
import com.google.gson.*;
import com.google.protobuf.Internal;
import main.dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSockets.UserGameCommand;


import java.lang.reflect.Type;
import java.sql.Connection;


@WebSocket
public class WebsocketHandler {
    Connection connection;
    Session session;

    GameDAO gameDatabase;
    AuthDAO authDatabase;
    UserDAO userDatabase;
    Database db = new Database();
    Chess_Game game;
    public final ConnectionHandler connectionHandler = new ConnectionHandler();

    public WebsocketHandler(Connection connection, GameDAO gameDatabase, AuthDAO authDatabase, UserDAO userDatabase) {
        this.connection = connection;
        this.gameDatabase = gameDatabase;
        this.authDatabase = authDatabase;
        this.userDatabase = userDatabase;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException {
        System.out.println("Message received: " + message);
        connection = db.getConnection();
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
//        GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(ChessPosition.class, new ListAdapter());
//        Gson gson = builder.create();
        Gson gson = new Gson();
        switch (command.getCommandType(
        )) {
            case JOIN_PLAYER -> join(session, new Gson().fromJson(message, JoinPlayerCommand.class));
            case JOIN_OBSERVER -> observe(session, "hello");
            case MAKE_MOVE -> move(session, "gson.fromJson(message, String.class)");
            case LEAVE -> leave(session, "new Gson().fromJson(message, String.class)");
            case RESIGN -> resign(session, "new Gson().fromJson(message, String.class)");
        }
    }

    private void join(Session session, JoinPlayerCommand command) throws DataAccessException {
        // TODO: Implement logic here
        // Create notification message
        // Broadcast it to all the users in the specified game
        // Need to actually get the game
        System.out.println("Joining game websockets");
        Game Game = gameDatabase.getGame(command.getGameID(), db);
        // TODO: Do a type adapter for this to work
        ChessGame actualGame = gameDatabase.JSONToGame(Game.getGameImplementation());
    }

    private void observe(Session session, String joinObserver) throws DataAccessException {
        // TODO: Implement logic here
        System.out.println("Observing game websockets");
    }

    private void move(Session session, String move) throws DataAccessException {
        // TODO: Implement logic here
        System.out.println("Making move websockets");
    }

    private void leave(Session session, String leave) throws DataAccessException {
        // TODO: Implement logic here
        System.out.println("Leaving game websockets");
    }

    private void resign(Session session, String resign) throws DataAccessException {
        // TODO: Implement logic here
        System.out.println("Resigning game websockets");
    }

//     TODO: Probably will need to make a type adapter for ChessPosition
//    class ListAdapter implements com.google.gson.JsonDeserializer<ChessPosition> {
//        @Override
//        public ChessPosition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//            return jsonDeserializationContext.deserialize(jsonElement,ChessPosition.class);
//        }
//    }


}


