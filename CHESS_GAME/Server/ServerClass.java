import Handlers.*;
import Requests.*;
import Results.ListGamesResult;
import spark.Spark;

/**
 * ServerClass is the class that handles the HTTP requests and calls the correct Handler method
 */
public class ServerClass {

    public ServerClass() {}

    /**
     * Main method that starts the server
     * @param args the port number
     * Example: 8080
     */
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Spark.port(port);
            Spark.externalStaticFileLocation("web");
            createRoutes();
            Spark.awaitInitialization();
            System.out.println("Listening on port " + port);
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }

    /**
     * Creates the routes for the server
     */
    private static void createRoutes() {
        Spark.post("/user", (req, res) -> {
            RegisterRequest RR = new RegisterRequest();
            RR = new RegisterHandler().HTTPToRegisterResult(req.body());
            String result;
            result = new RegisterHandler().registerRequestToHTTP(RR);
            res.type("application/json");
            return result;
        });

        Spark.post("/session", (req, res) -> {
            LoginRequest LR = new LoginHandler().HTTPToLoginRequest(req.body());
            String result = new LoginHandler().loginRequestToHTTP(LR);
            res.type("application/json");
            return result;
        });

        Spark.delete("/session", (req, res) -> {
            AuthTokenRequest logoutRequest = new LogoutHandler().HTTPToLogoutRequest(req.body());
            String result = new LogoutHandler().logoutRequestToHTTP(logoutRequest);
            res.type("application/json");
            return result;
        });

        Spark.get("/game", (req, res) -> {
            AuthTokenRequest listGamesRequest = new AuthTokenRequest();
            String authToken = req.headers("Authorization");
            listGamesRequest.setAuthToken(authToken);
            String result = new ListGamesHandler().authTokenTolistGamesHTTP(listGamesRequest);
            res.type("application/json");
            return result;
        });

        Spark.post("/game", (req, res) -> {
            CreateGameRequest createGameRequest = new CreateGameHandler().HTTPToCreateGameRequest(req.body());
            createGameRequest.setAuthToken(req.headers("Authorization"));
            String result = new CreateGameHandler().createGameToHTTP(createGameRequest);
            res.type("application/json");
            return result;
        });

        Spark.put("/game", (req, res) -> {
            JoinGameRequest joinGameRequest = new JoinGameHandler().HTTPToJoinGameRequest(req.body());
            joinGameRequest.setAuthToken(req.headers("Authorization"));
            String result = new JoinGameHandler().joinGameRequestToHTTP(joinGameRequest);
            res.type("application/json");
            return result;
        });

        Spark.delete("/db", (req, res) -> {
            String result = new ClearApplicationHandler().clearApplicationRequestToHTTP();
            res.type("application/json");
            return result;
        });
    }
}
