package Services;

import Handlers.BaseChecker;
import Models.Game;
import Requests.AuthTokenRequest;
import Requests.JoinGameRequest;
import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.GameDAO;

/**
 * JoinGameService allows a user to join a game either as a player or as an observer
 */
public class JoinGameService extends BaseChecker {
    /**
     * Allows a user to join a game either as a player or as an observer
     * @param request the JoinGameRequest object to convert
     * @return a JoinGameResult object
     */
    public String joinGame(JoinGameRequest request, AuthDAO authDatabase, GameDAO gameDatabase, Database db) throws DataAccessException {
        String errorMessage;
        AuthTokenRequest authTokenRequest = new AuthTokenRequest();
        authTokenRequest.setAuthToken(request.getAuthToken());
        authTokenRequest.setUsername(authDatabase.getUserName(request.getAuthToken(),db ));

        if(invalidAuthToken(authTokenRequest, authDatabase, db)) {
            errorMessage = "Error: Unauthorized";
        }
        else if(!validateGameID(request.getGameID(), gameDatabase, db)){
            errorMessage = "Error: Bad Request";
        }
        else if(teamAlreadyTaken(request, authDatabase, gameDatabase, db)){
            errorMessage = "Error: Already Taken";
        }
        else {
            errorMessage = null;
            gameDatabase.claimSpot(request.getGameID(), request.getTeam(), authTokenRequest.getUsername(), db);
        }
        return errorMessage;
    }

    private boolean teamAlreadyTaken(JoinGameRequest request, AuthDAO authDatabase, GameDAO gameDatabase, Database db) throws DataAccessException {
        Game game = new Game();
        String authToken = request.getAuthToken();
        for(Game g : gameDatabase.findAll(db)){
            if(g.getGameID() == request.getGameID()){
                game = g;
            }
        }

        if(request.getTeam() == null){
            return false;
        }

        if(request.getTeam().equals(ChessGame.TeamColor.WHITE)){
            if(game.getWhiteUsername() == null){
                game.setWhiteUsername(authDatabase.getUserName(authToken,db ));
                return false;
            }
            else{
                return true;
            }
        }
        else if(request.getTeam().equals(ChessGame.TeamColor.BLACK)){
            if(game.getBlackUsername() == null){
                game.setBlackUsername(authDatabase.getUserName(authToken,db ));
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
