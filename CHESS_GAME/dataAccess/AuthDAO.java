package dataAccess;

import Models.Authtoken;

import java.util.List;

/**
 * AuthDAO class that stores the authtoken
 */
public class AuthDAO {
    private List<Authtoken> databaseAuthtokens;

    /**
     * Reads the authtoken from the database
     * @param authToken This is your authtoken
     * @throws DataAccessException
     * @return Authtoken
     */
    public boolean readAuthToken(String authToken) throws DataAccessException{
        for (Authtoken a : databaseAuthtokens) {
            if (a.getAuthToken().equals(authToken)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the authtoken in the database
     * @param oldAuthToken This is your authtoken you want to change
     * @param newAuthtoken This is your authtoken you change the old one with
     * @throws DataAccessException If there is an error updating the database
     */
    public void updateAuthToken(String oldAuthToken, String newAuthtoken) throws DataAccessException{
        for(Authtoken a : databaseAuthtokens) {
            if (a.getAuthToken().equals(oldAuthToken)) {
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
    }


}
