package dataAccess;

import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO class that stores the user
 */
public class UserDAO {
    private final List<User> databaseUsers = new ArrayList<>();
    private static final String INSERT_USER_SQL = "INSERT INTO users (user, password, email) VALUES (?, ?, ?);"; // Assuming your table is named 'users'
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM users;";
    private static final String UPDATE_USER_SQL = "UPDATE users SET password = ?, email = ? WHERE user = ?;";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE user = ?;";
    private static final String DELETE_ALL_USERS_SQL = "DELETE FROM users;";



    /**
     * Creates a new user
     * @param username This is your username
     * @param password This is your password
     * @param email This is your email
     * @param db This is your database
     * @throws DataAccessException If there is an error creating the user
     */
    public void createUser(String username, String password, String email, Database db) throws DataAccessException{
        if(readUser(new User(username, password, email), db) != null) {
            throw new DataAccessException("Duplicate User");
        }
        else if(username == null || password == null || email == null) {
            throw new DataAccessException("Null values for user");
        }

        // Obtain a connection from the database pool
        try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL)) {
            // Set the parameters for the SQL statement
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);

            // Execute the update
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User created successfully!");
            }
        } catch (SQLException e) {
            // Handle SQL exceptions (e.g., if the SQL statement is incorrect or there is a database access issue)
            throw new DataAccessException("Error inserting user into the database");
        }
    }

    /**
     * Reads the user from the database
     * @param user This is the user to be read
     * @return User with the given id
     * @throws DataAccessException If there is an error reading the user
     */
    public User readUser(User user, Database db) throws DataAccessException{
        List<User> userList = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_USERS_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User currentUser = new User();
                currentUser.setUsername(rs.getString("user"));
                currentUser.setPassword(rs.getString("password"));
                currentUser.setEmail(rs.getString("email"));
                userList.add(currentUser);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error reading users from the database");
        }

        System.out.println("User read");

        for (User u : userList) {
            if (u.getUsername().equals(user.getUsername())) {
                return u;
            }
        }
        return null;
    }

    /**
     * Updates the user in the database
     * @param username This is the username of the user to update
     * @param newUser This is the user to update in the database
     * @param db This is the database
     * @throws DataAccessException If there is an error updating the user
     */
    public void updateUser(String username, User newUser, Database db) throws DataAccessException{
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_USER_SQL)) {

            // Set the values for each parameter based on the user input
            pstmt.setString(1, newUser.getPassword());
            pstmt.setString(2, newUser.getEmail());
            pstmt.setString(3, username);

            // Execute the update
            int affectedRows = pstmt.executeUpdate();

            // Check the affected rows
            if (affectedRows > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("No user was updated.");
                throw new DataAccessException("Error updating user in the database");
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            throw new DataAccessException("Error updating user in the database");
        }
    }

    /**
     * Deletes the user from the database
     *
     * @param username This is the username of the user to be deleted
     * @param db This is the database
     * @throws DataAccessException If there is an error deleting the user
     */
    public void deleteUser(String username, Database db) throws DataAccessException{
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_USER_SQL)) {

            pstmt.setString(1, username);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("No user was deleted. User may not exist.");
                throw new DataAccessException("Error deleting user from the database");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting user from the database");
        }
    }

    /**
     * Clears database of all users
     * @throws DataAccessException If there is an error clearing the database
     */
    public void clearUserDatabase(Database db) throws DataAccessException {
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_USERS_SQL)) {

            int affectedRows = pstmt.executeUpdate();

            System.out.println("Deleted " + affectedRows + " users from the database.");
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error deleting all users from the database");
        }


        this.databaseUsers.clear();
    }
}
