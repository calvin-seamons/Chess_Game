package Handlers;

import Requests.AuthTokenRequest;
import Results.ListGamesResult;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;

/**
 * This class is responsible for handling the list games request
 * It will convert a list games request to an HTTP request and an HTTP response to a list games result
 */
public class ListGamesHandler extends BaseHandler{

    /**
     * This method will take in a ListGamesResult object and return an HTTP response body
     * @param request the AuthTokenRequest object
     * @return the HTTP response body
     */
    public String authTokenTolistGamesHTTP (AuthTokenRequest request, AuthDAO authDatabase, GameDAO gameDatabase) throws DataAccessException {
        Gson gson = new Gson();
        ListGamesResult listGamesResult = new ListGamesResult();
        request.setUsername(authDatabase.getUserName(request.getAuthToken()));

        if(!validateAuthToken(request, authDatabase)){
            listGamesResult.setMessage("Error: Unauthorized");
            listGamesResult.setGames(null);
            return gson.toJson(listGamesResult);
        }

        listGamesResult.setGames(gameDatabase.findAll());
        listGamesResult.setMessage(null);
        String message = gson.toJson(listGamesResult);
        System.out.println(message);
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
