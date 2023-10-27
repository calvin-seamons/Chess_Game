package Handlers;

import Requests.AuthTokenRequest;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;

import java.util.UUID;

public abstract class BaseHandler {

    protected String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    protected boolean validateAuthToken(AuthTokenRequest authToken, AuthDAO authDAO) throws DataAccessException {
        return authDAO.readAuthToken(authToken);
    }

    protected boolean validateGameID(String gameID, GameDAO gameDAO) throws DataAccessException {
        return gameDAO.readGame(gameID);
    }

    // Any other common methods can go here

}