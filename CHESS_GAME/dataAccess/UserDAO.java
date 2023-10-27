package dataAccess;

import Models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO class that stores the user
 */
public class UserDAO {
    private List<User> databaseUsers = new ArrayList<>();
    /**
     * Creates a new user
     *
     * @throws DataAccessException
     */
    public void createUser(String username, String password, String email) throws DataAccessException{
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        databaseUsers.add(user);
        System.out.println("User created");
        // Print out the user from the database
        System.out.println(databaseUsers.get(databaseUsers.size() - 1).getUsername());
    }

    /**
     * Reads the user from the database
     * @param user This is the user to be read
     * @return User with the given id
     * @throws DataAccessException
     */
    public User readUser(User user) throws DataAccessException{
        for (User u : databaseUsers) {
            if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
                return u;
            }
        }
        return null;
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
        System.out.println("Deleted user");
    }

    /**
     * Clears database of all users
     * @throws DataAccessException If there is an error clearing the database
     */
    public void clearUsers() throws DataAccessException{
        this.databaseUsers.clear();
    }

    public void clearUserDatabase() {
        this.databaseUsers.clear();
    }
}
