package Services;

import Handlers.BaseChecker;
import Requests.AuthTokenRequest;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;

public class LogoutService extends BaseChecker {

    /**
     * Logs out the user (Creates the HTTP request)
     * @param request the AuthtokenRequest object to convert
     * @param authDatabase the AuthDAO object to use
     * @return a error string
     * @throws DataAccessException if there is an error accessing the database
     */
    public String logout(AuthTokenRequest request, AuthDAO authDatabase, Database db) throws DataAccessException {
        String errorMessage = null;
        if(!databaseAuthMatches(request, authDatabase, db)){
            errorMessage = "Error: Unauthorized";
        }
        return errorMessage;
    }

    private boolean databaseAuthMatches(AuthTokenRequest request, AuthDAO authDatabase, Database db) throws DataAccessException {
        if(!authDatabase.readAuthToken(request,db)){
            return false;
        }
        return true;
    }
}
