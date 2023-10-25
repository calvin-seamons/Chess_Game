package Results;

import Models.Game;

/**
 * ListGamesResult contains the list of games that the list games service returns
 */
public class ListGamesResult {
    private Game[] games;

    public ListGamesResult() {}

    public Game[] getGames() {
        return games;
    }

    public void setGames(Game[] games) {
        this.games = games;
    }

}
