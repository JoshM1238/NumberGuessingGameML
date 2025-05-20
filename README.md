# Number Guessing Game (ML-Enhanced Version)

This is a terminal-based Java number guessing game where the computer attempts to guess a randomly selected number. Over time, the game learns from past attempts using simple pattern-based machine learning logic to improve its initial guesses.

---

## How It Works

- The computer tries to guess a hidden number between 1 and a given max range.
- On the first guess, it may use a **machine-learning-informed estimate** based on past games stored in `MLData.txt`.
- After each guess, the game narrows the range based on whether the guess was too high or too low.
- Once the game ends, it logs the outcome to:
  - `MLData.txt` (used for ML-based guessing)
  - `GameResults.csv` (for historical game stats)

---

## Machine Learning Logic

- After each game, the system logs:
  • The percentage position of the target number
  • The percentage offset between the first guess and the target
- On future runs, it averages these to make more informed first guesses.

---

## Files

| File Name        | Purpose                                              |
|------------------|------------------------------------------------------|
| `NumberGuessGame.java` | Main driver of the game logic                  |
| `UserTurn.java`        | Handles user's turn logic                      |
| `ComputerPlayer.java`  | ML logic and standard guessing strategy        |
| `MLData.txt`           | Stores data used for learning (ignored in Git) |
| `GameResults.csv`      | Stores outcome logs (ignored in Git)           |

---

##  How to Run

From the command line:

javac *.java
java NumberGuessGame
