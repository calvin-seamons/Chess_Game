package passoffTests.serverTests;

import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestModels;

import java.util.Scanner;


public class MyPersistenceTest {

    private static TestModels.TestUser existingUser;
    private static TestModels.TestUser newUser;
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

        theServer = new TestServerFacade("localhost", passoffTests.TestFactory.getServerPort());
    }


    @BeforeEach
    public void setup() {
        theServer.clear();

        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = existingUser.username;
        registerRequest.password = existingUser.password;
        registerRequest.email = existingUser.email;

        //one user already logged in
        TestModels.TestLoginRegisterResult regResult = theServer.register(registerRequest);
        existingAuth = regResult.authToken;
    }

    @Test
    @Order(1)
    @DisplayName("CreateUser Positive")
    public void createUserPositive() {
        TestModels.TestRegisterRequest registerRequest = new TestModels.TestRegisterRequest();
        registerRequest.username = newUser.username;
        registerRequest.password = newUser.password;
        registerRequest.email = newUser.email;

        TestModels.TestLoginRegisterResult regResult = theServer.register(registerRequest);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Shut down the server, wait a few seconds, then restart the server. Then press ENTER.");
        scanner.nextLine();

        Assertions.assertNotNull(regResult.authToken);
        Assertions.assertEquals(newUser.username, regResult.username);
        Assertions.assertEquals(true, regResult.success);
        Assertions.assertEquals(null, regResult.message);
    }

}
