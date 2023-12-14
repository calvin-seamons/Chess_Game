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

    public void addConnection(String username, Session session, Connection connection){
        System.out.println("Adding connection for " + username);
        // Iterate through the connections and remove any that are closed
        for (Connection c : gameConnections.values()) {
            if (c.session.isOpen()) {
                if(c.gameID == connection.gameID){
                    if(!c.username.equals(username)){
                        try {
                            c.send(new Gson().toJson(new Notification(username + " has joined the game")));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else{
                gameConnections.remove(c.username);
            }
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

    public <T> void broadcast(String username, T message, Connection connection) {
        System.out.println("Broadcasting to " + username);
        var closedSessions = new ArrayList<Connection>();

        for (Connection c : gameConnections.values()) {
            if (c.session.isOpen()) {
                if(c.gameID == connection.gameID){
                    if(!c.username.equals(username)){
                        try {
                            c.send(new Gson().toJson(message));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else{
                gameConnections.remove(c.username);
            }
        }
    }

    public void broadcastAll(Notification notification){
        System.out.println("Broadcasting to all");
        var closedSessions = new ArrayList<Connection>();

        for (Connection c : gameConnections.values()) {
            if (c.session.isOpen()) {
                try {
                    c.send(new Gson().toJson(notification));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                gameConnections.remove(c.username);
            }
        }
    }




}
