package Handlers;

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
    public String createGameToHTTP (CreateGameRequest request) throws DataAccessException {
        Gson gson = new Gson();
        CreateGameResult result;
        if(!validateAuthToken(request.getAuthToken())){
            result = new CreateGameResult(null, "Error: unauthorized");
        }
        else{
            result = new CreateGameResult(null, null);
        }

        if(!validateGameName(request.getGameName())){
            result = new CreateGameResult(null, "Error: Bad Request");
        }
        else{
            result = new CreateGameResult(getNewGameID(), null);
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

    private boolean validateGameName(String gameName) throws DataAccessException {
        GameDAO gameDAO = new GameDAO();
        gameDAO.createGame(gameName);
        return true;
    }

    private String getNewGameID() {
        return "1234";
    }
}
