package Services;

import Models.LoginRequest;
import Models.LoginResult;
import Models.LogoutResult;

public class LoginService {
    /**
     * Logs a user into the server
     * @param request the LoginRequest object to convert
     * @return a LoginResult object
     */
    public LoginResult login(LoginRequest request) {
        return null;
    }

    /**
     * Logs a user out of the server
     * @param authToken the authentication token of the user to logout
     * @return a LogoutResult object
     */
    public LogoutResult logout(String authToken) {
        return null;
    }
}
