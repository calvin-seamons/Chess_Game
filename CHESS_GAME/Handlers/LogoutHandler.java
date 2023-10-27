package Handlers;

import Models.User;
import Results.LogoutResult;
import Requests.AuthTokenRequest;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;

/**
 * Handles the Logout request
 * Converts a JSON string into a LogoutRequest object
 * Converts a LogoutRequest object into a JSON string
 */
public class LogoutHandler {
public LogoutHandler() {}

    /**
     * Converts a JSON string into a LogoutRequest object
     * @param authToken the JSON string to convert
     * @return a LogoutRequest object
     */
    public AuthTokenRequest HTTPToLogoutRequest(String authToken, AuthDAO authDatabase) throws DataAccessException {
        AuthTokenRequest request = new AuthTokenRequest();
        request.setAuthToken(authToken);
        //TODO: You're grabbing the username from the database, which doesn't entirely make sense but that's the best you can do for now
        request.setUsername(authDatabase.getUserName(request.getAuthToken()));
        return request;
    }

    /**
     * Logs out the user (Creates the HTTP request)
     * @param request the LogoutRequest object to convert
     * @return a JSON string
     */
    public String logoutRequestToHTTP(AuthTokenRequest request, AuthDAO authDatabase) throws DataAccessException {
        LogoutResult result = new LogoutResult();

        if(!databaseAuthMatches(request, authDatabase)){
            result.setMessage("Error: Unauthorized");
            Gson gson = new Gson();
            return gson.toJson(result);
        }
        else{
            result.setMessage(null);
            Gson gson = new Gson();
            return gson.toJson(result);
        }
    }

    private boolean databaseAuthMatches(AuthTokenRequest request, AuthDAO authDatabase) throws DataAccessException {
        if(!authDatabase.readAuthToken(request)){
            return false;
        }
        return true;
    }
}
