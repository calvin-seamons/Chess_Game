package main.dataAccess;

import Models.Authtoken;
import Requests.AuthTokenRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * AuthDAO class that stores the authtoken
 */
public class AuthDAO {
    private final List<Authtoken> databaseAuthtokens = new ArrayList<>();
    private static final String INSERT_AUTH_SQL = "INSERT INTO authtokens (authtoken, username) VALUES (?, ?);";
    private static final String SELECT_AUTH_BY_AUTHTOKEN_SQL = "SELECT * FROM authtokens WHERE authtoken = ?;";
    private static final String UPDATE_AUTH_SQL = "UPDATE authtokens SET authtoken = ? WHERE username = ?;";
    private static final String DELETE_AUTH_SQL = "DELETE FROM authtokens WHERE authtoken = ?;";
    private static final String SELECT_USERNAME_BY_TOKEN_SQL = "SELECT username FROM authtokens WHERE authtoken = ?;";
    private static final String SELECT_TOKEN_BY_USERNAME_SQL = "SELECT authtoken FROM authtokens WHERE username = ?;";
    private static final String SELECT_ALL_AUTHTOKENS_SQL = "SELECT * FROM authtokens;";
    private static final String DELETE_ALL_AUTHTOKENS_SQL = "DELETE FROM authtokens;";




    /**
     * Creates a new authtoken
     * @param auth This is your authtoken
     * @throws DataAccessException If there is an error creating the authtoken
     */
    public void createAuth(Authtoken auth, Database db) throws DataAccessException{
        if(auth == null) {
            throw new DataAccessException("Error creating the authtoken");
        }

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_AUTH_SQL)) {

            pstmt.setString(1, auth.getAuthToken());
            pstmt.setString(2, auth.getUsername());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating authtoken failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating the authtoken in the database");
        }
    }

    /**
     * Reads the authtoken from the database
     *
     * @param authToken This is your authtoken request
     * @param db
     * @return Authtoken
     * @throws DataAccessException If there is an error reading the authtoken
     */
    public boolean readAuthToken(AuthTokenRequest authToken, Database db) throws DataAccessException{
        boolean tokenExists = false;
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_AUTH_BY_AUTHTOKEN_SQL)) {
            pstmt.setString(1, authToken.getAuthToken());

            try (ResultSet rs = pstmt.executeQuery()) {
                tokenExists = rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error reading the authtoken from the database");
        }
        return tokenExists;
    }

    /**
     * Updates the authtoken in the database
     *
     * @param username     This is your username
     * @param newAuthtoken This is your authtoken you change the old one with
     * @param db
     * @throws DataAccessException If there is an error updating the database
     */
    public boolean updateAuthToken(String username, String newAuthtoken, Database db) throws DataAccessException{
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_AUTH_SQL)) {
            pstmt.setString(1, newAuthtoken);
            pstmt.setString(2, username);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating the authtoken in the database");
        }
    }

    /**
     * Deletes the authtoken from the database
     *
     * @param authToken This is your authtoken
     * @param db
     * @throws DataAccessException
     */
    public void deleteAuthToken(String authToken, Database db) throws DataAccessException{
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_AUTH_SQL)) {
            pstmt.setString(1, authToken);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Deleting authtoken failed, no rows affected.");
            } else {
                System.out.println("Deleted authtoken");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting the authtoken from the database");
        }
    }

    public String getUserName(String authToken, Database db) throws DataAccessException{
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USERNAME_BY_TOKEN_SQL)) {
            pstmt.setString(1, authToken);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            } else {
                return null;  // authToken not found
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving the username from the database");
        }
    }


    public String getAuthToken(String username, Database db) throws DataAccessException {
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_TOKEN_BY_USERNAME_SQL)) {
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("authtoken");
            } else {
                return null;  // username not found
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error retrieving the authtoken from the database");
        }
    }

    public List<Authtoken> getDatabaseAuthtokens(Database db) throws DataAccessException {
        List<Authtoken> authtokens = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_AUTHTOKENS_SQL)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Authtoken authtoken = new Authtoken();
                authtoken.setAuthToken(rs.getString("authtoken"));
                authtoken.setUsername(rs.getString("username"));
                authtokens.add(authtoken);
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error retrieving all auth tokens from the database");
        }
        return authtokens;
    }

    public void clearAuthDatabase(Database db) throws DataAccessException {
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_ALL_AUTHTOKENS_SQL)) {

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting all auth tokens from the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
