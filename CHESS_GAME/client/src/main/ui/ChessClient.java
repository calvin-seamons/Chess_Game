package ui;

import java.util.Scanner;

public class ChessClient {

    private enum AppState {
        PRE_LOGIN, POST_LOGIN
    }

    private final AppState currentState = AppState.PRE_LOGIN;

    public static void main(String[] args) {
        ChessClient client = new ChessClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println(EscapeSequences.BLACK_KING + " Welcome to the Chess Application " + EscapeSequences.WHITE_KING);

        while (true) {
            client.displayMenu();
            String input = scanner.nextLine();
            client.processInput(input);
        }
    }

    private void displayMenu() {
        if (currentState == AppState.PRE_LOGIN) {
            // Display pre-login menu
            System.out.println("Register <username> <password><email> - To create an account");
            System.out.println("Login <username> <password> - To play chess");
            System.out.println("Quit - To quit playing chess");
            System.out.println("Help - To display this menu");

        } else {
            System.out.println("Create <gameName> - To create a game");
            System.out.println("List - To list all games");
            System.out.println("Join <gameID> [WHITE|BLACK|<empty>] - To join a game");
        }
    }

    private void processInput(String input) {
        switch (input.toLowerCase()) {
            case "login":
                System.out.println("Enter username: ");
                handleLogin();
                break;
            // Handle other cases
        }
    }

    private void handleLogin() {
        // Implementation for login
        // On successful login, change state to POST_LOGIN
    }

    // Other methods for different commands
}

