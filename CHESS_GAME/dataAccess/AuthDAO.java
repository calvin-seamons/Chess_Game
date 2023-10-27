package dataAccess;

import Models.Authtoken;
import Requests.AuthTokenRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * AuthDAO class that stores the authtoken
 */
public class AuthDAO {
    private List<Authtoken> databaseAuthtokens = new ArrayList<>();


    /**
     * Creates a new authtoken
     * @param auth This is your authtoken
     * @throws DataAccessException If there is an error creating the authtoken
     */
    public void createAuth(Authtoken auth) throws DataAccessException{
        databaseAuthtokens.add(auth);
        System.out.println("Created authtoken");
    }
    /**
     * Reads the authtoken from the database
     * @param authToken This is your authtoken request
     * @throws DataAccessException If there is an error reading the authtoken
     * @return Authtoken
     */
    public boolean readAuthToken(AuthTokenRequest authToken) throws DataAccessException{
        for(Authtoken a : databaseAuthtokens){
            if(a.getAuthToken().equals(authToken.getAuthToken()) && a.getUsername().equals(authToken.getUsername())){
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the authtoken in the database
     * @param username This is your username
     * @param newAuthtoken This is your authtoken you change the old one with
     * @throws DataAccessException If there is an error updating the database
     */
    public void updateAuthToken(String username, String newAuthtoken) throws DataAccessException{
        for(Authtoken a : databaseAuthtokens) {
            if (a.getUsername().equals(username)) {
                a.setAuthToken(newAuthtoken);
                break;
            }
        }
    }

    /**
     * Deletes the authtoken from the database
     * @param authToken This is your authtoken
     * @throws DataAccessException
     */
    public void deleteAuthToken(String authToken) throws DataAccessException{
        for(Authtoken a : databaseAuthtokens){
            if(a.getAuthToken().equals(authToken)){
                databaseAuthtokens.remove(a);
                break;
            }
        }
        System.out.println("Deleted authtoken");
    }

    public String getUserName(String authToken) throws DataAccessException{
        for(Authtoken a : databaseAuthtokens){
            if(a.getAuthToken().equals(authToken)){
                return a.getUsername();
            }
        }
        return null;
    }


    public String getAuthToken(String username) {
        for(Authtoken a : databaseAuthtokens){
            if(a.getUsername().equals(username)){
                return a.getAuthToken();
            }
        }
        return null;
    }

    public List<Authtoken> getDatabaseAuthtokens() {
        return databaseAuthtokens;
    }

    public void clearAuthDatabase() {
        this.databaseAuthtokens.clear();
    }
}
