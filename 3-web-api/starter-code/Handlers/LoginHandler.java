package Handlers;

import Requests.LoginRequest;

/**
 * The handler for the login request
 * Converts a LoginRequest object into a JSON string
 * Converts a JSON string into a LoginRequest object
 */
public class LoginHandler {
    public LoginHandler() {}

    /**
     * Converts a JSON string into a LoginRequest object
     * @param requestBody the JSON string to convert
     * @return a LoginRequest object
     */
    public LoginRequest HTTPToLoginRequest(String requestBody) {
        return null;
    }

    /**
     * Converts a LoginRequest object into a JSON string
     * @param request the LoginRequest object to convert
     * @return a JSON string
     */
    public String loginRequestToHTTP(LoginRequest request) {
        return null;
    }

}
