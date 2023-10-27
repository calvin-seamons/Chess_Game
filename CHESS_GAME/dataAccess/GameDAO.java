package dataAccess;

import Models.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * GameDAO class that has methods to create, read, update, and delete games
 */
public class GameDAO {
    private List<Game> databaseGames = new ArrayList<>();
    /**
     * Creates a new game
     * @throws DataAccessException If there is an error creating the game
     * @return gameID
     */
    public String createGame(Game game) throws DataAccessException{
        // Check to see if the game name is already taken
        for (Game g : databaseGames) {
            if (g.getGameName().equals(game.getGameName())) {
                return null;
            }
        }
        databaseGames.add(game);

        // Print out all the games in the database
        for (Game g : databaseGames) {
            System.out.println(g.getGameName());
        }

        return game.getGameId();
    }

    public String getNewGameID() {
        // Create a 4 number random gameID
        String gameID = "";
        for (int i = 0; i < 4; i++) {
            gameID += (int) (Math.random() * 10);
        }
        return gameID;
    }

    /**
     * Reads the game from the database
     * @param id This is the id of the game to be read
     * @return Game with the given id
     * @throws DataAccessException If there is an error reading from the database
     */
    public boolean readGame(String id) throws DataAccessException{
        for(Game g : databaseGames){
            if(g.getGameId().equals(id)){
                return true;
            }
        }
        return false;
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
    public List<Game> findAll() throws DataAccessException{
        return List.of(databaseGames.toArray(new Game[databaseGames.size()]));
    }

    /**
     * Claims a spot in the game
     * @param gameID This is the id of the game
     * @param username This is the username of the user
     * @throws DataAccessException
     */
    public void claimSpot (int gameID, String username) throws DataAccessException{}


    public void clearGameDatabase() {
        this.databaseGames.clear();
    }

    public boolean checkDuplicateName(String gameName) {
        for(Game g : databaseGames){
            if(g.getGameName().equals(gameName)){
                return true;
            }
        }
        return false;
    }
}
