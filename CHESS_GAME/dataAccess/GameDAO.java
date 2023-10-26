package dataAccess;

import Models.Game;

/**
 * GameDAO class that has methods to create, read, update, and delete games
 */
public class GameDAO {
    /**
     * Creates a new game
     * @throws DataAccessException
     * @return gameID
     */
    public String createGame(String gameName) throws DataAccessException{
        return null;
    }

    /**
     * Reads the game from the database
     * @param id This is the id of the game to be read
     * @return Game with the given id
     * @throws DataAccessException If there is an error reading from the database
     */
    public Game readGame(int id) throws DataAccessException{
        return null;
    }

    /**
     * Updates the game in the database
     * @param game This is the game to update in the database
     * @throws DataAccessException
     */
    public void updateGame(Game game) throws DataAccessException{
    }

    /**
     * Deletes the game from the database
     * @param id This is the id of the game to be deleted
     * @throws DataAccessException
     */
    public void deleteGame(int id) throws DataAccessException{
    }

    /**
     * Inserts the game into the database
     * @param game This is the game to be inserted into the database
     * @throws DataAccessException
     */
    public void insertGame(Game game) throws DataAccessException{
    }

    /**
     * Finds all games in the database
     *
     * @return Collection of games
     * @throws DataAccessException
     */
    public Game[] findAll() throws DataAccessException{
        return null;
    }

    /**
     * Claims a spot in the game
     * @param gameID This is the id of the game
     * @param username This is the username of the user
     * @throws DataAccessException
     */
    public void claimSpot (int gameID, String username) throws DataAccessException{}



}
