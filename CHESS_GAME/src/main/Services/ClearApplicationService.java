package main.Services;

import dataAccess.*;
import main.dataAccess.AuthDAO;
import main.dataAccess.Database;
import main.dataAccess.GameDAO;
import main.dataAccess.UserDAO;

public class ClearApplicationService {

    /**
     * Clears all data from the database (just like the /clear API)
     */
    public void clearApplication(AuthDAO authDatabase, GameDAO gameDatabase, UserDAO userDatabase, Database db) throws main.dataAccess.DataAccessException {
        authDatabase.clearAuthDatabase(db);
        gameDatabase.clearGameDatabase(db);
        userDatabase.clearUserDatabase(db);
    }
}
