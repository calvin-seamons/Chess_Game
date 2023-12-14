import Models.Game;
import Results.*;
import chess.*;
import ui.EscapeSequences;
import ui.HTTPClient;
import ui.NotificationHandler;
import ui.WebsocketClient;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;
import java.util.concurrent.CountDownLatch;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import com.google.gson.Gson;


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
    private Chess_Game game = new Chess_Game();
    ChessGame.TeamColor teamColor;
    private int currentGameID;
    static String URL = "http://localhost:8080";
    NotificationHandler notificationHandler;
    Gson gson = new Gson();

    public ChessClient() throws Exception{
        client = new HTTPClient(URL);
        websocketClient = new WebsocketClient("ws://localhost:8080/connect", this);
        Chess_Board board = new Chess_Board();
        board.resetBoard();
        game.setBoard(board);
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
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + "Redraw Chess Board - " + EscapeSequences.SET_TEXT_COLOR_YELLOW + "To redraw the chess board ('redraw')" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Make Move - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To make a move" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Highlight Legal Moves - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To highlight legal moves ('legal moves')" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Resign - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To resign" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Leave - " + EscapeSequences.SET_TEXT_COLOR_YELLOW  + "To leave the game" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
            System.out.println("Help - " + EscapeSequences.SET_TEXT_COLOR_YELLOW + "To display this menu" + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        }
    }

    private void processInput(String input) throws IOException {
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
            case "legal moves":
                System.out.println("What piece do you want to move? ");
                System.out.print("Enter row: ");
                int theRow = Integer.parseInt(new Scanner(System.in).nextLine());
                System.out.print("Enter column: ");
                int theCol = Integer.parseInt(new Scanner(System.in).nextLine());
                handleLegalMoves(theRow, theCol);
                break;
            case "redraw":
                handleRedrawBoard(teamColor);
                break;
            case "make move":
                System.out.println("What piece do you want to move? ");
                System.out.print("Enter row: ");
                int row = Integer.parseInt(new Scanner(System.in).nextLine());
                System.out.print("Enter column: ");
                int col = Integer.parseInt(new Scanner(System.in).nextLine());
                System.out.println("Where do you want to move it? ");
                System.out.print("Enter row: ");
                int newRow = Integer.parseInt(new Scanner(System.in).nextLine());
                System.out.print("Enter column: ");
                int newCol = Integer.parseInt(new Scanner(System.in).nextLine());
                handleMove(row, col, newRow, newCol);
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
            case "leave":
                handleLeave();
                break;
            case "logout":
                handleLogout();
                break;
            case "observe":
                System.out.print("Enter Game Number: ");
                String observeGameNumber = new Scanner(System.in).nextLine();
                handleObserve(observeGameNumber);
                break;
            case "resign":
                handleResign();
                break;
            case "help":
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command\n");
                break;
        }
    }

    private void handleLegalMoves(int theRow, int theCol) {
        // Implementation for highlight legal moves
        System.out.println("Highlighting legal moves: ");
        ChessPosition chessPosition = new Chess_Position(theRow, theCol);
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) game.getBoard().getPiece(chessPosition).pieceMoves(game.getBoard(), chessPosition);
        // TODO: Highlight the legal moves
        for (ChessMove move : moves) {
            System.out.println("Row: " + move.getEndPosition().getRow() + " Col: " + move.getEndPosition().getColumn());
        }
        System.out.println();
    }

    private void handleRedrawBoard(ChessGame.TeamColor teamColor) {
        // Implementation for redraw chess board
//        System.out.println("Redrawing chess board: ");
        // If teamColor is null, then print both boards
        if (teamColor == null) {
            showBoard("white", game.getBoard());
            showBoard("black", game.getBoard());
            return;
        }

        showBoard(teamColor.toString().toLowerCase(), game.getBoard());
    }

    private void handleMove(int row, int col, int newRow, int newCol) throws IOException {
        if(game.getTeamTurn() != teamColor){
            System.out.println("It is not your turn");
            return;
        }
        ChessPosition chessPosition = new Chess_Position(row, col);
        ChessPosition newChessPosition = new Chess_Position(newRow, newCol);
        ChessMove move = new Chess_Move(chessPosition, newChessPosition);

        MakeMoveCommand command = new MakeMoveCommand(row, col, newRow, newCol, currentUserAuthToken, currentGameID, gson.toJson(game));
        websocketClient.send(command);
        try{
            Thread.sleep(500);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleResign() throws IOException {
        ResignCommand command = new ResignCommand(currentUserAuthToken, currentGameID);
        websocketClient.send(command);
//        System.out.println("Resigning game " + currentGameID + ": ");
        currentGameID = 0;
        currentState = AppState.POST_LOGIN;
    }

    private void handleLeave() throws IOException{
        LeaveCommand command = new LeaveCommand(currentUserAuthToken, currentGameID, teamColor);
        websocketClient.send(command);
        System.out.println("Leaving game " + currentGameID + ": ");
        currentGameID = 0;
        currentState = AppState.POST_LOGIN;
    }

    private void handleLogout() {
        // Implementation for logout
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

    private void handleObserve(String observeGameNumber) throws IOException {
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
            currentGameID = gameID;

            // Implementing the WebSocket
            JoinObserverCommand command = new JoinObserverCommand(gameID, currentUserAuthToken);
            websocketClient.send(command);
            currentState = AppState.IN_GAME;
        } else {
            errorMessageHandler(result.getMessage());
        }
    }

    private void handleJoin(int gameNumber, String team) throws IOException, InterruptedException {
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
//            ChessBoard board = new Chess_Board();
//            board.resetBoard();
//            showBoard("white", board);
//            showBoard("black", board);

            team = team.toUpperCase();
            ChessGame.TeamColor teamColor = team.equalsIgnoreCase("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

            currentGameID = gameID;
            this.teamColor = teamColor;
            JoinPlayerCommand command = new JoinPlayerCommand(gameID, teamColor, currentUserAuthToken);

            websocketClient.send(command);
            try{
                Thread.sleep(500);
            } catch (Exception e){
                e.printStackTrace();
            }


            currentState = AppState.IN_GAME;
//            System.out.println("\n You should've joined game " + gameID + " as " + teamColor + "\n");

        } else {
            errorMessageHandler(result.getMessage());
        }
    }

    private void handleList() {
        // Implementation for list
//        System.out.print("Listing games: ");
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
//        System.out.print("Creating game " + gameName + ": ");
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
//        System.out.print("Registering user " + username + " with password " + password + " and email " + email + ": ");

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
//        System.out.print("Logging in user: ");
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

    public void errorMessageHandler(String message) {
        // Check to see if the message contains the number 400
        if (message.contains("400")) {
            message = "Error: Bad Request\n";
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        } else if(message.contains("401")){
            message = "Error: Unauthorized\n";
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        } else if(message.contains("403")){
            message = "Error: Already Taken\n";
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        }
        else{
            message = "Error: Server Problem (Or something else)\n";
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message + EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        }
    }

    @Override
    public void message(String message) {
        if (message != null){
            System.out.println(message);
        }
    }

    @Override
    public void updateBoard(Chess_Game game) {
        this.game = game;
        showBoard(teamColor.toString().toLowerCase(), game.getBoard());
    }

    @Override
    public void error(String message) {
        if (message != null){
            System.out.println(message);
        }
    }

}

