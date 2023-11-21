package Server;

import Handlers.*;
import Models.Authtoken;
import Models.Game;
import Requests.*;
import Results.CreateGameResult;
import Services.ClearApplicationService;
import com.google.gson.Gson;
import dataAccess.*;
import spark.Spark;
import Services.*;

import static spark.Spark.halt;

/**
 * ServerClass is the class that handles the HTTP requests and calls the correct Handler method
 */
public class ServerClass {
    private static final GameDAO gameDatabase = new GameDAO();
    private static final AuthDAO authDatabase = new AuthDAO();
    private static final UserDAO userDatabase = new UserDAO();

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
            createRoutes(db);
            Spark.awaitInitialization();
            System.out.println("Listening on port " + port);
//            userDatabase.updateUser(new User("calvin", "sv164889", "calvinseamons35@gmail.com"), new User("calvin", "hellothere", "iloveyou.com"), db);
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the routes for the server
     */
    private static void createRoutes(Database db) {
        Spark.before((req, res) -> {
            boolean authenticated = true;

            // ... check if authenticated

            if (!authenticated) {
                halt(401, "You are not welcome here");
            }

        });

// UPDATED
        Spark.post("/user", (req, res) -> {
            Gson gson = new Gson();
            RegisterRequest RR;
            RR = new RegisterHandler().HTTPToRegisterRequest(req.body());
            String errorMessage = new RegisterService().register(RR, userDatabase, db);
            res.type("application/json");

            String result;
            result = new RegisterHandler().registerRequestToHTTP(RR, errorMessage);

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
                userDatabase.createUser(RR.getUsername(), RR.getPassword(), RR.getEmail(), db);
                authDatabase.createAuth(auth, db);
                res.status(200);
                return result;
            }
        });

// UPDATED
        Spark.post("/session", (req, res) -> {
            LoginRequest LR = new LoginHandler().HTTPToLoginRequest(req.body());
            String errorMessage = new LoginService().login(LR, userDatabase, authDatabase, db);
            String result = new LoginHandler().loginRequestToHTTP(LR, authDatabase, errorMessage, db);

            res.type("application/json");

            if(result.contains("Error: Unauthorized")){
                res.status(401);
            }
            else{
                res.status(200);
            }
            return result;
        });

    // UPDATED
        Spark.delete("/session", (req, res) -> {
            String authToken = req.headers("Authorization");
            AuthTokenRequest logoutRequest = new LogoutHandler().HTTPToLogoutRequest(authToken, authDatabase, db);
            String errorMessage = new LogoutService().logout(logoutRequest, authDatabase, db);
            String result = new LogoutHandler().logoutRequestToHTTP(logoutRequest, errorMessage);
            res.type("application/json");

            if(result.contains("Error: Unauthorized")){
                res.status(401);
            }
            else{
                authDatabase.deleteAuthToken(logoutRequest.getAuthToken(),db );
                res.status(200);
            }
            return result;
        });

    // UPDATED
        Spark.get("/game", (req, res) -> {
            AuthTokenRequest listGamesRequest = new AuthTokenRequest();
            String authToken = req.headers("Authorization");
            listGamesRequest.setAuthToken(authToken);
            String errorMessage = new ListGamesService().listGames(listGamesRequest, authDatabase, gameDatabase, db);
            String result = new ListGamesHandler().authTokenTolistGamesHTTP(listGamesRequest, errorMessage, authDatabase, gameDatabase, db);
            res.type("application/json");

            if(result.contains("Error: Unauthorized")){
                res.status(401);
            }
            else{
                res.status(200);
            }
            return result;
        });

// UPDATED
        Spark.post("/game", (req, res) -> {
            CreateGameRequest createGameRequest = new CreateGameHandler().HTTPToCreateGameRequest(req.body());
            createGameRequest.setAuthToken(req.headers("Authorization"));
            String errorMessage = new CreateGameService().createGame(createGameRequest, gameDatabase, authDatabase, db);
            String result = new CreateGameHandler().createGameToHTTP(createGameRequest, gameDatabase, errorMessage);
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
                gameDatabase.createGame(newGame, db);
                res.status(200);
                return result;
            }
        });
// UPDATED
        Spark.put("/game", (req, res) -> {
            JoinGameRequest joinGameRequest = new JoinGameHandler().HTTPToJoinGameRequest(req.body());
            joinGameRequest.setAuthToken(req.headers("Authorization"));
            String errorMessage = new JoinGameService().joinGame(joinGameRequest, authDatabase, gameDatabase, db);
            res.type("application/json");

            String result = new JoinGameHandler().joinGameRequestToHTTP(joinGameRequest, errorMessage);


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
// UPDATED
        Spark.delete("/db", (req, res) -> {
            ClearApplicationService clearApplicationService = new ClearApplicationService();
            clearApplicationService.clearApplication(authDatabase, gameDatabase, userDatabase, db);
            String result = new ClearApplicationHandler().clearApplicationRequestToHTTP();
            res.type("application/json");
            res.status(200);
            return result;
        });
    }
}
