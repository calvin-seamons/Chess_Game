package WebSockets;

import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;


public class ConnectionHandler {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void addConnection(String username, Session session){
        System.out.println("Adding connection for " + username);
        var connection = connections.get(username);
        if (!connections.containsKey(username)) {
            connections.put(username, new Connection(username, session));
        }
        else {
            connection.session = session;
        }
    }
}
