package main.Handlers;

import Requests.RegisterRequest;
import Results.RegisterResult;
import com.google.gson.Gson;
import dataAccess.DataAccessException;

import java.util.Objects;

/**
 * The handler for the register request
 * Converts a RegisterRequest object into a JSON string
 * Converts a JSON string into a RegisterRequest object
 */
public class RegisterHandler extends BaseChecker {
    public RegisterHandler() {}

    /**
     * Converts a RegisterRequest object to a JSON string
     * @param request the RegisterRequest object to convert
     * @return a JSON string
     */
    public String registerRequestToHTTP(RegisterRequest request, String errorMessage) throws DataAccessException {
        Gson gson = new Gson();
        RegisterResult result = new RegisterResult();
        result.setMessage(errorMessage);

        if(Objects.equals(result.getMessage(), "Error: Already Taken")){
            return gson.toJson(result);
        }
        else if (Objects.equals(result.getMessage(), "Error: Bad Request")){
            return gson.toJson(result);
        }

        result.setUsername(request.getUsername());
        result.setAuthToken(createAuthToken());
        return gson.toJson(result);
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
