package pentago;

import java.util.Random;
import java.util.Scanner;

/**
 * Pentago board game runner class. 
 * @author Cade Reynoldson
 */
public class Pentago {
    
    /** The name of the AI. */
    private final String aiName;
    
    /** The token the AI will use. */
    private char aiToken;
    
    /** The user's name. */
    private String playerName;
    
    /** The token the player is using. Either b or w */
    private char playerToken;
    
    /** The scanner used for user input. */
    private Scanner userInput;
    
    /** The game tree of potential moves the AI can make. */
    private GameTree gameTree;
    
    /** The ai turn look ahead count. */
    private int aiLookAhead;
    
    /** Indicates if it's the AI's turn. */ 
    private boolean aiTurn;
    
    /** The evaluation function to use. Defaults to my "smart(ish)" evaluation function. (2 point bonus for win block bonus) */
    private UtilityFunction evaluationFunction;
    
    /** Indicates if alpha beta pruning is to take place in the game. Default value is true. */
    private boolean alphaBetaPruning;

    /**
     * Creates a new instance of the pentago game. 
     * @param aiName the name of the AI.
     * @param aiLookAhead the AI lookahead count. 
     */
    public Pentago(String aiName, int aiLookAhead) {
        this.aiName = aiName;
        this.aiLookAhead = aiLookAhead;
        alphaBetaPruning = true;
        evaluationFunction = new UtilityFunction(2);
    }
    
    /**
     * Runs the pentago game. 
     */
    public void runGame() { 
        getUserInfo();
        char[][] currentState = getFirstMoves();
        while (GameState.isWinningState(currentState) == 'n' && GameState.canMakeMove(currentState)) { //While the current state is not a winning state. 
            System.out.println("+--------------------------------------------+");
            GameState.printInputState(currentState);
            if (aiTurn) { //If it's the AI's turn. 
                GameTreeNode aiChoice = gameTree.getNext(); //Get the ai's next move.
                System.out.println(aiName + " (token = " + aiToken + ") chooses: " + aiChoice.getArgs());
                currentState = aiChoice.getState();
                aiTurn = false; 
            } else { //If it's the players turn. 
                userInput = new Scanner(System.in);
                String args = getPlayerInput(currentState);
                while (args == null) {
                    args = getPlayerInput(currentState);
                }
                currentState = gameTree.updateNext(args);
                aiTurn = true;
            }
        }
        System.out.println("+--------------------------------------------+");
        printWinners(GameState.isWinningState(currentState));
        System.out.println("Final Board State: ");
        GameState.printState(currentState);
        userInput.close();
    }
    
    /**
     * Fetches the first moves of the game. 
     * @return a the first state of the game. 
     */
    public char[][] getFirstMoves() {
        char[][] currentState = GameState.initialState;
        if (aiGoesFirst()) {
            gameTree = new GameTree(currentState, aiToken, aiLookAhead, evaluationFunction, alphaBetaPruning);
            GameTreeNode aiChoice = gameTree.getNext(); //Get the ai's next move.
            GameState.printInputState(currentState);
            System.out.println("Computer goes first! ");
            System.out.println(aiName + " (token = " + aiToken + ") chooses: " + aiChoice.getArgs());
            currentState = aiChoice.getState();
            aiTurn = false;
        } else {
            GameState.printInputState(currentState);
            System.out.println(playerName + " goes first!: ");
            String args = getPlayerInput(currentState);
            currentState = GameState.alter(currentState, playerToken, args);
            gameTree = new GameTree(currentState, aiToken, aiLookAhead, evaluationFunction, alphaBetaPruning);
            aiTurn = true; 
        }
        return currentState;
    }
    
    /**
     * Gets player input for their next move. 
     * @param currentState the current state to apply the move to. 
     * @return the arguments provided to generate the next move. 
     */
    public String getPlayerInput(char[][] currentState) {
        userInput = new Scanner(System.in);
        System.out.print(playerName + "'s turn (token = " + playerToken + ") - Enter your move (b/p bd): ");
        String args = userInput.nextLine();
        while (!GameState.isValidArgument(args)) {
            System.out.print("Invalid argument! Enter your move (b/p bd): ");
            args = userInput.nextLine();
        }
        try {
            currentState = GameState.alter(currentState, playerToken, args);
        } catch (IllegalArgumentException e) {
            System.out.print("Invalid input (cannot replace existing point token). Restarting turn.");
            return null;
        } 
        return args.toLowerCase();
    }
    
    /**
     * Generates a random number for determining if the AI or player will go first. 
     * @return true is the ai goes first, false otherwise. 
     */
    public boolean aiGoesFirst() {
        Random r = new Random();
        int randomNum = r.nextInt(2);
        return randomNum == 0;
    }
    
    /**
     * Prints the winners given a win condition generated by GameState.java
     * @param winnerConditions the winning conditions to print. 
     */
    private void printWinners(char winnerConditions) {
        if (winnerConditions == 'b') { //If b won. 
             System.out.println("Token B has won!");
        } else if (winnerConditions == 'w') { //If w won. 
            System.out.println("Token W has won!");
        } else if (winnerConditions == 't') { //If it was a tie. 
            System.out.println("It's a tie!");
        } else { //If game ended with no winner.
            System.out.println("Game ended! No more moves to be made.");
        }
    }
    
    /**
     * Intializes the fields contained in this class VIA user input from the console. 
     */
    public void getUserInfo() {
        userInput = new Scanner(System.in);
        System.out.print("Enter Player Name: ");
        playerName = userInput.nextLine();
        System.out.print("Enter Token Identifier (b or w): ");
        playerToken = userInput.nextLine().toLowerCase().charAt(0);
        while (playerToken != 'b' && playerToken != 'w') {
            System.out.print("Invalid Token Input. You entered: " + playerToken +"\nEnter Token Identifier (b or w): ");
            playerToken = userInput.nextLine().toLowerCase().charAt(0);
        }
        if (playerToken == 'b')
            aiToken = 'w';
        else
            aiToken = 'b';
    }
    
    /**
     * Main function to run the game. 
     * @param args N/A.
     */
    public static void main(String[] args) {
        Pentago game = new Pentago("Computer", 2);
        game.runGame();
    }
    
}
