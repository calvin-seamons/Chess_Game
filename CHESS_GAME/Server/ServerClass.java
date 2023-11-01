import Handlers.*;
import Models.Authtoken;
import Models.Game;
import Requests.*;
import Results.CreateGameResult;
import com.google.gson.Gson;
import dataAccess.*;
import spark.Spark;

import static spark.Spark.halt;

/**
 * ServerClass is the class that handles the HTTP requests and calls the correct Handler method
 */
public class ServerClass {
    private static GameDAO gameDatabase = new GameDAO();
    private static AuthDAO authDatabase = new AuthDAO();
    private static UserDAO userDatabase = new UserDAO();

    public ServerClass() {}

    /**
     * Main method that starts the server
     * @param args the port number
     * Example: 8080
     */
    public static void main(String[] args) {
        try {
            Database db = new Database();
            db.getConnection();
            int port = Integer.parseInt(args[0]);
            Spark.port(port);
            Spark.externalStaticFileLocation("web");
            createRoutes();
            Spark.awaitInitialization();
            System.out.println("Listening on port " + port);
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the routes for the server
     */
    private static void createRoutes() {
        Spark.before((req, res) -> {
            boolean authenticated = true;

            // ... check if authenticated

            if (!authenticated) {
                halt(401, "You are not welcome here");
            }

        });

// THIS WORKS
        Spark.post("/user", (req, res) -> {
            Gson gson = new Gson();
            RegisterRequest RR = new RegisterRequest();
            RR = new RegisterHandler().HTTPToRegisterRequest(req.body());
            res.type("application/json");

            String result;
            result = new RegisterHandler().registerRequestToHTTP(RR, userDatabase);

            if(result.contains("Error: Already Taken")){
                res.status(403);
                return result;
            }
            else if(result.contains("Error: Bad Request")){
                res.status(400);
                return result;
            }
            else{
                Authtoken auth = new Authtoken();
                auth = gson.fromJson(result, auth.getClass());
                userDatabase.createUser(RR.getUsername(), RR.getPassword(), RR.getEmail());
                authDatabase.createAuth(auth);
                res.status(200);
                return result;
            }
        });

// THIS WORKS
        Spark.post("/session", (req, res) -> {
            LoginRequest LR = new LoginHandler().HTTPToLoginRequest(req.body());
            String result = new LoginHandler().loginRequestToHTTP(LR, userDatabase, authDatabase);

            res.type("application/json");

            if(result.contains("Error: Unauthorized")){
                res.status(401);
                return result;
            }
            else{
                res.status(200);
                return result;
            }
        });

    // THIS WORKS
        Spark.delete("/session", (req, res) -> {
            String authToken = req.headers("Authorization");
            AuthTokenRequest logoutRequest = new LogoutHandler().HTTPToLogoutRequest(authToken, authDatabase);
            String result = new LogoutHandler().logoutRequestToHTTP(logoutRequest, authDatabase);
            res.type("application/json");

            if(result.contains("Error: Unauthorized")){
                res.status(401);
                return result;
            }
            else{
                userDatabase.deleteUser(logoutRequest.getUsername());
                authDatabase.deleteAuthToken(logoutRequest.getAuthToken());

                // Print out the rest of the auth tokens
                System.out.println("Remaining auth tokens:");
                for (Authtoken token : authDatabase.getDatabaseAuthtokens()) {
                    System.out.println(token.getAuthToken());
                }

                res.status(200);
                return result;
            }
        });

    // Pretty Sure this Works
        Spark.get("/game", (req, res) -> {
            AuthTokenRequest listGamesRequest = new AuthTokenRequest();
            String authToken = req.headers("Authorization");
            listGamesRequest.setAuthToken(authToken);
            String result = new ListGamesHandler().authTokenTolistGamesHTTP(listGamesRequest, authDatabase, gameDatabase);
            res.type("application/json");

            if(result.contains("Error: Unauthorized")){
                res.status(401);
                return result;
            }
            else{
                res.status(200);
                return result;
            }
        });

        Spark.post("/game", (req, res) -> {
            CreateGameRequest createGameRequest = new CreateGameHandler().HTTPToCreateGameRequest(req.body());
            createGameRequest.setAuthToken(req.headers("Authorization"));
            String result = new CreateGameHandler().createGameToHTTP(createGameRequest, gameDatabase, authDatabase);
            res.type("application/json");

            if(result.contains("Error: Unauthorized")){
                res.status(401);
                return result;
            }
            else if(result.contains("Error: Bad Request")){
                res.status(400);
                return result;
            }
            else{
                CreateGameResult createGameResult = new Gson().fromJson(result, CreateGameResult.class);
                Game newGame = new Game();
                newGame.setGameID(createGameResult.getGameID());
                newGame.setGameName(createGameRequest.getGameName());
                gameDatabase.createGame(newGame);
                res.status(200);
                return result;
            }
        });

        Spark.put("/game", (req, res) -> {
            JoinGameRequest joinGameRequest = new JoinGameHandler().HTTPToJoinGameRequest(req.body());
            joinGameRequest.setAuthToken(req.headers("Authorization"));
            String result = new JoinGameHandler().joinGameRequestToHTTP(joinGameRequest, authDatabase, gameDatabase);
            res.type("application/json");

            if(result.contains("Error: Unauthorized")){
                res.status(401);
                return result;
            }
            else if(result.contains("Error: Bad Request")){
                res.status(400);
                return result;
            }
            else if(result.contains("Error: Already Taken")){
                res.status(403);
                return result;
            }
            else{
                res.status(200);
                return result;
            }
        });

        Spark.delete("/db", (req, res) -> {
            String result = new ClearApplicationHandler().clearApplicationRequestToHTTP(authDatabase, gameDatabase, userDatabase);

            // Print out the rest of the auth tokens
            System.out.println("Remaining auth tokens:");
            for (Authtoken token : authDatabase.getDatabaseAuthtokens()) {
                System.out.println(token.getAuthToken());
            }

            res.type("application/json");
            res.status(200);
            return result;
        });
    }
}
