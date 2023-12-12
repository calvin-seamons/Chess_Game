package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{
    public String message;

    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        this.message = message;
    }
}
