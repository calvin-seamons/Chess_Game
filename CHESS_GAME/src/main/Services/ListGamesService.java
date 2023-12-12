package main.Services;

import Requests.AuthTokenRequest;
import dataAccess.DataAccessException;
import main.dataAccess.*;
import main.Handlers.*;

/**
 * ListGamesHandler returns a list of games
 */
public class ListGamesService extends BaseChecker {

    /**
     * This method will take in an authToken and return a ListGamesResult object
     * @param request the HTTP request body
     * @param authDatabase the database access object for the auth table
     * @param gameDatabase the database access object for the game table
     * @return the errorMessage of the list games operation
     */
    public String listGames(AuthTokenRequest request, AuthDAO authDatabase, GameDAO gameDatabase, Database db) throws main.dataAccess.DataAccessException {
        String errorMessage = null;
        request.setUsername(authDatabase.getUserName(request.getAuthToken(), db));

        if(invalidAuthToken(request, authDatabase, db)){
            errorMessage = "Error: Unauthorized";
            return errorMessage;
        }
       return errorMessage;
    }
}
