package Services;

import dataAccess.*;

public class ClearApplicationService {

    /**
     * Clears all data from the database (just like the /clear API)
     */
    public void clearApplication(AuthDAO authDatabase, GameDAO gameDatabase, UserDAO userDatabase, Database db) throws DataAccessException {
        authDatabase.clearAuthDatabase(db);
        gameDatabase.clearGameDatabase();
        userDatabase.clearUserDatabase(db);
    }
}
