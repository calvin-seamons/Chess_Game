package Services;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ClearApplicationService {

    /**
     * Clears all data from the database (just like the /clear API)
     */
    public void clearApplication(AuthDAO authDatabase, GameDAO gameDatabase, UserDAO userDatabase) {
        authDatabase.clearAuthDatabase();
        gameDatabase.clearGameDatabase();
        userDatabase.clearUserDatabase();
    }
}
