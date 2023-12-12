package main.Services;

import Models.User;
import Requests.LoginRequest;
import main.dataAccess.*;
import main.Handlers.*;
import dataAccess.DataAccessException;

/**
 * LoginService logs a user into the server
 * Also logs a user out of the server
 */
public class LoginService extends BaseChecker {
    /**
     * Logs a user into the server
     * @param request the LoginRequest object to convert
     * @return a LoginResult object
     */
    public String login(LoginRequest request, UserDAO userDatabase, AuthDAO authDatabase, Database db) throws main.dataAccess.DataAccessException {
        String errorMessage = null;

        // Check to see if login is unauthorized
        if(unauthorizedLogin(userDatabase, request, db)){
            errorMessage = "Error: Unauthorized";
            return errorMessage;
        }

        return errorMessage;
    }

    private boolean unauthorizedLogin(UserDAO userDatabase, LoginRequest request, Database db) throws main.dataAccess.DataAccessException {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        user = userDatabase.readUser(user, db);

        if(user == null){
            return true;
        }
        else if(!user.getPassword().equals(request.getPassword())){
            return true;
        }
        return false;
    }
}
