package Handlers;

import Models.User;
import Requests.LoginRequest;
import Results.LoginResult;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;

/**
 * The handler for the login request
 * Converts a LoginRequest object into a JSON string
 * Converts a JSON string into a LoginRequest object
 */
public class LoginHandler extends BaseHandler{
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
    public String loginRequestToHTTP(LoginRequest request, UserDAO userDatabase, AuthDAO authDatabase) throws DataAccessException {
        Gson gson = new Gson();
        LoginResult result = new LoginResult();

        // Check to see if login is unauthorized
        if(unauthorizedLogin(userDatabase, request)){
            result.setUsername(null);
            result.setAuthToken(null);
            result.setMessage("Error: Unauthorized");
            return gson.toJson(result);
        }
        System.out.println("Login successful");
        String newAuthToken = createAuthToken();

        result.setUsername(request.getUsername());
        result.setAuthToken(newAuthToken);
        result.setMessage(null);

        authDatabase.updateAuthToken(request.getUsername(), newAuthToken);

        return gson.toJson(result);
    }

    private boolean unauthorizedLogin(UserDAO userDatabase, LoginRequest request) throws DataAccessException {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        user = userDatabase.readUser(user);

        if(user == null){
            return true;
        }
        else if(!user.getPassword().equals(request.getPassword())){
            return true;
        }
        return false;
    }
}
