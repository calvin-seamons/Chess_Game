package main.Handlers;

import Models.User;
import Requests.AuthTokenRequest;
import Requests.RegisterRequest;
import com.google.gson.Gson;
import dataAccess.*;
import main.dataAccess.AuthDAO;
import main.dataAccess.Database;
import main.dataAccess.GameDAO;
import main.dataAccess.UserDAO;

import java.util.UUID;

public abstract class BaseChecker {

    protected String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    protected boolean invalidAuthToken(AuthTokenRequest authToken, AuthDAO authDAO, Database db) throws main.dataAccess.DataAccessException {
        return !authDAO.readAuthToken(authToken, db);
    }

    protected boolean validateGameID(int gameID, GameDAO gameDAO, Database db) throws main.dataAccess.DataAccessException {
        return gameDAO.readGame(gameID, db);
    }

    protected boolean validateGameName(String gameName, GameDAO databaseGames, Database db) throws main.dataAccess.DataAccessException {
        return !databaseGames.gameExists(gameName, db);
    }

    protected boolean duplicateInDatabase(UserDAO userDAO, RegisterRequest request, Database db) throws main.dataAccess.DataAccessException {
        Gson gson = new Gson();
        User user = new User();
        user = gson.fromJson(gson.toJson(request), user.getClass());

        return userDAO.readUser(user, db) != null;
    }
}