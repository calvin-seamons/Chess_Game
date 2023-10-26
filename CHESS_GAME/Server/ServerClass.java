import Handlers.*;
import Requests.*;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import spark.Spark;

import static spark.Spark.halt;

/**
 * ServerClass is the class that handles the HTTP requests and calls the correct Handler method
 */
public class ServerClass {
    private GameDAO gameDatabase = new GameDAO();
    private AuthDAO authDatabase = new AuthDAO();
    private UserDAO userDatabase = new UserDAO();

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
    private void createRoutes() {
//        Spark.before((req, res) -> {
//            boolean authenticated = false;
//
//            // ... check if authenticated
//
//            if (!authenticated) {
//                halt(401, "You are not welcome here");
//            }
//
//        });

        Spark.post("/user", (req, res) -> {
            RegisterRequest RR = new RegisterRequest();
            RR = new RegisterHandler().HTTPToRegisterRequest(req.body());
            res.type("application/json");

            if(RR.getUsername() == null || RR.getPassword() == null || RR.getEmail() == null){
                res.status(400);
                return "Error: Bad Request";
            }

            String result;
            result = new RegisterHandler().registerRequestToHTTP(RR);

            if(result.contains("Error: Already Taken")){
                res.status(403);
                return result;
            }
            else{
                res.status(200);
                return result;
            }
        });

        Spark.post("/session", (req, res) -> {
            LoginRequest LR = new LoginHandler().HTTPToLoginRequest(req.body());
            String result = new LoginHandler().loginRequestToHTTP(LR);
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

        Spark.delete("/session", (req, res) -> {
            AuthTokenRequest logoutRequest = new LogoutHandler().HTTPToLogoutRequest(req.body());
            String result = new LogoutHandler().logoutRequestToHTTP(logoutRequest);
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

        Spark.get("/game", (req, res) -> {
            AuthTokenRequest listGamesRequest = new AuthTokenRequest();
            String authToken = req.headers("Authorization");
            listGamesRequest.setAuthToken(authToken);
            String result = new ListGamesHandler().authTokenTolistGamesHTTP(listGamesRequest);
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
            String result = new CreateGameHandler().createGameToHTTP(createGameRequest, this.gameDatabase);
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
                res.status(200);
                return result;
            }
        });

        Spark.put("/game", (req, res) -> {
            JoinGameRequest joinGameRequest = new JoinGameHandler().HTTPToJoinGameRequest(req.body());
            joinGameRequest.setAuthToken(req.headers("Authorization"));
            String result = new JoinGameHandler().joinGameRequestToHTTP(joinGameRequest);
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
            String result = new ClearApplicationHandler().clearApplicationRequestToHTTP();
            res.type("application/json");
            res.status(200);
            return result;
        });
    }
}
