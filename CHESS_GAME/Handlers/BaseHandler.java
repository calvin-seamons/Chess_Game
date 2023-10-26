package Handlers;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;

import java.util.UUID;

public abstract class BaseHandler {

    protected String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    protected boolean validateAuthToken(String authToken) throws DataAccessException {
        AuthDAO authDAO = new AuthDAO();
        authDAO.readAuthToken(authToken);
        return true;
    }

    // Any other common methods can go here

}