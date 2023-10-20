package dataAccess;

import Models.User;

/**
 * UserDAO class that stores the user
 */
public class UserDAO {
    /**
     * Creates a new user
     * @throws DataAccessException
     * @return Created user
     */
    public User createUser(User user) throws DataAccessException{
        return null;
    }

    /**
     * Reads the user from the database
     * @param id This is the id of the user to be read
     * @return User with the given id
     * @throws DataAccessException
     */
    public User readUser(int id) throws DataAccessException{
        return null;
    }

    /**
     * Updates the user in the database
     * @param user This is the user to update in the database
     * @throws DataAccessException
     */
    public void updateUser(User user) throws DataAccessException{}

    /**
     * Deletes the user from the database
     * @param id This is the id of the user to be deleted
     * @throws DataAccessException
     */
    public void deleteUser(int id) throws DataAccessException{}
}
