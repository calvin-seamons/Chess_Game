package Handlers;

import Results.ListGamesResult;

/**
 * This class is responsible for handling the list games request
 * It will convert a list games request to an HTTP request and an HTTP response to a list games result
 */
public class ListGamesHandler {

    /**
     * This method will take in an authToken and return a ListGamesResult object
     * @param authToken the authToken of the user requesting the list of games
     * @return the result of the list games operation
     */
    public String listGamesToHTTP (String authToken){
        return null;
    }

    /**
     * This method will take in an HTTP response and return a ListGamesResult object
     * @param responseBody the HTTP response
     * @return the result of the list games operation
     */
    public ListGamesResult HTTPToListGamesResult (String responseBody){
        return null;
    }
}
