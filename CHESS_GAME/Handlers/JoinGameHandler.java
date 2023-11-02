package Handlers;

import Models.Game;
import Requests.AuthTokenRequest;
import Requests.JoinGameRequest;
import Results.JoinGameResult;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;

import static chess.ChessPiece.Team.WHITE;

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
    public String joinGameRequestToHTTP(JoinGameRequest request, String errorMessage) throws DataAccessException {
        Gson gson = new Gson();
        JoinGameResult result = new JoinGameResult();
        result.setMessage(errorMessage);
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
