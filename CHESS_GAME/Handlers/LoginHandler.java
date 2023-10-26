package Handlers;

import Requests.LoginRequest;
import Results.LoginResult;
import com.google.gson.Gson;

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
    public String loginRequestToHTTP(LoginRequest request) {
        Gson gson = new Gson();
        LoginResult result = new LoginResult();

        // Check to see if login is unauthorized
        if(unauthorizedLogin()){
            result.setUsername(null);
            result.setAuthToken(null);
            result.setMessage("Error: Unauthorized");
            return gson.toJson(result);
        }

        result.setUsername(request.getUsername());
        result.setAuthToken(createAuthToken());
        result.setMessage(null);
        return gson.toJson(result);
    }

    private boolean unauthorizedLogin() {
        //TODO check if username and password match in database
        return false;
    }
}
