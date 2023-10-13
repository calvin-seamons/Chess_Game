package dataAccess;

import Models.Authtoken;

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
    public Authtoken readAuthToken(String authToken) throws DataAccessException{
        return null;
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
