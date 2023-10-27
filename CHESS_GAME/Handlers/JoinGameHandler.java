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
    public String joinGameRequestToHTTP(JoinGameRequest request, AuthDAO authDatabase, GameDAO gameDatabase) throws DataAccessException {
        Gson gson = new Gson();
        JoinGameResult result = new JoinGameResult();
        AuthTokenRequest authTokenRequest = new AuthTokenRequest();
        authTokenRequest.setAuthToken(request.getAuthToken());
        authTokenRequest.setUsername(authDatabase.getUserName(request.getAuthToken()));

        if(!validateAuthToken(authTokenRequest, authDatabase)) {
            result.setMessage("Error: Unauthorized");
        }
        else if(!validateGameID(request.getGameID(), gameDatabase)){
            result.setMessage("Error: Bad Request");
        }
        else if(teamAlreadyTaken(request, authDatabase, gameDatabase)){
            result.setMessage("Error: Already Taken");
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

    private boolean teamAlreadyTaken(JoinGameRequest request, AuthDAO authDatabase, GameDAO gameDatabase) throws DataAccessException {
        Game game = new Game();
        String authToken = request.getAuthToken();
        for(Game g : gameDatabase.findAll()){
            if(g.getGameId().equals(request.getGameID())){
                game = g;
            }
        }

        if(request.getTeam().equals(ChessGame.TeamColor.WHITE)){
            if(game.getWhiteUsername() == null){
                game.setWhiteUsername(authDatabase.getUserName(authToken));
                return false;
            }
            else{
                return true;
            }
        }
        else if(request.getTeam().equals(ChessGame.TeamColor.BLACK)){
            if(game.getBlackUsername() == null){
                game.setBlackUsername(authDatabase.getUserName(authToken));
                return false;
            }
            else{
                return true;
            }
        }
        else{
            return false;
        }
    }
}
