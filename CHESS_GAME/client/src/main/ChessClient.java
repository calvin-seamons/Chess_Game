import Requests.RegisterRequest;
import ui.EscapeSequences;
import com.google.gson.Gson;

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
            case "register":
                System.out.println("Enter username: ");
                String username = new Scanner(System.in).nextLine();
                System.out.println("Enter password: ");
                String password = new Scanner(System.in).nextLine();
                System.out.println("Enter email: ");
                String email = new Scanner(System.in).nextLine();
                handleRegister(username, password, email);
                break;
        }
    }

    private void handleRegister(String username, String password, String email) {
        // Implementation for register
        Gson gson = new Gson();
        RegisterRequest request = new RegisterRequest(username, password, email);
        // Check if username is already taken
        // If not, create user



    }

    private void handleLogin() {
        // Implementation for login
        // On successful login, change state to POST_LOGIN
    }

    // Other methods for different commands
}

