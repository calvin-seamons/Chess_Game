package ui;

public class ChessClient {
    // TODO: Deal with all the command line arguments
    // This will call "ServerFacade which calls your own database
    public ChessClient() {
        System.out.println("Introduction to Chess");
        System.out.println(EscapeSequences.WHITE_QUEEN + " " + EscapeSequences.BLACK_QUEEN);
    }

    public void help() {
        System.out.println("Commands:");
        System.out.println("help - Displays this help message");
        System.out.println("login <username> <password> - Logs the user in");
        System.out.println("register <username> <password> - Registers the user");
        System.out.println("create <gameName> - Creates a new game");
        System.out.println("join <gameName> - Joins a game");
        System.out.println("move <from> <to> - Moves a piece");
        System.out.println("resign - Resigns from the game");
        System.out.println("quit - Quits the game");
    }
}
