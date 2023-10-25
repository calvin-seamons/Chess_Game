package Handlers;

import Requests.JoinGameRequest;
import Results.JoinGameResult;

/**
 * The handler for the joinGame request
 * Converts a JoinGameRequest object into a JSON string
 * Converts a JSON string into a JoinGameRequest object
 */
public class JoinGameHandler {
    public JoinGameHandler() {}

    /**
     * Converts a JSON string into a JoinGameRequest object
     * @param request the JSON string to convert
     * @return a JoinGameRequest object
     */
    public String joinGameRequestToHTTP(JoinGameRequest request) {
        return null;
    }

    /**
     * Converts a JoinGameRequest object into a JSON string
     * @param responseBody the JoinGameRequest object to convert
     * @return a JSON string
     */
    public JoinGameResult HTTPToJoinGameResult(String responseBody) {
        return null;
    }
}
