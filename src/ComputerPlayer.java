import java.io.*;
import java.util.Random;

public class ComputerPlayer {
    int low; // Lowest value in the guessable range
    int high; // Highest value in the guessable range
    private int lastGuess; // Holds the previous guess so that the guessable range can be updated accordingly
    private final Random random;
    private final File file = new File("MLData.txt");
    private boolean isFirstGuess = true;   // Used to switch from ML to STD after first guess

    public ComputerPlayer(int low, int high){
       this.low = low;
       this.high = high;
       this.random = new Random();

    }

    // Choose a number in the given range for the computer to guess
     public int computerMakeGuess(){
        lastGuess = NumberGuessGame.safeRandomInt(random, low, high);
     return lastGuess;
    }

    // Checks to see if the data file has contents. If true, use ML
    public boolean computerUseML(){
        return file.exists() && file.length() > 0;
    }

    // Calculates the required adjustment to the initial guess based on the data in the data file
    int getMLGuess(){

        // Initialize sums and line count for all data in the data file
        int sumDataTarget = 0;
        int sumDataOffset = 0;
        int validLineCount = 0;

        // Read the data from the file, line by line, taken from the file in large chunks
        // FileReader reads only in one line sections and then pulls the next line to read. BufferedReader pulls large
        // chunks of data to be read. This way, FileReader doesn't have to pull data every time that it wants to read
        try (BufferedReader br = new BufferedReader(new FileReader(file))){

            String line;
            while ((line = br.readLine()) != null){   // While the next line is not empty

                // Take in data lin-by-line. Trim white spaces from the end, and split the data at the space
                String[] parts = line.trim().split("\\s+");

                if (parts.length == 2){   // Make sure that there are two numbers per line
                    try{

                        // The target number is the first number on each line
                        int target = Integer.parseInt(parts[0]);
                        // The offset amount is the second number on each line
                        int offset = Integer.parseInt(parts[1]);

                        sumDataTarget += target;   // Add the target number to the sum of all so far
                        sumDataOffset += offset;   // Add the offset number to the sum of all so far

                        // Keeps the number of lines so that it can be used for calculating averages
                        validLineCount ++;
                    }

                    catch (NumberFormatException e){
                        // Pass the line and read the next one if the format is incorrect
                    }

                }

            }

        }

        catch (IOException e){
            return 0;
        }

        if (validLineCount == 0) return 0;

        // Get the average target number from data
        float avgTarget = sumDataTarget / (float) validLineCount;
        // Get the average offset number from data
        float avgOffset = sumDataOffset / (float) validLineCount;

        // Get the average adjusted number
        float guessPercentage = avgTarget + avgOffset;
        int MLGuess = Math.round(high * (guessPercentage / 100f));


        // Check to make sure that the adjusted guess is not outside the allowed range
        if (MLGuess > high) {
            return high;
        }
        else if (MLGuess < low){
            return low;
        }

        return MLGuess;
    }

    // Creates and/or adds to the data file used to predict the most likely target number
    void handleMLFile(int target, int firstGuess, int high){

        // Calculate the average percentage of the range that the target number is
        float targetPercentage = ((float) target / high) * 100;
        // Calculate the average percentage difference between the initial guess and the target number
        float offsetPercentage = ((target - firstGuess) / (float) high) * 100;

        // Round the output percentages to save as a whole number
        int roundedTarget = Math.round(targetPercentage);
        int roundedOffset = Math.round(offsetPercentage);

        // Write the data to a text file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
            writer.write(roundedTarget + " " + roundedOffset);
            writer.newLine();
        }
        catch (IOException e){
            System.out.println("Error writing data to the data file");
        }
    }

    // Used to compare the computer's guesses to the target number
    public String checkComputerGuess(int currentGuess, int target){

        // Check if the guess is equal to the target.
        // If the guess was lower than the target, return 'higher', meaning that a higher guess is needed
        // If the guess was higher than the target, return 'lower', meaning that a lower guess is needed
        return currentGuess == target ? "correct" : (currentGuess < target ? "higher" : "lower");
    }

    // Records the guessing performance of the computer to a CSV file
    void recordPerformanceToCSV(int firstGuess, int target){
        String fileName = "GameResults.CSV";
        File file = new File(fileName);
        boolean fileExists = file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
            if (!fileExists){
                writer.write("Initial Guess,Target Number,Absolute Difference, Range");
                writer.newLine();
            }

            float difference = Math.abs(((float) target - firstGuess) / high) * 100;
            String line = String.format("%d,%d,%.1f%%,%d-%d", firstGuess, target, difference, low, high);
            writer.write(line);
            writer.newLine();
        }
        catch (IOException e){
            System.out.println("Error writing data to CSV file");
        }
    }

    // Combines all required instructions for one computer turn, and updates values to prepare for the next turn
    public boolean handleComputerTurn(int target){
        int guess;
        if (isFirstGuess){
            guess = computerUseML() ? getMLGuess() : computerMakeGuess();
            handleMLFile(target, guess, high);
            recordPerformanceToCSV(guess, target);
            isFirstGuess = false;   // The first guess has been made. Flip 'isFirstGuess' to false
        }
        else {
            guess = computerMakeGuess();
        }

        lastGuess = guess;

        String guessResult = checkComputerGuess(guess, target);

        System.out.println("The computer guessed " + guess);

        if (guessResult.equals("correct")) {
            System.out.println("The number was " + target + ". The computer wins!");
            return true;

        }
        else if (guessResult.equals("higher")) {
            this.low = lastGuess + 1;
        }
        else {
            this.high = lastGuess - 1;

        }
        return false;
    }
}

