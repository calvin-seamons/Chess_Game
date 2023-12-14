package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    public JoinObserverCommand(int gameID, String authToken) {
        super(CommandType.JOIN_OBSERVER, authToken, gameID);
    }
}
