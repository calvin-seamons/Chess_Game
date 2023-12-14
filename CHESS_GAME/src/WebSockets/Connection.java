package WebSockets;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;

import java.io.IOException;


public class Connection {
    public String username;
    public String authToken;
    public String gameID;
    public Session session;

    public Connection(String username, Session session){
        this.username = username;
        this.session = session;
    }

    public Connection(String authToken, String gameID, Session session){
        this.authToken = authToken;
        this.gameID = gameID;
        this.session = session;
    }

    public void send(String message) throws IOException {
        session.getRemote().sendString(message);
    }

    public void sendError(RemoteEndpoint endpoint, String message) throws IOException {
        endpoint.sendString(message);
    }
}
