package Server;

import spark.Spark;

/**
 * ServerClass is the class that handles the HTTP requests and calls the correct Handler method
 */
public class ServerClass {
    public ServerClass() {}

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
