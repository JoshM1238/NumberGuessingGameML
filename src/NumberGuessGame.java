import java.util.Random;
import java.util.Scanner;

public class NumberGuessGame {

    final private static Scanner scanner = new Scanner(System.in);   // For taking input from the user
    final private static Random random = new Random();   // For choosing random values. Ex. guesses

    public static void main(String[] args) {

        int low = 1; // Set the initial lowest guessable value to 1
        int high = highestNumber(); // Set the initial value of high to the user-defined top of the range
        int targetNumber = chooseTargetNumber(low, high);   // The number that we are attempting to guess
        boolean isComputerTurn = isComputerFirst();   // Initialize the turn to the output of isComputerFirst()
        boolean gameOver = false;
        ComputerPlayer computer = new ComputerPlayer(low, high); // Create an object of the ComputerPlayer class
        UserTurn userTurn = new UserTurn(targetNumber, scanner); // Create an object of the UserTurn class

        while(!gameOver){
            if (isComputerTurn){

                // Returns a bool value for whether the game is over
                gameOver = computer.handleComputerTurn(targetNumber);
                isComputerTurn = false;   // Switch to the user's turn
            }

            else {
                gameOver = userTurn.handleUserTurn();   // Check if the user won during their turn
                isComputerTurn = true;   // Switch the turn to the computer
            }
        }
        scanner.close();
    }

    // Checks a range to make sure that both ends are integer-storable
    // If the top end is exactly the highest value able to be stored by an integer variable
    // the top of the range is temporarily cast to long, and then a random value is chosen from the range, and
    // returned as an integer
    // This makes sure that you do not get an overflow when the Integer.MAX_VALUE is input, and that the range is
    // high-end inclusive
    public static int safeRandomInt(Random random, int low, int highInclusive) {

        // If low is higher than high, give an error
        if (low > highInclusive) {
            throw new IllegalArgumentException("Low cannot be greater than high");
        }

        // If the range only contains one number I.E. (1, 1), return that number
        // This ensures dynamic usability for this function
        if (low == highInclusive) {
            return low;  // Only one possible value
        }

        // If the argument is the same as Integer.MAX_VALUE, cast the high end to a long, choose the number,
        // and then return the randomly chosen number back as in the integer form
        // This works by allowing the top end of the range to temporarily be Integer.MAX_VALUE + 1, meaning
        // that IntegerMAX_VALUE can be chosen
        if (highInclusive == Integer.MAX_VALUE) {
            long range = (long) highInclusive - (long) low + 1;
            long result = (long) (random.nextDouble() * range) + low;
            return (int) result;
        }

        // Standard random choice from a range
        else {
            return random.nextInt(low, highInclusive + 1);
        }
    }


    // Used to let the user choose the largest number that can be chosen
    static int highestNumber() {
        while (true) {
            System.out.print("Enter Maximum Number: ");

            if (scanner.hasNextLong()) {   // Check input to make sure that it is an integer, loop again if not
                long input = scanner.nextLong();
                scanner.nextLine(); // Clear the remaining input if the input had a space followed by values

                // Check to make sure that the input is between 2 and the largest storable integer
                if (input >= 2 && input <= Integer.MAX_VALUE){
                    return (int) input;   // The input becomes the top of the range
                }

                // If the number was not higher than 1, show an error message and reprompt the user
                else {
                    System.out.println("Please enter a number between 2 and " + Integer.MAX_VALUE);
                }
            }
            else {
                System.out.println("Invalid entry. Please enter a valid number between 2 and " + Integer.MAX_VALUE);
                scanner.nextLine();   // Clear the buffer in the event that the input is not an integer
            }
        }
    }

   // Used to check who the user wants to go first: the computer, or the user
   static boolean isComputerFirst() {
        System.out.println("Who do you want to guess first? Your options are as follows:\n");
        System.out.println("> 'Computer'");
        System.out.println("> 'User'\n");
        System.out.print("First Turn Belongs to: ");

        String firstTurnChoice;

        // Make sure that the user enters a valid input. If not, clear the buffer and re-prompt
        while (true) {

            firstTurnChoice = scanner.nextLine().toLowerCase().strip();

            if (firstTurnChoice.equals("user") || firstTurnChoice.equals("computer")){
                break;
            }

            System.out.println("Invalid entry. Please enter either 'user' or 'computer'");
            System.out.print("First Turn Belongs to: ");
        }

        // Return a boolean value to decide if the computer makes the initial guess
        return firstTurnChoice.equals("computer");
    }

   // Picks the number that is to be guessed
   static int chooseTargetNumber(int low, int maxAllowed){
       return safeRandomInt(random, low, maxAllowed);
   }

}

