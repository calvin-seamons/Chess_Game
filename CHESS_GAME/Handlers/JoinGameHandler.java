package Handlers;

import Requests.JoinGameRequest;
import Results.JoinGameResult;
import com.google.gson.Gson;
import dataAccess.DataAccessException;

/**
 * The handler for the joinGame request
 * Converts a JoinGameRequest object into a JSON string
 * Converts a JSON string into a JoinGameRequest object
 */
public class JoinGameHandler extends BaseHandler{
    public JoinGameHandler() {}

    /**
     * Converts a JSON string into a JoinGameRequest object
     * @param request the JSON string to convert
     * @return a JoinGameRequest object
     */
    public String joinGameRequestToHTTP(JoinGameRequest request) throws DataAccessException {
        Gson gson = new Gson();
        JoinGameResult result = new JoinGameResult();
        if(!validateAuthToken(request.getAuthToken())) {
            result.setMessage("Error: Unauthorized");
        }
        else if(request.getGameID() == null){
            result.setMessage("Error: bad request");
        }
        else {
            result.setMessage(null);
        }
        return gson.toJson(result);
    }

    /**
     * Converts a JoinGameRequest object into a JSON string
     * @param responseBody the JoinGameRequest object to convert
     * @return a JSON string
     */
    public JoinGameRequest HTTPToJoinGameRequest (String responseBody) {
        Gson gson = new Gson();
        return gson.fromJson(responseBody, JoinGameRequest.class);
    }
}
