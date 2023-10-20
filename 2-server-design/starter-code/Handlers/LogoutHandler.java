package Handlers;

import Models.LogoutResult;

public class LogoutHandler {
public LogoutHandler() {}

    /**
     * Converts a JSON string into a LogoutRequest object
     * @param requestBody the JSON string to convert
     * @return a LogoutRequest object
     */
    public LogoutResult HTTPToLogoutResult(String requestBody) {
        return null;
    }

    /**
     * Logs out the user (Creates the HTTP request)
     * @param authToken the authentication of the user to logout
     * @return a JSON string
     */
    public String logoutRequestToHTTP(String authToken) {
        return null;
    }
}
