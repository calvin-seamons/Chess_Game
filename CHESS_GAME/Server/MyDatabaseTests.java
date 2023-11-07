import Handlers.RegisterHandler;
import Models.Authtoken;
import Models.Game;
import Models.User;
import Requests.AuthTokenRequest;
import Requests.RegisterRequest;
import Results.RegisterResult;
import Services.ClearApplicationService;
import chess.ChessGame;
import com.google.gson.Gson;
import com.sun.source.doctree.AuthorTree;
import dataAccess.*;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.*;

import java.util.Scanner;

public class MyDatabaseTests {
    private static User existingUser;
    private static User newUser;
    private String existingAuth;
    private static Database THE_DATABASE;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;


    @BeforeAll
    public static void init() {
        existingUser = new User();
        existingUser.setUsername("Calvin");
        existingUser.setPassword("Seamons");
        existingUser.setEmail("calvinseamons35@gmail.com");

        newUser = new User();
        newUser.setUsername("testUsername");
        newUser.setPassword("testPassword");
        newUser.setEmail("testEmail");

        THE_DATABASE = new Database();
        authDAO = new AuthDAO();
        gameDAO = new GameDAO();
        userDAO = new UserDAO();
    }


    @BeforeEach
    public void setup() throws DataAccessException {
        ClearApplicationService clearApplicationService = new ClearApplicationService();
        clearApplicationService.clearApplication(authDAO, gameDAO, userDAO, THE_DATABASE);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(existingUser.getUsername());
        registerRequest.setPassword(existingUser.getPassword());
        registerRequest.setEmail(existingUser.getEmail());

        //one user already registered
        String regResult = new RegisterHandler().registerRequestToHTTP(registerRequest, null);
        Gson gson = new Gson();
        RegisterResult registerResult = gson.fromJson(regResult, RegisterResult.class);
        existingAuth = registerResult.getAuthToken();

        Authtoken auth = new Authtoken();
        auth = gson.fromJson(regResult, auth.getClass());

        userDAO.createUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail(), THE_DATABASE);
        authDAO.createAuth(auth, THE_DATABASE);

        //Create A Test Game
        Game game = new Game();
        game.setGameName("Test Game");
        game.setGameID(1435);
        game.setGameImplementation("Test Implementation");
    }

    private Game createTestGame(){
        Game game = new Game();
        game.setGameName("Test Game");
        game.setGameID(1435);
        game.setGameImplementation("Test Implementation");

        return game;
    }

    @Test
    @DisplayName("Create Game Positive Test")
    @Order(1)
    public void createGameP() throws DataAccessException {
        Game game = createTestGame();

        gameDAO.createGame(game, THE_DATABASE);

        Assertions.assertTrue(gameDAO.readGame(game.getGameID(), THE_DATABASE), "Game not found in database");
    }

    @Test
    @DisplayName("Create Game Negative Test")
    @Order(2)
    public void createGameN(){
        Game game = createTestGame();

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(game, THE_DATABASE);
            gameDAO.createGame(game, THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Update Game Positive Test")
    @Order(3)
    public void updateGameP() throws DataAccessException {
        Game game = createTestGame();

        gameDAO.createGame(game, THE_DATABASE);

        game.setGameName("Test Game 2");
        game.setGameImplementation("Test Implementation 2");

        gameDAO.updateGame(game, THE_DATABASE);

        Assertions.assertTrue(gameDAO.readGame(game.getGameID(), THE_DATABASE), "Game not found in database");
    }

    @Test
    @DisplayName("Update Game Negative Test")
    @Order(4)
    public void updateGameN() throws DataAccessException {
        Game game = createTestGame();

        Game falseGame = new Game();
        falseGame.setGameName("Test Game 2");
        falseGame.setGameID(1436);
        falseGame.setGameImplementation("Test Implementation 2");

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.updateGame(falseGame, THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Read Game Positive Test")
    @Order(5)
    public void readGameP() throws DataAccessException {
        Game game = createTestGame();

        gameDAO.createGame(game, THE_DATABASE);

        Assertions.assertTrue(gameDAO.readGame(game.getGameID(), THE_DATABASE), "Game not found in database");
    }

    @Test
    @DisplayName("Read Game Negative Test")
    @Order(6)
    public void readGameN() throws DataAccessException {
        Game game = createTestGame();

        gameDAO.createGame(game, THE_DATABASE);

        Assertions.assertFalse(gameDAO.readGame(1439, THE_DATABASE), "Game found in database");
    }

    @Test
    @DisplayName("Delete Game Positive Test")
    @Order(7)
    public void deleteGameP() throws DataAccessException {
        Game game = createTestGame();

        gameDAO.createGame(game, THE_DATABASE);

        Assertions.assertTrue(gameDAO.readGame(game.getGameID(), THE_DATABASE), "Game not found in database");

        gameDAO.deleteGame(game.getGameID(), THE_DATABASE);

        Assertions.assertFalse(gameDAO.readGame(game.getGameID(), THE_DATABASE), "Game found in database");
    }

    @Test
    @DisplayName("Delete Game Negative Test")
    @Order(8)
    public void deleteGameN() throws DataAccessException {
        Game game = createTestGame();

        gameDAO.createGame(game, THE_DATABASE);

        Assertions.assertTrue(gameDAO.readGame(game.getGameID(), THE_DATABASE), "Game not found in database");

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.deleteGame(1436, THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Insert Game Positive Test")
    @Order(9)
    public void insertGameP() throws DataAccessException {
        Game game = createTestGame();

        gameDAO.insertGame(game, THE_DATABASE);

        Assertions.assertTrue(gameDAO.readGame(game.getGameID(), THE_DATABASE), "Game not found in database");
    }

    @Test
    @DisplayName("Insert Game Negative Test")
    public void insertGameN(){
        Game game = createTestGame();

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.insertGame(game, THE_DATABASE);
            gameDAO.insertGame(game, THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Find All Games Positive Test")
    @Order(10)
    public void FindAllP() throws DataAccessException {
        Game game = createTestGame();

        Game game2 = new Game();
        game2.setGameName("Test Game 2");
        game2.setGameID(1436);
        game2.setGameImplementation("Test Implementation 2");

        gameDAO.insertGame(game, THE_DATABASE);
        gameDAO.insertGame(game2, THE_DATABASE);

        Assertions.assertDoesNotThrow(() -> {
            gameDAO.findAll(THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Find All Games Negative Test")
    @Order(11)
    public void FindAllN() throws DataAccessException {
        Game game = createTestGame();

        Game game2 = new Game();
        game2.setGameName("Test Game 2");
        game2.setGameID(1436);
        game2.setGameImplementation("Test Implementation 2");

        gameDAO.insertGame(game, THE_DATABASE);
        gameDAO.insertGame(game2, THE_DATABASE);

        Assertions.assertThrows(NullPointerException.class, () -> {
            gameDAO.findAll(null);
        });
    }

    @Test
    @DisplayName("Claim Spot Positive Test")
    @Order(12)
    public void claimSpotP() throws DataAccessException {
        Game game = createTestGame();
        gameDAO.insertGame(game, THE_DATABASE);

        Assertions.assertDoesNotThrow(() -> {
            gameDAO.claimSpot(game.getGameID(), ChessGame.TeamColor.WHITE, existingUser.getUsername(), THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Claim Spot Negative Test")
    @Order(13)
    public void claimSpotN(){
        Game game = createTestGame();

        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.claimSpot(4839, ChessGame.TeamColor.WHITE, existingUser.getUsername(), THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Create User Positive Test")
    @Order(14)
    public void createUserP(){
        Assertions.assertDoesNotThrow(() -> {
            userDAO.createUser(newUser.getUsername(), newUser.getPassword(), newUser.getEmail(), THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Create User Negative Test")
    @Order(15)
    public void createUserN(){
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(existingUser.getUsername(), newUser.getPassword(), newUser.getEmail(), THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Read User Positive Test")
    @Order(16)
    public void readUserP() throws DataAccessException {
        Assertions.assertNotNull(userDAO.readUser(existingUser, THE_DATABASE), "User not found in database");
    }

    @Test
    @DisplayName("Read User Negative Test")
    @Order(17)
    public void readUserN() throws DataAccessException {
        Assertions.assertThrows(NullPointerException.class, () -> {
            userDAO.readUser(existingUser, null);
        });

        User falseUser = new User();
        falseUser.setUsername("falseUsername");
        falseUser.setPassword("falsePassword");
        falseUser.setEmail("falseEmail");

        Assertions.assertNull(userDAO.readUser(falseUser, THE_DATABASE), "User found in database");
    }

    @Test
    @DisplayName("Update User Positive Test")
    @Order(18)
    public void updateUserP() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.updateUser(existingUser.getUsername(), newUser, THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Update User Negative Test")
    @Order(19)
    public void updateUserN() throws DataAccessException {
        Assertions.assertThrows(NullPointerException.class, () -> {
            userDAO.updateUser(existingUser.getUsername(), newUser, null);
        });

        Assertions.assertThrows(DataAccessException.class, () -> {
            User falseUser = new User();
            falseUser.setUsername("falseUsername");
            falseUser.setPassword("falsePassword");
            falseUser.setEmail("falseEmail");
            userDAO.updateUser(falseUser.getUsername(), newUser, THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Delete User Positive Test")
    @Order(20)
    public void deleteUserP() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.deleteUser(existingUser.getUsername(), THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Delete User Negative Test")
    @Order(21)
    public void deleteUserN() throws DataAccessException {
        Assertions.assertThrows(NullPointerException.class, () -> {
            userDAO.deleteUser(existingUser.getUsername(), null);
        });

        Assertions.assertThrows(DataAccessException.class, () -> {
            User falseUser = new User();
            falseUser.setUsername("falseUsername");
            falseUser.setPassword("falsePassword");
            falseUser.setEmail("falseEmail");
            userDAO.deleteUser(falseUser.getUsername(), THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Create Auth Positive Test")
    @Order(22)
    public void createAuthP() throws DataAccessException {
        Authtoken auth = new Authtoken();
        auth.setAuthToken("testAuthToken");
        auth.setUsername("testUsername");

        Assertions.assertDoesNotThrow(() -> {
            authDAO.createAuth(auth, THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Create Auth Negative Test")
    @Order(23)
    public void createAuthN() throws DataAccessException {
        Authtoken auth = new Authtoken();
        auth.setAuthToken(existingAuth);
        auth.setUsername(existingUser.getUsername());

        Assertions.assertThrows(NullPointerException.class, () -> {
            authDAO.createAuth(auth, null);
        });
    }

    @Test
    @DisplayName("Read Auth Positive Test")
    @Order(24)
    public void readAuthP() throws DataAccessException {
        AuthTokenRequest authTokenRequest = new AuthTokenRequest();
        authTokenRequest.setAuthToken(existingAuth);
        authTokenRequest.setUsername(existingUser.getUsername());

        Assertions.assertTrue(authDAO.readAuthToken(authTokenRequest, THE_DATABASE), "Auth not found in database");
    }

    @Test
    @DisplayName("Read Auth Negative Test")
    @Order(25)
    public void readAuthN() throws DataAccessException {
        AuthTokenRequest authTokenRequest = new AuthTokenRequest();
        authTokenRequest.setAuthToken(existingAuth);
        authTokenRequest.setUsername(existingUser.getUsername());

        Assertions.assertThrows(NullPointerException.class, () -> {
            authDAO.readAuthToken(authTokenRequest, null);
        });

        AuthTokenRequest falseAuthTokenRequest = new AuthTokenRequest();
        falseAuthTokenRequest.setAuthToken("falseAuthToken");
        falseAuthTokenRequest.setUsername("falseUsername");

        Assertions.assertFalse(authDAO.readAuthToken(falseAuthTokenRequest, THE_DATABASE), "Auth found in database");
    }

    @Test
    @DisplayName("Update Auth Positive Test")
    @Order(26)
    public void updateAuthP() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            authDAO.updateAuthToken(existingUser.getUsername(), "newAuthToken", THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Update Auth Negative Test")
    @Order(27)
    public void updateAuthN() throws DataAccessException {
        Assertions.assertThrows(NullPointerException.class, () -> {
            authDAO.updateAuthToken(existingUser.getUsername(), "newAuthToken", null);
        });

        Assertions.assertFalse(authDAO.updateAuthToken("falseUsername", "newAuthToken", THE_DATABASE), "Auth found in database");
    }

    @Test
    @DisplayName("Delete Auth Positive Test")
    @Order(28)
    public void deleteAuthP() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            authDAO.deleteAuthToken(existingAuth, THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Delete Auth Negative Test")
    @Order(29)
    public void deleteAuthN(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            authDAO.deleteAuthToken(existingAuth, null);
        });

        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.deleteAuthToken("falseAuthToken", THE_DATABASE);
        });
    }

    @Test
    @DisplayName("Get Username Positive Test")
    @Order(30)
    public void getUsernameP() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            authDAO.getUserName(existingAuth, THE_DATABASE);
        });

        Assertions.assertEquals(existingUser.getUsername(), authDAO.getUserName(existingAuth, THE_DATABASE));
    }

    @Test
    @DisplayName("Get Username Negative Test")
    @Order(31)
    public void getUsernameN() throws DataAccessException {
        Assertions.assertThrows(NullPointerException.class, () -> {
            authDAO.getUserName(existingAuth, null);
        });

        Assertions.assertNull(authDAO.getUserName("falseAuthToken", THE_DATABASE));
    }

    @Test
    @DisplayName("Get AuthToken Positive Test")
    @Order(32)
    public void getAuthTokenP() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            authDAO.getAuthToken(existingUser.getUsername(), THE_DATABASE);
        });

        Assertions.assertEquals(existingAuth, authDAO.getAuthToken(existingUser.getUsername(), THE_DATABASE));
    }

    @Test
    @DisplayName("Get AuthToken Negative Test")
    @Order(33)
    public void getAuthTokenN() throws DataAccessException {
        Assertions.assertThrows(NullPointerException.class, () -> {
            authDAO.getAuthToken(existingUser.getUsername(), null);
        });

        Assertions.assertNull(authDAO.getAuthToken("falseUsername", THE_DATABASE));
    }

    @Test
    @DisplayName("Get Database AuthTokens Positive Test")
    @Order(34)
    public void getDatabaseTokensP() throws DataAccessException {
        Authtoken newToken = new Authtoken();
        newToken.setAuthToken("newToken");
        newToken.setUsername("newUsername");

        Authtoken newToken2 = new Authtoken();
        newToken2.setAuthToken("newToken2");
        newToken2.setUsername("newUsername2");

        authDAO.createAuth(newToken, THE_DATABASE);
        authDAO.createAuth(newToken2, THE_DATABASE);

        Assertions.assertDoesNotThrow(() -> {
            authDAO.getDatabaseAuthtokens(THE_DATABASE);
        });

        Assertions.assertEquals(3, authDAO.getDatabaseAuthtokens(THE_DATABASE).size());
    }

    @Test
    @DisplayName("Get Database AuthTokens Negative Test")
    @Order(35)
    public void getDatabaseTokensN() throws DataAccessException {
        Assertions.assertThrows(NullPointerException.class, () -> {
            authDAO.getDatabaseAuthtokens(null);
        });
    }

    @Test
    @DisplayName("Clear the Database Test")
    @Order(36)
    public void clearDatabase() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> {
            ClearApplicationService clearApplicationService = new ClearApplicationService();
            clearApplicationService.clearApplication(authDAO, gameDAO, userDAO, THE_DATABASE);
        });

        Assertions.assertEquals(0, authDAO.getDatabaseAuthtokens(THE_DATABASE).size());
        Assertions.assertEquals(0, gameDAO.findAll(THE_DATABASE).size());
    }
}

