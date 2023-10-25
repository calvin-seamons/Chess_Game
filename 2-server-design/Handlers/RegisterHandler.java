package Handlers;

import Requests.RegisterRequest;
import Results.RegisterResult;
import com.google.gson.Gson;

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
    public String registerRequestToHTTP(RegisterRequest request){
        Gson gson = new Gson();
        RegisterResult result = new RegisterResult();
        if(duplicateInDatabase()){
            result.setUsername(null);
            result.setAuthToken(null);
            result.setMessage("ERROR 403: already taken ");
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
            result.setMessage("ERROR 400: Bad Request");
        }

        return gson.toJson(result);
    }

    private boolean duplicateInDatabase() {
        //TODO: check if username or email is already in database
        return false;
    }

    /**
     * Converts a JSON string to a RegisterRequest object
     * @param responseBody the JSON string to convert
     * @return a RegisterRequest object
     */
    public RegisterRequest HTTPToRegisterResult(String responseBody) {
        Gson gson = new Gson();
        return gson.fromJson(responseBody, RegisterRequest.class);
    }
}
