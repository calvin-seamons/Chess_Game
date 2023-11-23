package Results;

/**
 * JoinGameResult contains the message that the join game service returns
 */
public class JoinGameResult {
    private String message;

    public JoinGameResult() {}
    public JoinGameResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
