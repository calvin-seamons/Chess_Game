package Results;

import Models.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * ListGamesResult contains the list of games that the list games service returns
 */
public class ListGamesResult {
    private List<Game> games = new ArrayList<>();
    private String message;

    public ListGamesResult() {}

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
    	this.message = message;
    }

}
