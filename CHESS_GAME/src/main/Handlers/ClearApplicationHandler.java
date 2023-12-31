package main.Handlers;

import Results.ClearApplicationResult;
import com.google.gson.Gson;

/**
 * ClearApplicationHandler class that has a constructor and methods
 */
public class ClearApplicationHandler {
    public ClearApplicationHandler(){}

    /**
     * Creates a clearApplication HTTP string
     * @return a clearApplication HTTP string
     */
    public String clearApplicationRequestToHTTP(){
        Gson gson = new Gson();
        ClearApplicationResult result = new ClearApplicationResult();
        return gson.toJson(result);
    }
}
