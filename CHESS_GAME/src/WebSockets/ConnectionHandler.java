package WebSockets;

import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.*;

import java.io.IOException;
import java.util.ArrayList;


public class ConnectionHandler {
    public final ConcurrentHashMap<String, Connection> gameConnections = new ConcurrentHashMap<>();

    public void addConnection(String username, Session session){
        System.out.println("Adding connection for " + username);
        var connection = gameConnections.get(username);
        if (!gameConnections.containsKey(username)) {
            gameConnections.put(username, new Connection(username, session));
        }
        else {
            connection.session = session;
        }
    }

    public void removeConnection(String username){
        System.out.println("Removing connection for " + username);
        gameConnections.remove(username);
    }

    public void removeAllConnections(){
        System.out.println("Removing all connections");
        gameConnections.clear();
    }

    public <T> void broadcast(String username, T message) {
        System.out.println("Broadcasting to " + username);
        var closedSessions = new ArrayList<Connection>();

        for (Connection connection : gameConnections.values()) {
            if (connection.session.isOpen()) {
                if (!connection.username.equals(username)) {
                    try {
                        String jsonMessage = new Gson().toJson(message);
                        connection.send(jsonMessage);
                    } catch (IOException e) {
                        // closedSessions.add(connection);
                    }
                }
            } else {
                closedSessions.add(connection);
            }
        }

        for (var connection : closedSessions) {
            gameConnections.remove(connection.username);
        }
    }

    public void broadcastAll(Notification notification){
        System.out.println("Broadcasting to all");
    }




}
