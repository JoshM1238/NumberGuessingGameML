import java.util.Scanner;

public class UserTurn {

    int target;   // Target number will be passed as an argument, and held by this variable
    private final Scanner scanner;   // Scanner from NumberGuessGame Class. Meant to take user input

    public UserTurn(int target, Scanner scanner) {
        this.target = target;
        this.scanner = scanner;
    }

    // Get the guess from the user, and validate it as an integer
    public int getUserGuess() {
        while (true) {
            System.out.print("Enter your guess: ");
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                scanner.nextLine(); // Consume the buffer
                return num;
            }
            else {
                System.out.println("Please enter a valid number");
                scanner.nextLine(); // Clear the buffer so that new input can be entered
            }

            }
        }

    // Used to check the user's guess against the target number
    public boolean checkUserGuess(int guess){

        if (guess < target){
            System.out.println("Too low. Guess higher.");
        }
        else if (guess > target){
            System.out.println("Too high. Guess lower.");
        }
        else{
            System.out.println("You guessed correctly. You win!");
            return true;   // The user guessed correctly. Game over
        }
        return false;   // User guessed incorrectly. The game will continue
    }

    // Organizes the user's turn into an easily callable handle function
    public boolean handleUserTurn(){
        int guess = getUserGuess();
        return checkUserGuess(guess);
    }
}
