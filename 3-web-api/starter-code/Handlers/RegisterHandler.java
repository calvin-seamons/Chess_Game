package Handlers;

import Requests.RegisterRequest;

/**
 * The handler for the register request
 * Converts a RegisterRequest object into a JSON string
 * Converts a JSON string into a RegisterRequest object
 */
public class RegisterHandler {
    public RegisterHandler() {}

    /**
     * Converts a RegisterRequest object to a JSON string
     * @param request the RegisterRequest object to convert
     * @return a JSON string
     */
    public String registerRequestToHTTP(RegisterRequest request){
        return null;
    }

    /**
     * Converts a JSON string to a RegisterRequest object
     * @param responseBody the JSON string to convert
     * @return a RegisterRequest object
     */
    public RegisterRequest HTTPToRegisterResult(String responseBody) {
        return null;
    }
}
