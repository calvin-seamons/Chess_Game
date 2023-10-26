package Results;

import Models.Game;

/**
 * ListGamesResult contains the list of games that the list games service returns
 */
public class ListGamesResult {
    private Game[] games;
    private String message;

    public ListGamesResult() {}

    public Game[] getGames() {
        return games;
    }

    public void setGames(Game[] games) {
        this.games = games;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
    	this.message = message;
    }

}
