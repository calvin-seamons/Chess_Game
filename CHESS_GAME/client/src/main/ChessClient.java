import Models.Game;
import Results.*;
import chess.*;
import ui.EscapeSequences;
import ui.HTTPClient;
import java.util.Objects;
import java.util.Scanner;


public class ChessClient {

    private enum AppState {
        PRE_LOGIN, POST_LOGIN
    }

    private String currentUserAuthToken;


    private final HTTPClient client = new HTTPClient();

    private AppState currentState = AppState.PRE_LOGIN;

    public static void main(String[] args) {
        ChessClient client = new ChessClient();
        Scanner scanner = new Scanner(System.in);


        System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.BLACK_KING + " Welcome to the Chess Application " + EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_MAGENTA);

        while (true) {
            client.displayMenu();
            String input = scanner.nextLine();
            client.processInput(input);
        }
    }

    private void displayMenu() {
        if (currentState == AppState.PRE_LOGIN) {
            // Display pre-login menu
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            System.out.println("Register - " + EscapeSequences.SET_TEXT_COLOR_YELLOW + "To create an account" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Login - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To play chess" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Quit - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To quit playing chess" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Help - " + EscapeSequences.SET_TEXT_COLOR_YELLOW + "To display this menu" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);

        } else {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + "Create - " + EscapeSequences.SET_TEXT_COLOR_YELLOW +  "To create a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("List - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To list all games" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Join - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To join a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Observe - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To observe a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Logout - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To logout" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Quit - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To quit playing chess" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Help - " + EscapeSequences.SET_TEXT_COLOR_YELLOW + "To display this menu" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        }
    }

    private void processInput(String input) {
        switch (input.toLowerCase()) {
            case "login":
                System.out.print("Enter username: ");
                String loginUsername = new Scanner(System.in).nextLine();
                System.out.print("Enter password: ");
                String loginPassword = new Scanner(System.in).nextLine();
                handleLogin(loginUsername, loginPassword);
                break;
            case "register":
                System.out.print("Enter username: ");
                String username = new Scanner(System.in).nextLine();
                System.out.print("Enter password: ");
                String password = new Scanner(System.in).nextLine();
                System.out.print("Enter email: ");
                String email = new Scanner(System.in).nextLine();
                handleRegister(username, password, email);
                break;
            case "create":
                System.out.print("Enter game name: ");
                String gameName = new Scanner(System.in).nextLine();
                handleCreate(gameName);
                break;
            case "list":
                handleList();
                break;
            case "join":
                System.out.print("Enter game ID: ");
                int gameID = Integer.parseInt(new Scanner(System.in).nextLine());
                System.out.print("[WHITE|BLACK] ?");
                String team = new Scanner(System.in).nextLine();
                handleJoin(gameID, team);
                break;
            case "logout":
                handleLogout();
                break;
            case "observe":
                System.out.print("Enter game ID: ");
                String observeGameID = new Scanner(System.in).nextLine();
                handleObserve(observeGameID);
                break;
            case "help":
                displayMenu();
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command\n");
                break;
        }
    }

    private void handleLogout() {
        // Implementation for logout
        System.out.print("Logging out: ");
        LogoutResult result = client.logout(currentUserAuthToken);

        if(result == null){
            System.out.println("Error: Could not logout");
            return;
        }

        if(result.getMessage() == null){
            System.out.println("Success\n");
            currentState = AppState.PRE_LOGIN;
            currentUserAuthToken = null;
        } else {
            System.out.println(result.getMessage() + "\n");
        }
    }

    private void handleObserve(String observeGameID) {
        System.out.print("Observing game " + observeGameID + ": ");
        JoinGameResult result = client.joinGame(Integer.parseInt(observeGameID), "SPECTATOR", currentUserAuthToken);

        if(result == null) {
            System.out.println("Error: Could not observe game");
            return;
        }

        if(result.getMessage() == null){
            System.out.println("Success\n");
            ChessBoard board = new Chess_Board();
            board.resetBoard();
            showBoard("white", board);
            showBoard("black", board);
        } else {
            System.out.println(result.getMessage()+ "\n");
        }
    }

    private void handleJoin(int gameID, String team) {
        System.out.println("Joining game " + gameID + " as " + team + ": ");
        if(!team.equalsIgnoreCase("white") && !team.equalsIgnoreCase("black")){
            System.out.println("Error: Invalid team color");
            return;
        }

        JoinGameResult result = client.joinGame(gameID, team, currentUserAuthToken);

        if(result == null) {
            System.out.println("Error: Could not join game");
            return;
        }

        if(result.getMessage() == null){
            System.out.println("Success\n");
            ChessBoard board = new Chess_Board();
            board.resetBoard();
            showBoard("white", board);
            showBoard("black", board);
        } else {
            System.out.println(result.getMessage()+ "\n");
        }
    }

    private void handleList() {
        // Implementation for list
        System.out.print("Listing games: ");
        ListGamesResult result = client.listGames(currentUserAuthToken);

        if(result == null) {
            System.out.println("Error: Could not list games");
            return;
        }

        if(result.getMessage() == null){
            System.out.println("Success\n");
            for (Game game : result.getGames()) {
                System.out.println("Game ID: " + game.getGameID() + " Game Name: " + game.getGameName());
            }
            System.out.println();
        } else {
            System.out.println(result.getMessage()+ "\n");
        }
    }

    private void handleCreate(String gameName) {
        // Implementation for create
        System.out.print("Creating game " + gameName + ": ");
        CreateGameResult result = client.createGame(gameName, currentUserAuthToken);

        if(result == null) {
            System.out.println("Error: Could not create game");
            return;
        }

        if(Objects.equals(result.getMessage(), "")){
            System.out.println("Success, your game ID is: " + result.getGameID() + "\n");
        } else {
            System.out.println(result.getMessage()+ "\n");
        }

    }

    private void handleRegister(String username, String password, String email) {
        // Implementation for register
        System.out.print("Registering user " + username + " with password " + password + " and email " + email + ": ");

        // Check if username is already taken
        // If not, create user
        // On successful registration, change state to POST_LOGIN
        RegisterResult result = client.register(username, password, email);
        if (result == null) {
            System.out.println("Error: Could not register user");
            return;
        }

        if(result.getMessage() == null){
            currentState = AppState.POST_LOGIN;
            System.out.println("Success\n");
            currentUserAuthToken = result.getAuthToken();
        } else {
            System.out.println(result.getMessage()+ "\n");
        }
    }

    private void handleLogin(String username, String password) {
        // Implementation for login
        // On successful login, change state to POST_LOGIN
        System.out.print("Logging in user: ");
        LoginResult result = client.login(username, password);

        if (result == null) {
            System.out.println("Error: Could not login user");
        } else if(result.getMessage() == null){
            System.out.println("Success\n");
            currentState = AppState.POST_LOGIN;
            currentUserAuthToken = result.getAuthToken();
        } else {
            System.out.println(result.getMessage()+ "\n");
        }
    }

    private void showBoard(String color, ChessBoard board) {
        // Define the colors for the pieces
        String whitePieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        String blackPieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
        String whiteTileColor = EscapeSequences.SET_BG_COLOR_WHITE;
        String blackTileColor = EscapeSequences.SET_BG_COLOR_BLACK;

        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);

        // Print the column labels
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        if (color.equalsIgnoreCase("white")) {
            System.out.println("   a  b  c  d  e  f  g  h");
        } else {
            System.out.println("   h  g  f  e  d  c  b  a");
        }

        for (int i = 0; i < 8; i++) {
            // Print the row label
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.print(color.equalsIgnoreCase("white") ? (8 - i) + " " : (i + 1) + " ");

            for (int j = 0; j < 8; j++) {
                // Choose the correct tile based on the perspective
                int row = color.equalsIgnoreCase("white") ? 8 - i : i + 1;
                int col = color.equalsIgnoreCase("white") ? j + 1 : 8 - j;

                // Choose background color based on tile position
                String tileColor = (row + col) % 2 == 0 ? whiteTileColor : blackTileColor;
                System.out.print(tileColor);

                Chess_Piece piece = (Chess_Piece) board.getPiece(new Chess_Position(row, col));
                if (piece == null) {
                    System.out.print(EscapeSequences.EMPTY); // Use the constant for an empty tile
                } else {
                    String pieceColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? whitePieceColor : blackPieceColor;
                    System.out.print(pieceColor +  piece.getSymbol(piece.getPieceType(), piece.getTeamColor()) +  EscapeSequences.RESET_BG_COLOR);
                }
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY); // Reset background color after each tile
            }

            // Print the row label again
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.print(" " + (color.equalsIgnoreCase("white") ? (8 - i) : (i + 1)) + " ");
            System.out.println(); // Newline at the end of each board row
        }

        // Print the column labels again
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        if (color.equalsIgnoreCase("white")) {
            System.out.println("   a  b  c  d  e  f  g  h\n");
        } else {
            System.out.println("   h  g  f  e  d  c  b  a\n");
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR);
    }

}

