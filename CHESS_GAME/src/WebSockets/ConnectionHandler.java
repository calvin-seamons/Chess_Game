package WebSockets;

import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;


public class ConnectionHandler {
    public final ConcurrentHashMap<String, Connection> gameConnections = new ConcurrentHashMap<>();
    Gson gson = new Gson();

    public void addConnection(String username, Session session, Connection connection){
        System.out.println("Adding connection for " + username);

        if(!gameConnections.containsKey(username)){
            System.out.println("Connection does not exist for " + username);
            connection.session = session;
            gameConnections.put(username, connection);
        }
        else{
            connection.session = session;
            gameConnections.replace(username, connection);
            System.out.println("Connection already exists for " + username);
        }
    }

    public void removeGameConnections(int gameID){
        System.out.println("Removing connections for game " + gameID);
        var closedSessions = new ArrayList<Connection>();

        for (Connection c : gameConnections.values()) {
            if (c.session.isOpen()) {
                if(c.gameID == gameID){
                    c.gameID = 1;
                }
            }
            else{
                closedSessions.add(c);
            }
        }
        for (Connection c : closedSessions) {
            gameConnections.remove(c.username);
        }
    }

    public void removeAllConnections(){
        System.out.println("Removing all connections");
        gameConnections.clear();
    }

    public <T> void broadcast(String username, T message, int gameID) {
        System.out.println("Broadcasting to " + username);
        var closedSessions = new ArrayList<Connection>();

        for (Connection c : gameConnections.values()) {
            if (c.session.isOpen()) {
                if(c.gameID == gameID){
                    if(!c.username.equals(username)){
                        try {
                            c.send(gson.toJson(message));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else{
                closedSessions.add(c);
            }
        }
        for (Connection c : closedSessions) {
            gameConnections.remove(c.username);
        }
    }

    public void broadcastAll(Notification notification, int gameID){
        System.out.println("Broadcasting to all");
        var closedSessions = new ArrayList<Connection>();

        for (Connection c : gameConnections.values()) {
            if (c.session.isOpen()) {
                if(c.gameID == gameID){
                    try {
                        c.send(gson.toJson(notification));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                closedSessions.add(c);
            }
        }
        for (Connection c : closedSessions) {
            gameConnections.remove(c.username);
        }
    }




}
