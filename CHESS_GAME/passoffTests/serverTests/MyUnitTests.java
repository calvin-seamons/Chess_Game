package passoffTests.serverTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoffTests.TestFactory;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestModels;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

public class MyUnitTests {
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_FORBIDDEN = 403;

    private static TestModels.TestUser existingUser;
    private static TestModels.TestUser newUser;
    private static TestModels.TestCreateRequest createRequest;
    private static TestServerFacade theServer;
    private String existingAuth;


    @BeforeAll
    public static void init() {
        existingUser = new TestModels.TestUser();
        existingUser.username = "Calvin";
        existingUser.password = "Seamons";
        existingUser.email = "calvinseamons35@gmail.com";

        newUser = new TestModels.TestUser();
        newUser.username = "testUsername";
        newUser.password = "testPassword";
        newUser.email = "testEmail";

        createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "testGame";

        theServer = new TestServerFacade("localhost", TestFactory.getServerPort());
    }


    @BeforeEach
    public void setup() {
        theServer.clear();

        TestModels.TestRegisterRequest RR = new TestModels.TestRegisterRequest();
        RR.username = existingUser.username;
        RR.password = existingUser.password;
        RR.email = existingUser.email;

        //one user already logged in
        TestModels.TestLoginRegisterResult regResult = theServer.register(RR);
        existingAuth = regResult.authToken;
    }


    @Test
    @Order(1)
    @DisplayName("Testing the Login (Positive)")
    public void testLoginPositive() {
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = existingUser.username;
        loginRequest.password = existingUser.password;

        TestModels.TestLoginRegisterResult loginResult = theServer.login(loginRequest);

        Assertions.assertEquals(HTTP_OK, theServer.getStatusCode(), "Status code should be 200");
        Assertions.assertNotNull(loginResult.authToken, "Auth token should not be null");
    }


    @Test
    @Order(2)
    @DisplayName("Testing the Login (Negative)")
    public void testLoginNegative(){
        TestModels.TestLoginRequest loginRequest = new TestModels.TestLoginRequest();
        loginRequest.username = newUser.username;
        loginRequest.password = newUser.password;

        TestModels.TestLoginRegisterResult loginResult = theServer.login(loginRequest);

        Assertions.assertNotEquals(HTTP_OK, theServer.getStatusCode(), "Status code should not be 200");
        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Your status code should be a 401");

        TestModels.TestLoginRequest diffLoginRequest = new TestModels.TestLoginRequest();
        diffLoginRequest.username = existingUser.username;
        diffLoginRequest.password = "hello govna";

        Assertions.assertNotEquals(HTTP_OK, theServer.getStatusCode(), "Status code should not be 200");
        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Your status code should be a 401, same username different password");
    }


    @Test
    @Order(3)
    @DisplayName("Testing Registration (Positive)")
    public void testRegistrationPositive(){
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = newUser.username;
        registerRequest.password = newUser.password;
        registerRequest.email = newUser.email;

        TestModels.TestLoginRegisterResult registerResult = theServer.register(registerRequest);

        Assertions.assertEquals(HTTP_OK, theServer.getStatusCode(), "Status code should be 200");
        Assertions.assertNotNull(registerResult.authToken, "Auth token should not be null");
    }


    @Test
    @Order(4)
    @DisplayName("Testing Registration (Negative)")
    public void testRegistrationNegative(){
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = existingUser.username;
        registerRequest.password = existingUser.password;
        registerRequest.email = existingUser.email;

        //submit register request
        TestModels.TestLoginRegisterResult registerResult = theServer.register(registerRequest);

        Assertions.assertEquals(HTTP_FORBIDDEN, theServer.getStatusCode(), "Server code should be 403");
        Assertions.assertNull(registerResult.authToken, "Auth token should be null");
        Assertions.assertTrue(registerResult.message.toLowerCase(Locale.ROOT).contains("error"), "You did not return an error message containing the word 'error'");
    }

    @Test
    @Order(5)
    @DisplayName("Testing Logout (Positive)")
    public void testLogoutPositive(){
        TestModels.TestResult logoutResult = theServer.logout(existingAuth);

        Assertions.assertEquals(HTTP_OK, theServer.getStatusCode(), "Status code should be 200");
        Assertions.assertNull(logoutResult.message, "Message should be null");
    }

    @Test
    @Order(6)
    @DisplayName("Testing Logout (Negative)")
    public void testLogoutNegative(){
        TestModels.TestResult logoutResult = theServer.logout(null);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Status code should be 401, there was no auth token provided");
        Assertions.assertNotNull(logoutResult.message, "Message should not be null");

        TestModels.TestResult logoutResult2 = theServer.logout("badAuthToken");

        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Status code should be 401, Bad auth token provided");
        Assertions.assertEquals("Error: Unauthorized", logoutResult2.message, "Message should be 'Error: Unauthorized'");
    }

    @Test
    @Order(7)
    @DisplayName("Testing Create Game (Positive)")
    public void createGamePositive(){
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "testGame";

        TestModels.TestCreateResult createResult = theServer.createGame(createRequest, existingAuth);

        Assertions.assertEquals(HTTP_OK, theServer.getStatusCode(), "Status code should be 200");
        Assertions.assertNotNull(createResult.gameID, "Game ID should not be null");
        Assertions.assertNull(createResult.message, "Message should be null");
    }

    @Test
    @Order(8)
    @DisplayName("Testing Create Game (Negative)")
    public void createGameNegative(){
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "testGame";

        TestModels.TestCreateResult createResult = theServer.createGame(createRequest, null);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Status code should be 401, no auth token provided");
        Assertions.assertNull(createResult.gameID, "Game ID should be null");
        Assertions.assertNotNull(createResult.message, "Message should not be null");

        TestModels.TestCreateResult createResult2 = theServer.createGame(createRequest, "badAuthToken");

        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Status code should be 401, bad auth token provided");
        Assertions.assertNull(createResult2.gameID, "Game ID should be null");
        Assertions.assertNotNull(createResult2.message, "Message should not be null");
    }

    @Test
    @Order(9)
    @DisplayName("Testing List Games (Positive)")
    public void listGamesPositive(){
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "testGame";

        TestModels.TestCreateRequest createRequest2 = new TestModels.TestCreateRequest();
        createRequest2.gameName = "testGame2";

        TestModels.TestCreateResult createResult = theServer.createGame(createRequest, existingAuth);
        TestModels.TestCreateResult createResult2 = theServer.createGame(createRequest2, existingAuth);

        Assertions.assertEquals(HTTP_OK, theServer.getStatusCode(), "Status code should be 200");
        Assertions.assertNotNull(createResult2.gameID, "Game ID should not be null");
        Assertions.assertNull(createResult2.message, "Message should be null");

        TestModels.TestListResult listResult = theServer.listGames(existingAuth);

        Assertions.assertEquals(HTTP_OK, theServer.getStatusCode(), "Status code should be 200");
        Assertions.assertNotNull(listResult.games, "Games should not be null");
        Assertions.assertNull(listResult.message, "Message should be null");
    }

    @Test
    @Order(10)
    @DisplayName("Testing List Games (Negative)")
    public void listGamesNegative(){
        TestModels.TestListResult listResult = theServer.listGames(null);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Status code should be 401, no auth token provided");
        Assertions.assertNull(listResult.games, "Games should be null");
        Assertions.assertNotNull(listResult.message, "Message should not be null");

        TestModels.TestListResult listResult2 = theServer.listGames("badAuthToken");

        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Status code should be 401, bad auth token provided");
        Assertions.assertNull(listResult2.games, "Games should be null");
        Assertions.assertNotNull(listResult2.message, "Message should not be null");
    }

    @Test
    @Order(11)
    @DisplayName("Testing Join Game (Positive)")
    public void joinGamePositive(){
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "testGame";

        TestModels.TestCreateResult createResult = theServer.createGame(createRequest, existingAuth);

        Assertions.assertEquals(HTTP_OK, theServer.getStatusCode(), "Status code should be 200");
        Assertions.assertNotNull(createResult.gameID, "Game ID should not be null");
        Assertions.assertNull(createResult.message, "Message should be null");

        // Now actually testing the join game

        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;

        TestModels.TestResult joinResult = theServer.verifyJoinPlayer(joinRequest, existingAuth);

        TestModels.TestListResult listResult = theServer.listGames(existingAuth);

        Assertions.assertEquals(HTTP_OK, theServer.getStatusCode(), "Status code should be 200");
        Assertions.assertNull(joinResult.message, "Message should be null");
        Assertions.assertEquals(ChessGame.TeamColor.WHITE, joinRequest.playerColor, "Player color should be white");

        //Create expected list of games
        Collection<TestModels.TestListResult.TestListEntry> expectedList = new HashSet<>();
        TestModels.TestListResult.TestListEntry game = new TestModels.TestListResult.TestListEntry();

        game = new TestModels.TestListResult.TestListEntry();
        game.gameID = createResult.gameID;
        game.gameName = "testGame";
        game.blackUsername = null;
        game.whiteUsername = existingUser.username;
        expectedList.add(game);

        Collection<TestModels.TestListResult.TestListEntry> returnedList = new HashSet<>(Arrays.asList(listResult.games));

        Assertions.assertEquals(expectedList, returnedList, "List of games should be the same");
    }

    @Test
    @Order(12)
    @DisplayName("Testing Join Game (Negative)")
    public void joinGameNegative(){
        TestModels.TestCreateRequest createRequest = new TestModels.TestCreateRequest();
        createRequest.gameName = "testGame";

        //Logout the Existing User
        TestModels.TestResult logoutResult = theServer.logout(existingAuth);

        //Now Try and create a game with the existing user
        TestModels.TestCreateResult createResult = theServer.createGame(createRequest, existingAuth);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Status code should be 401, the user logged out");
        Assertions.assertNull(createResult.gameID, "Game ID should be null");
        Assertions.assertNotNull(createResult.message, "Message should not be null");

        //Now try and join the game with the existing user
        TestModels.TestJoinRequest joinRequest = new TestModels.TestJoinRequest();
        joinRequest.gameID = createResult.gameID;
        joinRequest.playerColor = ChessGame.TeamColor.WHITE;

        TestModels.TestResult joinResult = theServer.verifyJoinPlayer(joinRequest, existingAuth);

        Assertions.assertEquals(HTTP_UNAUTHORIZED, theServer.getStatusCode(), "Status code should be 401, the user logged out");
        Assertions.assertNotNull(joinResult.message, "Message should not be null");
    }

    @Test
    @Order(13)
    @DisplayName("Testing Clear Database")
    public void clearDatabase(){
        TestModels.TestResult clearResult = theServer.clear();

        Assertions.assertEquals(HTTP_OK, theServer.getStatusCode(), "Status code should be 200");
        Assertions.assertNull(clearResult.message, "Message should be null");
    }



}
