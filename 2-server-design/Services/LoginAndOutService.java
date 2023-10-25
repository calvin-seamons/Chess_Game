package Services;

import Requests.LoginRequest;
import Results.LoginResult;
import Results.LogoutResult;

/**
 * LoginService logs a user into the server
 * Also logs a user out of the server
 */
public class LoginAndOutService {
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
