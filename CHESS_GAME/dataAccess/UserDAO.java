package dataAccess;

import Models.User;
import java.util.List;

/**
 * UserDAO class that stores the user
 */
public class UserDAO {
    private List<User> databaseUsers;
    /**
     * Creates a new user
     * @throws DataAccessException
     * @return Created user
     */
    public User createUser(User user) throws DataAccessException{
        databaseUsers.add(user);
        return user;
    }

    /**
     * Reads the user from the database
     * @param username This is the username of the user to be read
     * @return User with the given id
     * @throws DataAccessException
     */
    public User readUser(String username) throws DataAccessException{
        User user = null;
        for (User u : databaseUsers) {
            if (u.getUsername().equals(username)) {
                user = u;
                break;
            }
        }
        return user;
    }

    /**
     * Updates the user in the database
     * @param oldUser This is the user to update in the database
     * @param newUser This is the user to update in the database
     * @throws DataAccessException
     */
    public void updateUser(User oldUser, User newUser) throws DataAccessException{
        for (User u : databaseUsers) {
            if (u.getUsername().equals(oldUser.getUsername())) {
                u = newUser;
                break;
            }
        }
    }

    /**
     * Deletes the user from the database
     * @param username This is the username of the user to be deleted
     * @throws DataAccessException
     */
    public void deleteUser(String username) throws DataAccessException{
        for(int i = 0; i < databaseUsers.size(); i++) {
            if (databaseUsers.get(i).getUsername().equals(username)) {
                databaseUsers.remove(i);
                break;
            }
        }
    }

    /**
     * Clears database of all users
     * @throws DataAccessException If there is an error clearing the database
     */
    public void clearUsers() throws DataAccessException{
        this.databaseUsers.clear();
    }
}
