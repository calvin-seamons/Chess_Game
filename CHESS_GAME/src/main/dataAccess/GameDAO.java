package main.dataAccess;

import Models.Game;
import chess.ChessGame;
import chess.Chess_Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * GameDAO class that has methods to create, read, update, and delete games
 */
public class GameDAO {
    private List<Game> databaseGames = new ArrayList<>();
    private static final String INSERT_GAME_SQL = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, gameImplementation) VALUES (?, ?, ?, ?, ?);";
    final String CHECK_GAME_SQL = "SELECT count(*) FROM games WHERE gameName = ?";
    final String QUERY_GAME_SQL = "SELECT count(*) FROM games WHERE gameId = ?";
    final String GET_GAME_SQL = "SELECT * FROM games WHERE gameId = ?";
    final String UPDATE_SQL = "UPDATE games SET %s = ? WHERE gameID = ?";
    final String UPDATE_GAME_SQL = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameImplementation = ? WHERE gameID = ?";
    final String DELETE_GAME_SQL = "DELETE FROM games WHERE gameID = ?";
    final String FIND_ALL_GAMES_SQL = "SELECT * FROM games";
    final String CLEAR_GAME_DATABASE_SQL = "DELETE FROM games"; // Replace 'games' with your actual table name




    /**
     * Creates a new game
     *
     * @throws DataAccessException If there is an error creating the game
     */
    public void createGame(Game game, Database db) throws DataAccessException{
        if (gameExists(game.getGameName(), db)) {
            throw new DataAccessException("Game already exists in the database");
        }

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_GAME_SQL)) {

            pstmt.setInt(1, game.getGameID());
            pstmt.setString(2, game.getWhiteUsername());
            pstmt.setString(3, game.getBlackUsername());
            pstmt.setString(4, game.getGameName());

            String json = gameToJSON(new Chess_Game());
            pstmt.setString(5, json);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating game failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating the game in the database");
        }

    }

    public boolean gameExists(String gameName, Database db) throws DataAccessException {
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_GAME_SQL)) {

            pstmt.setString(1, gameName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // If count is greater than 0, then the game name already exists
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking for existing game name in the database");
        }
        return false;
    }

    public int getNewGameID() {
        // Create a 4 number random gameID
        return (int) (Math.random() * 9000) + 1000;
    }

    /**
     * Reads the game from the database
     * @param gameID This is the id of the game to be read
     * @return Game with the given id
     * @throws DataAccessException If there is an error reading from the database
     */
    public boolean readGame(int gameID, Database db) throws DataAccessException{
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(QUERY_GAME_SQL)) {

            pstmt.setInt(1, gameID);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error reading the game from the database");
        }
    }

    /**
     * Updates the game in the database
     * @param game This is the game to update in the database
     * @throws DataAccessException
     */
    public void updateGame(Game game, Database db) throws DataAccessException{
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_GAME_SQL)) {

            pstmt.setString(1, game.getWhiteUsername());
            pstmt.setString(2, game.getBlackUsername());
            pstmt.setString(3, game.getGameName());
            pstmt.setString(4, game.getGameImplementation());
            pstmt.setInt(5, game.getGameID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating game failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating the game in the database");
        }
    }

    /**
     * Deletes the game from the database
     * @param gameID This is the id of the game to be deleted
     * @throws DataAccessException
     */
    public void deleteGame(int gameID, Database db) throws DataAccessException{
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_GAME_SQL)) {

            pstmt.setInt(1, gameID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting game failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting the game from the database");
        }
    }

    /**
     * Inserts the game into the database
     * @param game This is the game to be inserted into the database
     * @throws DataAccessException If there is an error inserting the game into the database
     */
    public void insertGame(Game game, Database db) throws DataAccessException{
        if(gameExists(game.getGameName(), db)){
            throw new DataAccessException("Gamwe already exists in the database");
        }

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_GAME_SQL)) {

            // Set the parameters for the PreparedStatement
            pstmt.setInt(1, game.getGameID());
            pstmt.setString(2, game.getWhiteUsername());
            pstmt.setString(3, game.getBlackUsername());
            pstmt.setString(4, game.getGameName());
            pstmt.setString(5, game.getGameImplementation());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting game failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error inserting the game into the database");
        }
    }

    /**
     * Finds all games in the database
     * @param db This is the database
     * @return Collection of games
     * @throws DataAccessException If there is an error finding all games in the database
     */
    public List<Game> findAll(Database db) throws DataAccessException{
        List<Game> games = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(FIND_ALL_GAMES_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Game game = new Game();
                game.setGameID(rs.getInt("gameID"));
                game.setWhiteUsername(rs.getString("whiteUsername"));
                game.setBlackUsername(rs.getString("blackUsername"));
                game.setGameName(rs.getString("gameName"));
                game.setGameImplementation(rs.getString("gameImplementation"));

                games.add(game);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error finding all games in the database");
        }

        return games;
    }

    /**
     * Claims a spot in the game
     * @param gameID This is the id of the game
     * @param username This is the username of the user
     * @throws DataAccessException If there is an error claiming the spot in the game
     */
    public void claimSpot (int gameID, ChessGame.TeamColor playerColor, String username, Database db) throws DataAccessException{
        // Choose the column based on the player's team color
        //FIXME: Might have to do something with the spectator role
        String colorColumn;
        if(playerColor == ChessGame.TeamColor.WHITE){
            colorColumn = "whiteUsername";
        } else if(playerColor == ChessGame.TeamColor.BLACK){
            colorColumn = "blackUsername";
        } else{
            return;
        }


        String sql = String.format(UPDATE_SQL, colorColumn);

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters for the PreparedStatement
            pstmt.setString(1, username);
            pstmt.setInt(2, gameID);

            // Execute the update
            int affectedRows = pstmt.executeUpdate();

            // Check if the update was successful
            if (affectedRows == 0) {
                throw new SQLException("Updating the game failed, no rows affected.");
            }

        } catch (SQLException e) {
            // Wrap and throw an exception specific to your application
            throw new DataAccessException("Error updating the game record in the database");
        }
    }

    public Game getGame(int gameID, Database db) throws DataAccessException {
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_GAME_SQL)) {

            pstmt.setInt(1, gameID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Game game = new Game();
                    game.setGameID(gameID);
                    game.setWhiteUsername(rs.getString("whiteUsername"));
                    game.setBlackUsername(rs.getString("blackUsername"));
                    game.setGameName(rs.getString("gameName"));
                    game.setGameImplementation(rs.getString("gameImplementation"));

                    return game;
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error reading the game from the database");
        }
        return null;
    }


    public void clearGameDatabase(Database db) throws DataAccessException {
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(CLEAR_GAME_DATABASE_SQL)) {

            // Execute the delete operation
            pstmt.executeUpdate();

        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException("Error clearing the game database");
        }
    }

    private String gameToJSON(Chess_Game game) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(game);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Chess_Game JSONToGame(String json) {
        return new Gson().fromJson(json, Chess_Game.class);
    }
}
