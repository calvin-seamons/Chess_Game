package Handlers;

import Requests.CreateGameRequest;
import Results.CreateGameResult;

/**
 * This class is responsible for converting CreateGameRequests to HTTP requests and HTTP responses to CreateGameResults
 */
public class CreateGameHandler {

    /**
     * This takes a CreateGameRequest and converts it to an HTTP request
     * @param request the CreateGameRequest
     * @return the HTTP request
     */
    public String createGameToHTTP (CreateGameRequest request){
            return null;
    }

    /**
     * This takes an HTTP response and converts it to a CreateGameResult
     * @param responseBody the HTTP response
     * @return the CreateGameResult
     */
    public CreateGameResult HTTPToCreateGameResult (String responseBody){
        return null;
    }
}
