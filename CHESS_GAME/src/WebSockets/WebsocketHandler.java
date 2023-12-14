package WebSockets;

import Models.Game;
import chess.*;
import com.google.gson.*;
import main.dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.userCommands.*;
import webSockets.UserGameCommand;


import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;


@WebSocket
public class WebsocketHandler {
    Connection connection;
    Session session;

    GameDAO gameDatabase;
    AuthDAO authDatabase;
    UserDAO userDatabase;
    Database db = new Database();
    Chess_Game game;
    Gson gson = new Gson();
    public final ConnectionHandler connectionHandler = new ConnectionHandler();

    public WebsocketHandler(Connection connection, GameDAO gameDatabase, AuthDAO authDatabase, UserDAO userDatabase) {
        this.connection = connection;
        this.gameDatabase = gameDatabase;
        this.authDatabase = authDatabase;
        this.userDatabase = userDatabase;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException {
        System.out.println("Message received: " + message);
        connection = db.getConnection();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
//        GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(ChessPosition.class, new ListAdapter());
//        Gson gson = builder.create();
        switch (command.getCommandType(
        )) {
            case JOIN_PLAYER -> join(session, new Gson().fromJson(message, JoinPlayerCommand.class));
            case JOIN_OBSERVER -> observe(session, new Gson().fromJson(message, JoinObserverCommand.class));
            case MAKE_MOVE -> move(session, new Gson().fromJson(message, MakeMoveCommand.class));
            case LEAVE -> leave(session, new Gson().fromJson(message, LeaveCommand.class));
            case RESIGN -> resign(session, new Gson().fromJson(message, ResignCommand.class));
        }
    }

    private void join(Session session, JoinPlayerCommand command) throws DataAccessException, IOException {
        // TODO: Do the precondition checks
        // Create notification message
        // Broadcast it to all the users in the specified game
        // Need to actually get the game
        System.out.println("Joining game websockets");
        Game gameModel = gameDatabase.getGameModel(command.getGameID(), db);

        if (command.teamColor == ChessGame.TeamColor.WHITE) {
            gameModel.setWhiteUsername(authDatabase.getUserName(command.getAuthString(), db));
            gameDatabase.addPlayer("white", command.getGameID(), gameModel.getWhiteUsername(), db);
        } else {
            gameModel.setBlackUsername(authDatabase.getUserName(command.getAuthString(), db));
            gameDatabase.addPlayer("black", command.getGameID(), gameModel.getBlackUsername(), db);
        }

        LoadMessage loadMessage = new LoadMessage(gameModel);
        session.getRemote().sendString(new Gson().toJson(loadMessage));
    }

    private void observe(Session session, JoinObserverCommand command) throws DataAccessException, IOException {
        System.out.println("Observing game websockets");

        Game gameModel = gameDatabase.getGameModel(command.getGameID(), db);

        LoadMessage loadMessage = new LoadMessage(gameModel);
        session.getRemote().sendString(new Gson().toJson(loadMessage));
    }

    private void move(Session session, MakeMoveCommand moveCommand) throws DataAccessException, IOException {
        ChessMove move = moveCommand.getMove();
        Game gameModel = gameDatabase.getGameModel(moveCommand.getGameID(), db);
        String gameImplementation = gameModel.getGameImplementation();
        ChessGame chessGame = gameDatabase.JSONToGame(gameImplementation);

        // Turn the move into a ChessMove object then make the move then back to String
        try{
            chessGame.makeMove(move);
        } catch (InvalidMoveException e) {
            sendError(session, e.getMessage());
            throw new RuntimeException(e);
        }

        gameModel.setGameImplementation(gson.toJson(chessGame));
        LoadMessage loadMessage = new LoadMessage(gameModel);
        session.getRemote().sendString(new Gson().toJson(loadMessage));
    }

    private void leave(Session session, LeaveCommand leaveCommand) throws DataAccessException {
        String userType = leaveCommand.getWhiteOrBlack().toLowerCase();
        gameDatabase.removePlayer(userType, leaveCommand.getGameID(), db);
        // TODO: Broadcast notification to all users in the game

    }

    private void resign(Session session, ResignCommand resignCommand) throws DataAccessException, IOException {
        Game gameModel = gameDatabase.getGameModel(resignCommand.getGameID(), db);
        if(gameModel == null){
            sendError(session, "Game does not exist");
        }

        // Get the usename from the authToken
        String username = authDatabase.getUserName(resignCommand.getAuthString(), db);
        if(Objects.equals(gameModel.getBlackUsername(), username) || Objects.equals(gameModel.getWhiteUsername(), username)){
            gameDatabase.deleteGame(resignCommand.getGameID(), db);
        }
        if(!Objects.equals(gameModel.getBlackUsername(), username) && !Objects.equals(gameModel.getWhiteUsername(), username)){
            sendError(session, "You are not a player in this game");
        }

        //Then broadcast the notification to all users in the game
        //Delete the connection from the connection handler
    }

    public void sendError(Session session, String message) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage(message);
        session.getRemote().sendString(new Gson().toJson(errorMessage));
    }



}


