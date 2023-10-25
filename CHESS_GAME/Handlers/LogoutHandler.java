package Handlers;

import Results.LogoutResult;
import Requests.LogoutRequest;
import com.google.gson.Gson;

/**
 * Handles the Logout request
 * Converts a JSON string into a LogoutRequest object
 * Converts a LogoutRequest object into a JSON string
 */
public class LogoutHandler {
public LogoutHandler() {}

    /**
     * Converts a JSON string into a LogoutRequest object
     * @param requestBody the JSON string to convert
     * @return a LogoutRequest object
     */
    public LogoutRequest HTTPToLogoutRequest(String requestBody) {
        Gson gson = new Gson();
        return gson.fromJson(requestBody, LogoutRequest.class);
    }

    /**
     * Logs out the user (Creates the HTTP request)
     * @param request the LogoutRequest object to convert
     * @return a JSON string
     */
    public String logoutRequestToHTTP(LogoutRequest request) {
        LogoutResult result = new LogoutResult();

        if(!databaseAuthMatches(request)){
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

    private boolean databaseAuthMatches(LogoutRequest request) {
        return true;
    }
}