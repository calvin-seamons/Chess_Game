package Handlers;

import Requests.AuthTokenRequest;
import Results.ListGamesResult;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.GameDAO;

/**
 * This class is responsible for handling the list games request
 * It will convert a list games request to an HTTP request and an HTTP response to a list games result
 */
public class ListGamesHandler extends BaseChecker {

    /**
     * This method will take in a ListGamesResult object and return an HTTP response body
     * @param request the AuthTokenRequest object
     * @return the HTTP response body
     */
    public String authTokenTolistGamesHTTP (AuthTokenRequest request, String errorMessage, AuthDAO authDatabase, GameDAO gameDatabase, Database db) throws DataAccessException {
        Gson gson = new Gson();
        ListGamesResult listGamesResult = new ListGamesResult();
        listGamesResult.setMessage(errorMessage);
        if(listGamesResult.getMessage() != null){
            listGamesResult.setMessage(errorMessage);
            listGamesResult.setGames(null);
            return gson.toJson(listGamesResult);
        }

        request.setUsername(authDatabase.getUserName(request.getAuthToken(),db ));
        listGamesResult.setGames(gameDatabase.findAll(db));
        return gson.toJson(listGamesResult);
    }

    /**
     * This method will take in an HTTP request and return a ListGamesResult object
     * @param request the HTTP request body
     * @return the result of the list games operation
     */
    public AuthTokenRequest HTTPToAuthTokenRequest (String request) {
        Gson gson = new Gson();
        return gson.fromJson(request, AuthTokenRequest.class);
    }
}
