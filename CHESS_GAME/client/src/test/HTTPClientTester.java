import Models.User;
import Results.*;
import org.junit.jupiter.api.*;
import ui.HTTPClient;


import javax.swing.*;

public class HTTPClientTester {
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_FORBIDDEN = 403;

    private static User existingUser;
    private static User newUser;
    private String existingAuth;
    private static HTTPClient serverFacade;
    private static ChessClient client;


    @BeforeAll
    public static void init() {
        serverFacade = new HTTPClient("http://localhost:8080");

        existingUser = new User();
        existingUser.setUsername("Calvin");
        existingUser.setPassword("Seamons");
        existingUser.setEmail("calvinseamons35@gmail.com");

        newUser = new User();
        newUser.setUsername("testUsername");
        newUser.setPassword("testPassword");
        newUser.setEmail("testEmail");
    }


    @BeforeEach
    public void setup() {
        serverFacade.clear();

        //one user already logged in
        Results.RegisterResult result = serverFacade.register(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());
//        LoginResult result = serverFacade.login(existingUser.getUsername(), existingUser.getPassword());
        existingAuth = result.getAuthToken();
    }

    @Test
    @Order(1)
    @DisplayName("Register Positive Test")
    public void testRegisterPositive() {
        // Registers a new user
        RegisterResult result = serverFacade.register(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        Assertions.assertNull(result.getMessage());
        Assertions.assertNotNull(result.getAuthToken());
        Assertions.assertNotNull(result.getUsername());
    }

    @Test
    @Order(2)
    @DisplayName("Register Negative Test")
    public void testRegisterNegative() {
        // Tries to register a user that already exists
        RegisterResult result = serverFacade.register(existingUser.getUsername(), existingUser.getPassword(), existingUser.getEmail());
        Assertions.assertNotNull(result.getMessage());
        Assertions.assertNull(result.getAuthToken());
        Assertions.assertNull(result.getUsername());
    }

    @Test
    @Order(3)
    @DisplayName("Login Positive Test")
    public void testLoginPositive() {
        // Logs in an existing user
        LoginResult result = serverFacade.login(existingUser.getUsername(), existingUser.getPassword());
        Assertions.assertNull(result.getMessage(), "Error message is not null");
        Assertions.assertNotNull(result.getAuthToken(), "Auth token is null");
        Assertions.assertNotNull(result.getUsername(), "Username is null");
    }

    @Test
    @Order(4)
    @DisplayName("Login Negative Test")
    public void testLoginNegative() {
        // Tries to log in a user that doesn't exist
        LoginResult result = serverFacade.login(newUser.getUsername(), newUser.getPassword());
        Assertions.assertNotNull(result.getMessage(), "Error message is null");
        Assertions.assertNull(result.getAuthToken(), "Auth token isn't null");
        Assertions.assertNull(result.getUsername(), "Username isn't null");
    }

    @Test
    @Order(5)
    @DisplayName("Create Game Positive Test")
    public void testCreateGamePositive() {
        // Creates a new game
        String gameName = "testGame";
        String authToken = existingAuth;
        CreateGameResult result = serverFacade.createGame(gameName, authToken);
        Assertions.assertEquals(result.getMessage(), "");
        Assertions.assertNotNull(result.getGameID(), "Game ID is null");
    }

    @Test
    @Order(6)
    @DisplayName("Create Game Negative Test")
    public void testCreateGameNegative(){
        // Create 2 games with the same name
        String gameName = "testGame";
        String authToken = existingAuth;
        CreateGameResult result = serverFacade.createGame(gameName, authToken);

        Assertions.assertEquals(result.getMessage(), "");
        Assertions.assertNotNull(result.getGameID(), "Game ID is null");

        CreateGameResult result2 = serverFacade.createGame(gameName, authToken);

        Assertions.assertNotNull(result2.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("Join Game Positive Test")
    public void testJoinGamePositive(){
        // Join a game that exists
        String gameName = "testGame";
        String authToken = existingAuth;
        CreateGameResult result = serverFacade.createGame(gameName, authToken);

        Assertions.assertEquals(result.getMessage(), "");
        Assertions.assertNotNull(result.getGameID(), "Game ID is null");

        int gameID = result.getGameID();
        JoinGameResult joinGameResult = serverFacade.joinGame(gameID, "white", authToken);

        Assertions.assertNull(joinGameResult.getMessage(), "Join game message is not null");
    }

    @Test
    @Order(8)
    @DisplayName("Join Game Negative Test")
    public void testJoinGameNegative(){
        // Join a game that doesn't exist
        String gameName = "testGame";
        String authToken = existingAuth;
        CreateGameResult result = serverFacade.createGame(gameName, authToken);

        Assertions.assertEquals(result.getMessage(), "");
        Assertions.assertNotNull(result.getGameID(), "Game ID is null");

        int gameID = result.getGameID();
        JoinGameResult joinGameResult = serverFacade.joinGame(gameID + 1, "white", authToken);

        Assertions.assertNotNull(joinGameResult.getMessage(), "Join game message is null");
    }

    @Test
    @Order(9)
    @DisplayName("List Games Positive Test")
    public void testListGamesPositive(){
        // List games when there are games
        String gameName = "testGame";
        String authToken = existingAuth;
        CreateGameResult result = serverFacade.createGame(gameName, authToken);

        Assertions.assertEquals(result.getMessage(), "");
        Assertions.assertNotNull(result.getGameID(), "Game ID is null");

        ListGamesResult listGamesResult = serverFacade.listGames(authToken);

        Assertions.assertNull(listGamesResult.getMessage(), "List games message is not null");
    }

    @Test
    @Order(10)
    @DisplayName("List Games Negative Test")
    public void testListGamesNegative(){
        // List games when there are no games
        String gameName = "testGame";
        String authToken = existingAuth;

        ListGamesResult listGamesResult = serverFacade.listGames(authToken);

        Assertions.assertNotNull(listGamesResult.getGames(), "List games message is null");
    }

    @Test
    @Order(11)
    @DisplayName("Logout Positive Test")
    public void testLogoutPositive(){
        // Logout when logged in
        String authToken = existingAuth;

        LogoutResult logoutResult = serverFacade.logout(authToken);

        Assertions.assertNull(logoutResult.getMessage(), "Logout message is not null");
    }

    @Test
    @Order(12)
    @DisplayName("Logout Negative Test")
    public void testLogoutNegative(){
        // Logout when not logged in
        String authToken = existingAuth;

        LogoutResult logoutResult = serverFacade.logout(authToken);

        Assertions.assertNull(logoutResult.getMessage(), "Logout message is not null");

        LogoutResult logoutResult2 = serverFacade.logout(authToken);
        Assertions.assertNotNull(logoutResult2.getMessage(), "Logout message is null");
    }

    @Test
    @Order(13)
    @DisplayName("Clear Database Test")
    public void testClearDatabase(){
        // Clear the database
        String errorMessage;
        errorMessage = serverFacade.clear();

        Assertions.assertNull(errorMessage, "Clear message is not null");
    }

}
