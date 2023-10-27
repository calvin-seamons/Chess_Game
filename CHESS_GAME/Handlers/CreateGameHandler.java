package Handlers;

import Requests.AuthTokenRequest;
import Requests.CreateGameRequest;
import Results.CreateGameResult;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;

/**
 * This class is responsible for converting CreateGameRequests to HTTP requests and HTTP responses to CreateGameResults
 */
public class CreateGameHandler extends BaseHandler{

    /**
     * This takes a CreateGameRequest and converts it to an HTTP request
     * @param request the CreateGameRequest
     * @return the HTTP request
     */
    public String createGameToHTTP (CreateGameRequest request, GameDAO gameDatabase, AuthDAO authDatabase) throws DataAccessException {
        Gson gson = new Gson();
        CreateGameResult result;

        AuthTokenRequest authTokenRequest = new AuthTokenRequest();
        authTokenRequest.setAuthToken(request.getAuthToken());
        authTokenRequest.setUsername(authDatabase.getUserName(request.getAuthToken()));

        if(!validateAuthToken(authTokenRequest, authDatabase)){
            result = new CreateGameResult(null, "Error: Unauthorized");
        }
        else if(!validateGameName(request.getGameName(), gameDatabase)){
            result = new CreateGameResult(null, "Error: Bad Request");
        }
        else{
            result = new CreateGameResult(gameDatabase.getNewGameID(), null);
        }

        return gson.toJson(result);
    }

    /**
     * This takes an HTTP response and converts it to a CreateGameResult
     * @param responseBody the HTTP response
     * @return the CreateGameResult
     */
    public CreateGameRequest HTTPToCreateGameRequest(String responseBody){
        Gson gson = new Gson();
        return gson.fromJson(responseBody, CreateGameRequest.class);
    }

    private boolean validateGameName(String gameName, GameDAO databaseGames) throws DataAccessException {
        if(databaseGames.checkDuplicateName(gameName)){
            return false;
        }
        return true;
    }
}
