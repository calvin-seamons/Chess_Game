package WebSockets;

import Models.Game;
import chess.*;
import com.google.gson.*;
import main.dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;
import webSockets.UserGameCommand;
import webSockets.UserGameCommand.CommandType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
    Gson gson = new Gson();
    private final ConnectionHandler connectionHandler = new ConnectionHandler();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);


    public WebsocketHandler(Connection connection, GameDAO gameDatabase, AuthDAO authDatabase, UserDAO userDatabase) {
        this.connection = connection;
        this.gameDatabase = gameDatabase;
        this.authDatabase = authDatabase;
        this.userDatabase = userDatabase;
    }

//    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException {
        executorService.submit(() -> {
            try {
                processMessage(session, message);
            } catch (Exception e) {
                System.out.println("Error processing message");
            }
        });
    }
    @OnWebSocketMessage
    public void processMessage(Session session, String message) throws DataAccessException, IOException {
        System.out.println("Thread activated");
        connection = db.getConnection();
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType(
        )) {
            case CommandType.JOIN_PLAYER -> join(session, gson.fromJson(message, JoinPlayerCommand.class));
            case CommandType.JOIN_OBSERVER -> observe(session, gson.fromJson(message, JoinObserverCommand.class));
            case CommandType.MAKE_MOVE -> move(session, gson.fromJson(message, MakeMoveCommand.class));
            case CommandType.LEAVE -> leave(session, gson.fromJson(message, LeaveCommand.class));
            case CommandType.RESIGN -> resign(session, gson.fromJson(message, ResignCommand.class));
        }
    }

    private void join(Session session, JoinPlayerCommand command) throws DataAccessException, IOException {
        Game gameModel = gameDatabase.getGameModel(command.getGameID(), db);
        String username = authDatabase.getUserName(command.getAuthString(), db);

        if(authDatabase.getUserName(command.getAuthString(), db) == null){
            errorSend(session, "Invalid auth token");
            return;
        }
        if(gameModel == null){
            errorSend(session, "Game does not exist");
            return;
        }
        if(command.getPlayerColor() == null){
            errorSend(session, "No team color specified");
            return;
        }

        if(command.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            if(gameModel.getWhiteUsername() != null && !Objects.equals(username, gameModel.getWhiteUsername())) {
                errorSend(session, "White player already exists");
                return;
            }
        } else if(command.getPlayerColor() == ChessGame.TeamColor.BLACK){
            if(gameModel.getBlackUsername() != null && !Objects.equals(username, gameModel.getBlackUsername())) {
                errorSend(session, "Black player already exists");
                return;
            }
        }

        if(gameModel.getBlackUsername() == null && gameModel.getWhiteUsername() == null){
            errorSend(session, "No players exist");
            return;
        }

        if (command.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            gameModel.setWhiteUsername(authDatabase.getUserName(command.getAuthString(), db));
            gameDatabase.addPlayer("white", command.getGameID(), gameModel.getWhiteUsername(), db);
        } else {
            gameModel.setBlackUsername(authDatabase.getUserName(command.getAuthString(), db));
            gameDatabase.addPlayer("black", command.getGameID(), gameModel.getBlackUsername(), db);
        }

        WebSockets.Connection connection = new WebSockets.Connection(command.getAuthString(), command.getGameID(), session, authDatabase.getUserName(command.getAuthString(), db));
        connectionHandler.addConnection(authDatabase.getUserName(command.getAuthString(), db), session, connection);
        connectionHandler.broadcast(username, new Notification("User " + username + " has joined the game"), connection);

        LoadMessage loadMessage = new LoadMessage(gameModel);
        session.getRemote().sendString(new Gson().toJson(loadMessage));
    }

    private void observe(Session session, JoinObserverCommand command) throws DataAccessException, IOException {
        String username = authDatabase.getUserName(command.getAuthString(), db);
        Game gameModel = gameDatabase.getGameModel(command.getGameID(), db);
        if(username == null){
            errorSend(session, "Invalid auth token");
            return;
        } else if(gameModel == null){
            errorSend(session, "Game does not exist");
            return;
        }

        LoadMessage loadMessage = new LoadMessage(gameModel);
        session.getRemote().sendString(gson.toJson(loadMessage));
        WebSockets.Connection connection = new WebSockets.Connection(command.getAuthString(), command.getGameID(), session, username);
        connectionHandler.addConnection(username, session, connection);
        Notification notification = new Notification("User " + username + " has joined the game");
        notification.message = "Some bullshit string";
        connectionHandler.broadcast(username, notification, connection);

//        session.getRemote().sendString(new Gson().toJson(loadMessage));
    }

    private void move(Session session, MakeMoveCommand moveCommand) throws DataAccessException, IOException {
        ChessMove move = moveCommand.getMove();
        ChessGame chessGame = gameDatabase.JSONToGame(moveCommand.getGameImplementation());

        if(moveCommand.getTeamColor() != chessGame.getTeamTurn()){
            errorSend(session, "It is not your turn");
            return;
        }

        // Turn the move into a ChessMove object then make the move then back to String
        try{
            chessGame.makeMove(move);
        } catch (InvalidMoveException e) {
            errorSend(session, e.getMessage());
            throw new RuntimeException(e);
        }

        Game gameModel = gameDatabase.getGameModel(moveCommand.getGameID(), db);
        gameModel.setGameImplementation(gson.toJson(chessGame));
        gameDatabase.updateGame(gameModel, db);
        LoadMessage loadMessage = new LoadMessage(gameModel);
        session.getRemote().sendString(new Gson().toJson(loadMessage));
    }

    private void leave(Session session, LeaveCommand leaveCommand) throws DataAccessException {
        String userType = leaveCommand.getWhiteOrBlack().toLowerCase();
        gameDatabase.removePlayer(userType, leaveCommand.getGameID(), db);
        connectionHandler.removeConnection(authDatabase.getUserName(leaveCommand.getAuthString(), db));
        // TODO: Broadcast notification to all users in the game

    }

    private void resign(Session session, ResignCommand resignCommand) throws DataAccessException, IOException {
        Game gameModel = gameDatabase.getGameModel(resignCommand.getGameID(), db);
        String username = authDatabase.getUserName(resignCommand.getAuthString(), db);
        if(gameModel == null){
            errorSend(session, "Game does not exist");
        }

        // Check if user is a player in the game
        if(gameModel.getBlackUsername() == null && gameModel.getWhiteUsername() == null){
            errorSend(session, "No players exist");
        }

        if(!Objects.equals(gameModel.getBlackUsername(), username) || !Objects.equals(gameModel.getWhiteUsername(), username)){
            errorSend(session, "You are not a player in this game");
        }

        if(Objects.equals(gameModel.getBlackUsername(), username) || Objects.equals(gameModel.getWhiteUsername(), username)){
            gameDatabase.deleteGame(resignCommand.getGameID(), db);
        }

        //Then broadcast the notification to all users in the game
        //Delete the connection from the connection handler
        connectionHandler.removeConnection(username);
        WebSockets.Connection connection = new WebSockets.Connection(resignCommand.getAuthString(), resignCommand.getGameID(), session, username);
        connectionHandler.broadcast(username, new Notification(username + " has resigned"), connection);
    }

    public void errorSend(Session session, String message) throws IOException {
        ErrorMessage errorMessage = new ErrorMessage(message);
        session.getRemote().sendString(gson.toJson(errorMessage));
    }

    @OnWebSocketError
    public void sendError(Session session, Throwable throwable) throws IOException {
        System.out.println("Error in websocket");
        throwable.printStackTrace();
//        session.getRemote().sendString(gson.toJson(new ErrorMessage(throwable.getMessage())));
    }



}


