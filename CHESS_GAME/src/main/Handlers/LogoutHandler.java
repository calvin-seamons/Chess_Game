package main.Handlers;

import Results.LogoutResult;
import Requests.AuthTokenRequest;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import main.dataAccess.*;


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
    public AuthTokenRequest HTTPToLogoutRequest(String authToken, AuthDAO authDatabase, Database db) throws main.dataAccess.DataAccessException {
        AuthTokenRequest request = new AuthTokenRequest();
        request.setAuthToken(authToken);
        //TODO: You're grabbing the username from the database, which doesn't entirely make sense but that's the best you can do for now
        request.setUsername(authDatabase.getUserName(request.getAuthToken(),db ));
        return request;
    }

    /**
     * Logs out the user (Creates the HTTP request)
     * @param request the LogoutRequest object to convert
     * @return a JSON string
     */
    public String logoutRequestToHTTP(AuthTokenRequest request, String errorMessage) throws DataAccessException {
        Gson gson = new Gson();
        LogoutResult result = new LogoutResult();
        result.setMessage(errorMessage);

        return gson.toJson(result);
    }
}
