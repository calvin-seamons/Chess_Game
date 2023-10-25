import Handlers.LoginHandler;
import Handlers.LogoutHandler;
import Handlers.RegisterHandler;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Requests.LogoutRequest;
import Results.RegisterResult;
import com.google.gson.Gson;
import spark.Spark;
import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * ServerClass is the class that handles the HTTP requests and calls the correct Handler method
 */
public class ServerClass {

    public ServerClass() {}

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
            LogoutRequest logoutRequest = new LogoutHandler().HTTPToLogoutRequest(req.body());
            String result = new LogoutHandler().logoutRequestToHTTP(logoutRequest);
            res.type("application/json");
            return result;
        });
    }

    /**
     * This method calls the correct Handler method based on the HTTP request type
     * @param requestBody the HTTP request body
     * @return the HTTP response body
     */
    public String handle(String requestBody) {
        Spark.externalStaticFileLocation("path/to/web/folder");

        return null;
    }
}
