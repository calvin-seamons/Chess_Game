import Models.Game;
import Results.*;
import chess.*;
import ui.EscapeSequences;
import ui.HTTPClient;
import ui.NotificationHandler;
import ui.WebsocketClient;
import webSocketMessages.userCommands.JoinPlayerCommand;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


public class ChessClient implements NotificationHandler {

    private enum AppState {
        PRE_LOGIN, POST_LOGIN, IN_GAME
    }

    private String currentUserAuthToken;
    private String currentUsername;
    // Create a game array that stores all the games
    private ArrayList<Game> games = new ArrayList<>();

    public WebsocketClient websocketClient;
    public HTTPClient client;

    private AppState currentState = AppState.PRE_LOGIN;
    private Chess_Game game;
    static String URL = "http://localhost:8080";
    NotificationHandler notificationHandler;

    public ChessClient() throws Exception{
        client = new HTTPClient(URL);
        websocketClient = new WebsocketClient("ws://localhost:8080/connect", this);
    }


    public static void main(String[] args) throws Exception {
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

        } else if (currentState == AppState.POST_LOGIN) {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + "Create - " + EscapeSequences.SET_TEXT_COLOR_YELLOW +  "To create a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("List - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To list all games" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Join - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To join a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Observe - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To observe a game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Logout - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To logout" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Quit - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To quit playing chess" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Help - " + EscapeSequences.SET_TEXT_COLOR_YELLOW + "To display this menu" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        }
        else{
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + "Redraw Chess Board - " + EscapeSequences.SET_TEXT_COLOR_YELLOW + "To redraw the chess board" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Make Move - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To make a move" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Highlight Legal Moves - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To highlight legal moves" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Resign - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To resign" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Leave - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To leave the game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
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
                try {
                    System.out.print("Enter Game Number: ");
                    int gameNumber = Integer.parseInt(new Scanner(System.in).nextLine());
                    System.out.print("White or Black? ");
                    String team = new Scanner(System.in).nextLine();
                    handleJoin(gameNumber, team);
                }
                catch(Exception e){
                    System.out.println("Error: Invalid game number\n*Don't forget to list the games first*\n");
                }
                break;
            case "logout":
                handleLogout();
                break;
            case "observe":
                System.out.print("Enter Game Number: ");
                String observeGameNumber = new Scanner(System.in).nextLine();
                handleObserve(observeGameNumber);
                break;
            case "help":
                break;
            case "quit":
                System.exit(0);
                break;
// TODO: Implement Make move with websockets, Resign, Leave, and Highlight legal moves
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
            currentUsername = null;
        } else {
            errorMessageHandler(result.getMessage());
        }
    }

    private void handleObserve(String observeGameNumber) {
        // TODO: Websockets
        System.out.print("Observing game " + observeGameNumber + ": ");
        // Check to see if the game number is valid
        if(Integer.parseInt(observeGameNumber) > games.size() || Integer.parseInt(observeGameNumber) < 1){
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error: Invalid game number\n*Don't forget to list the games first*\n" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            return;
        }
        int gameID = games.get(Integer.parseInt(observeGameNumber) - 1).getGameID();

        JoinGameResult result = client.joinGame(gameID, "SPECTATOR", currentUserAuthToken);

        if(result == null) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error: Could not observe game\n" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            return;
        }

        if(result.getMessage() == null){
            System.out.println("Success\n");
            ChessBoard board = new Chess_Board();
            board.resetBoard();
            showBoard("white", board);
            showBoard("black", board);
        } else {
            errorMessageHandler(result.getMessage());
        }
    }

    private void handleJoin(int gameNumber, String team) throws IOException {
        // TODO: Websockets
        System.out.println("Joining game " + gameNumber + " as " + team + ": ");
        if(!team.equalsIgnoreCase("white") && !team.equalsIgnoreCase("black")){
            System.out.println("Error: Invalid team color");
            return;
        }

        //Check if game number is valid
        if(gameNumber > games.size() || gameNumber < 1){
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error: Invalid game number\n*Don't forget to list the games first*\n" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            return;
        }

        int gameID = games.get(gameNumber - 1).getGameID();
        JoinGameResult result = client.joinGame(gameID, team, currentUserAuthToken);

        if(result == null) {
            System.out.println("Error: Could not join game");
            return;
        }

        //TODO: This will change
        if(result.getMessage() == null){
            System.out.println("Success\n");
            ChessBoard board = new Chess_Board();
            board.resetBoard();
            showBoard("white", board);
            showBoard("black", board);

            team = team.toUpperCase();
            ChessGame.TeamColor teamColor = team.equalsIgnoreCase("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            JoinPlayerCommand command = new JoinPlayerCommand(gameID, teamColor, currentUsername, currentUserAuthToken);
            websocketClient.send(command);
            System.out.println("\n You should've joined game " + gameID + " as " + teamColor + "\n");

        } else {
            errorMessageHandler(result.getMessage());
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
            int i = 1;
            games.clear();
            for (Game game : result.getGames()) {
                System.out.println(i + ") " + "Game Name: " + game.getGameName() +
                        " | White Player: " + game.getWhiteUsername() +
                        " | Black Player: " + game.getBlackUsername());
                games.add(game);
                i++;
            }
            System.out.println();
        } else {
            errorMessageHandler(result.getMessage());
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
            errorMessageHandler(result.getMessage());
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
            currentUsername = username;
        } else {
            errorMessageHandler(result.getMessage());
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
            currentUsername = username;
        } else {
            errorMessageHandler(result.getMessage());
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
            System.out.println("   a   b   c  d   e  f   g   h");
        } else {
            System.out.println("   h   g   f  e   d  c   b   a");
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
            System.out.println("   a   b   c  d   e  f   g   h\n");
        } else {
            System.out.println("   h   g   f  e   d  c   b   a\n");
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR);
    }

    public String errorMessageHandler(String message) {
        // Check to see if the message contains the number 400
        if (message.contains("400")) {
            message = "Error: Bad Request\n";
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            return message;
        } else if(message.contains("401")){
            message = "Error: Unauthorized\n";
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            return message;
        } else if(message.contains("403")){
            message = "Error: Already Taken\n";
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            return message;
        }
        else{
            message = "Error: Server Problem (Or something else)\n";
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            return message;
        }
    }

    @Override
    public void message(String message) {
        if (message != null){
            System.out.println(message);
        }
    }

    @Override
    public void updateBoard(ChessBoard board) {
        this.game.setBoard(board);
    }

    @Override
    public void error(String error) {
        if (error != null){
            System.out.println(error);
        }
    }

}

