package main.Handlers;

import Requests.CreateGameRequest;
import Results.CreateGameResult;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import main.dataAccess.*;

/**
 * This class is responsible for converting CreateGameRequests to HTTP requests and HTTP responses to CreateGameResults
 */
public class CreateGameHandler extends BaseChecker {

    /**
     * This takes a CreateGameRequest and converts it to an HTTP request
     * @param request the CreateGameRequest
     * @return the HTTP request
     */
    public String createGameToHTTP (CreateGameRequest request, GameDAO gameDatabase, String errorMessage) throws DataAccessException {
        Gson gson = new Gson();
        CreateGameResult result = new CreateGameResult(gameDatabase.getNewGameID(), errorMessage);
        if(!errorMessage.isEmpty()){
            result.setGameID(null);
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
}
