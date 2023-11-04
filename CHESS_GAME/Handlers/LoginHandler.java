package Handlers;

import Models.Authtoken;
import Requests.LoginRequest;
import Results.LoginResult;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;

/**
 * The handler for the login request
 * Converts a LoginRequest object into a JSON string
 * Converts a JSON string into a LoginRequest object
 */
public class LoginHandler extends BaseChecker {
    public LoginHandler() {}

    /**
     * Converts a JSON string into a LoginRequest object
     * @param requestBody the JSON string to convert
     * @return a LoginRequest object
     */
    public LoginRequest HTTPToLoginRequest(String requestBody) {
        Gson gson = new Gson();
        return gson.fromJson(requestBody, LoginRequest.class);
    }

    /**
     * Converts a LoginRequest object into a JSON string
     * @param request the LoginRequest object to convert
     * @return a JSON string
     */
    public String loginRequestToHTTP(LoginRequest request, AuthDAO authDatabase, String errorMessage, Database db) throws DataAccessException {
        Gson gson = new Gson();
        LoginResult result = new LoginResult();
        result.setMessage(errorMessage);

        // Check to see if login is unauthorized
        if(result.getMessage() != null){
            result.setMessage(errorMessage);
            return gson.toJson(result);
        }
        String newAuthToken = createAuthToken();
        result.setUsername(request.getUsername());
        result.setAuthToken(newAuthToken);
        if(!authDatabase.updateAuthToken(request.getUsername(), newAuthToken,db)){
            authDatabase.createAuth(new Authtoken(newAuthToken, request.getUsername()),db);
        }

        return gson.toJson(result);
    }
}
