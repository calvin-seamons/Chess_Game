package dataAccess;

import Models.Authtoken;

/**
 * AuthDAO class that stores the authtoken
 */
public class AuthDAO {

    /**
     * Creates a new authtoken
     * @param username This is the username of the user
     * @return Created authtoken
     * @throws DataAccessException
     */
    public Authtoken createAuthToken(String username) throws DataAccessException {
        return null;
    }

    /**
     * Reads the authtoken from the database
     * @param authToken This is your authtoken
     * @throws DataAccessException
     * @return Authtoken
     */
    public boolean readAuthToken(String authToken) throws DataAccessException{
        return true;
    }

    /**
     * Updates the authtoken in the database
     * @param authToken This is your authtoken
     * @throws DataAccessException
     */
    public void updateAuthToken(String authToken) throws DataAccessException{

    }

    /**
     * Deletes the authtoken from the database
     * @param authToken This is your authtoken
     * @throws DataAccessException
     */
    public void deleteAuthToken(String authToken) throws DataAccessException{}


}
