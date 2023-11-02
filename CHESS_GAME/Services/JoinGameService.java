package Services;

import Handlers.BaseHandler;
import Models.Game;
import Requests.AuthTokenRequest;
import Requests.JoinGameRequest;
import Results.JoinGameResult;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;

/**
 * JoinGameService allows a user to join a game either as a player or as an observer
 */
public class JoinGameService extends BaseHandler {
    /**
     * Allows a user to join a game either as a player or as an observer
     * @param request the JoinGameRequest object to convert
     * @return a JoinGameResult object
     */
    public String joinGame(JoinGameRequest request, AuthDAO authDatabase, GameDAO gameDatabase) throws DataAccessException {
        String errorMessage;
        AuthTokenRequest authTokenRequest = new AuthTokenRequest();
        authTokenRequest.setAuthToken(request.getAuthToken());
        authTokenRequest.setUsername(authDatabase.getUserName(request.getAuthToken()));

        if(!validateAuthToken(authTokenRequest, authDatabase)) {
            errorMessage = "Error: Unauthorized";
        }
        else if(!validateGameID(request.getGameID(), gameDatabase)){
            errorMessage = "Error: Bad Request";
        }
        else if(teamAlreadyTaken(request, authDatabase, gameDatabase)){
            errorMessage = "Error: Already Taken";
        }
        else {
            errorMessage = null;
        }
        return errorMessage;
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
