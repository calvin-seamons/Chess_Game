package ui;

import Adapters.ChessBoardAdapter;
import Adapters.ChessGameAdapter;
import Adapters.ChessPieceAdapter;
import Adapters.LoadMessageAdapter;
import chess.*;
import com.google.gson.GsonBuilder;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.Notification;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import javax.websocket.*;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.*;


public class WebsocketClient extends Endpoint {
    Session session;
    NotificationHandler notificationHandler;
    public WebsocketClient(String url, NotificationHandler notificationHandler) throws URISyntaxException, DeploymentException, IOException {
        System.out.println("Connecting to websocket");
        this.notificationHandler = notificationHandler;
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                try{
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> notificationHandler.message(new Gson().fromJson(message, Notification.class).message);
                        case LOAD_GAME -> notificationHandler.updateBoard(JSONToGame(message));
                        case ERROR -> notificationHandler.error(new Gson().fromJson(message, ErrorMessage.class).errorMessage);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void send(UserGameCommand command) throws IOException {
        System.out.println("Sending message");
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public Chess_Game JSONToGame(String json) {
        // Check if the json is null
        if (json == null) {
            System.out.println("JSON is null");
            return null;
        }
        var loadBuilder = new GsonBuilder();
        loadBuilder.registerTypeAdapter(LoadMessage.class, new LoadMessageAdapter());
        LoadMessage loadMessage = new Gson().fromJson(json, LoadMessage.class);
        String gameString = loadMessage.game.getGameImplementation();

        var builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
        builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());

        return builder.create().fromJson(gameString, Chess_Game.class);
    }

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig endpointConfig) {
        System.out.println("Opened websocket");
    }
}
