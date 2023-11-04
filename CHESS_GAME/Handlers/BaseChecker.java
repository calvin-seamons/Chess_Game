package Handlers;

import Models.User;
import Requests.AuthTokenRequest;
import Requests.RegisterRequest;
import com.google.gson.Gson;
import dataAccess.*;

import java.util.UUID;

public abstract class BaseChecker {

    protected String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    protected boolean invalidAuthToken(AuthTokenRequest authToken, AuthDAO authDAO, Database db) throws DataAccessException {
        return !authDAO.readAuthToken(authToken, db);
    }

    protected boolean validateGameID(String gameID, GameDAO gameDAO) throws DataAccessException {
        return gameDAO.readGame(gameID);
    }

    protected boolean validateGameName(String gameName, GameDAO databaseGames) throws DataAccessException {
        return !databaseGames.checkDuplicateName(gameName);
    }

    protected boolean duplicateInDatabase(UserDAO userDAO, RegisterRequest request, Database db) throws DataAccessException {
        Gson gson = new Gson();
        User user = new User();
        user = gson.fromJson(gson.toJson(request), user.getClass());

        return userDAO.readUser(user, db) != null;
    }
}