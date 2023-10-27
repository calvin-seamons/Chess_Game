package Handlers;

import Models.User;
import Requests.RegisterRequest;
import Results.RegisterResult;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;

/**
 * The handler for the register request
 * Converts a RegisterRequest object into a JSON string
 * Converts a JSON string into a RegisterRequest object
 */
public class RegisterHandler extends BaseHandler {
    public RegisterHandler() {}

    /**
     * Converts a RegisterRequest object to a JSON string
     * @param request the RegisterRequest object to convert
     * @return a JSON string
     */
    public String registerRequestToHTTP(RegisterRequest request, UserDAO userDatabase) throws DataAccessException {
        Gson gson = new Gson();
        RegisterResult result = new RegisterResult();

        if(duplicateInDatabase(userDatabase, request)){
            result.setUsername(null);
            result.setAuthToken(null);
            result.setMessage("Error: Already Taken");
            return gson.toJson(result);
        }

        if(request.getUsername() != null & request.getEmail() != null & request.getPassword() != null) {
            result.setUsername(request.getUsername());
            result.setAuthToken(createAuthToken());
            result.setMessage(null);
        }
        else {
            result.setUsername(null);
            result.setAuthToken(null);
            result.setMessage("Error: Bad Request");
        }

        return gson.toJson(result);
    }

    private boolean duplicateInDatabase(UserDAO userDAO, RegisterRequest request) throws DataAccessException {
        Gson gson = new Gson();
        User user = new User();
        user = gson.fromJson(gson.toJson(request), user.getClass());

        if(userDAO.readUser(user) != null)
            return true;
        else
            return false;
    }

    /**
     * Converts a JSON string to a RegisterRequest object
     * @param responseBody the JSON string to convert
     * @return a RegisterRequest object
     */
    public RegisterRequest HTTPToRegisterRequest(String responseBody) {
        Gson gson = new Gson();
        return gson.fromJson(responseBody, RegisterRequest.class);
    }
}
