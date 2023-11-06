package Services;

import Handlers.BaseChecker;
import Requests.AuthTokenRequest;
import Requests.CreateGameRequest;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.GameDAO;

/**
 * CreateGameService creates a new game
 */
public class CreateGameService extends BaseChecker {

    /**
     * This method creates a new game
     * @param request The request to create a new game
     * @param gameDatabase The database of games
     * @param authDatabase The database of auth tokens
     * @return The result of the request
     * @throws DataAccessException An error if there is a problem with the database
     */
    public String createGame(CreateGameRequest request, GameDAO gameDatabase, AuthDAO authDatabase, Database db) throws DataAccessException {
        String result = null;

        AuthTokenRequest authTokenRequest = new AuthTokenRequest();
        authTokenRequest.setAuthToken(request.getAuthToken());
        authTokenRequest.setUsername(authDatabase.getUserName(request.getAuthToken(),db ));

        if(invalidAuthToken(authTokenRequest, authDatabase, db)){
            result = "Error: Unauthorized";
        }
        else if(!validateGameName(request.getGameName(), gameDatabase, db)){
            result = "Error: Bad Request";
        }
        else{
            result = "";
        }
        return result;
    }
}
