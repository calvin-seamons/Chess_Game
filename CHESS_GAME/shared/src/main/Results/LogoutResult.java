package Results;

/**
 * LogoutResult contains the message that the logout service returns
 */
public class LogoutResult {
    private String message;

    public LogoutResult() {}
    public LogoutResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
