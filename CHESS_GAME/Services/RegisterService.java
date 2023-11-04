package Services;

import Handlers.BaseChecker;
import Models.User;
import Requests.RegisterRequest;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.UserDAO;

/**
 * RegisterService allows a user to register a new account
 */
public class RegisterService extends BaseChecker {

    /**
     * Checks for valid input and returns an errorMessage String
     * @param request the RegisterRequest object
     * @param userDatabase the UserDAO database
     * @return an errorMessage String
     * @throws DataAccessException if there is an error accessing the database
     */
    public String register(RegisterRequest request, UserDAO userDatabase, Database db) throws DataAccessException {
        if(duplicateInDatabase(userDatabase, request, db)){
            return "Error: Already Taken";
        }

        if(request.getUsername() != null & request.getEmail() != null & request.getPassword() != null) {
            return null;
        }
        else {
            return "Error: Bad Request";
        }
    }
}
