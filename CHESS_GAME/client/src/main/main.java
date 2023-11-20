import ui.ChessClient;

public class main {
    //Takes the command line arguments and passes them into the Chess Client
    public static void main(String[] args) {
        ChessClient chessClient = new ChessClient();
        System.out.println("Chess Client is running");
        System.out.println("You said: ");
        for (String arg : args) {
            System.out.println(arg);
        }
        System.out.println("Enter \"help\" for a list of commands");
        chessClient.help();

    }
}
