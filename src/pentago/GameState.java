package pentago;

/**
 * GameState Class. Contains methods for rotating and manipulating a 4x(3x3) pentago game board.  
 * Game Positions:
 *         +-------+-------+
 *         | 1 2 3 | 1 2 3 |
 * Block 1 | 4 5 6 | 4 5 6 | Block 2
 *         | 7 8 9 | 7 8 9 |
 *         +-------+-------+
 *         | 1 2 3 | 1 2 3 |
 * Block 3 | 4 5 6 | 4 5 6 | Block 4
 *         | 7 8 9 | 7 8 9 |
 *         +-------+-------+
 * @author Cade Reynoldson. 
 */
public class GameState {
    
    /** The initial game state that pentago starts with. */
    public static final char[][] initialState = {{'.', '.', '.', '.', '.', '.'}, 
                                                 {'.', '.', '.', '.', '.', '.'}, 
                                                 {'.', '.', '.', '.', '.', '.'}, 
                                                 {'.', '.', '.', '.', '.', '.'},
                                                 {'.', '.', '.', '.', '.', '.'},
                                                 {'.', '.', '.', '.', '.', '.'}};
    
    /** The input state to display alongside the current state. */ 
    public static final char[][] inputState = {{'1', '2', '3', '1', '2', '3'}, 
                                               {'4', '5', '6', '4', '5', '6'},
                                               {'7', '8', '9', '7', '8', '9'},
                                               {'1', '2', '3', '1', '2', '3'},
                                               {'4', '5', '6', '4', '5', '6'},
                                               {'7', '8', '9', '7', '8', '9'}};
    
    /**
     * Alters the values contained in a given state based on the arguments provided.
     * Format for arguments: b/p bd
     * Where (see positions in GameState JavaDoc):
     * b/p
     * b = block number to replace value. 
     * p = position number
     * bd
     * b = block number to rotate. 
     * d = direction (either left or right).
     * - Assumes arguments are valid! Check with Pentago.isValidArgument(). 
     * @param state the state to alter. 
     * @param args the arguments to execute. Assumes the arguments are VALID!
     * @return an altered state based on the arguments. 
     */
    public static char[][] alter(char[][] state, char player, String args) throws IllegalArgumentException {
        char[][] newState = copyState(state);
        String[] splitArgs = args.split(" ");
        if (!replace(newState, player, splitArgs[0]))
            throw new IllegalArgumentException();
        if (isWinningState(newState) != 'n') //If replacing has allowed for a win, return the winning state.
            return newState;
        else                                 //If not, rotate the state and return. 
            rotate(newState, splitArgs[1]);
        return newState; 
    }
    
    /**
     * Alters the parameterized state and replaces the character corresponding to the corner and
     * position with their player id. ASSUMES ARGUMENTS ARE VALID!
     * @param state the state to replace the position in. 
     * @param player the player id (either b or w)
     * @param corner the corner to place the new piece. 
     * @param position the position to place the new piece. 
     * @return true if position can be replaced, false otherwise. 
     */
    public static boolean replace(char[][] state, char player, String args) {
        int row = 0;
        int column = 0;
        int corner = Integer.parseInt(((Character) args.charAt(0)).toString()); 
        int position = Integer.parseInt(((Character) args.charAt(2)).toString());
        int rowOffset;
        int columnOffset;
        if (corner == 1) {
            rowOffset = 0;
            columnOffset = 0;
        } else if (corner == 2) {
            rowOffset = 0;
            columnOffset = 3;
        } else if (corner == 3) {
            rowOffset = 3;
            columnOffset = 0;
        } else {
            rowOffset = 3;
            columnOffset = 3; 
        }
        switch (position) {
            case 1:
                row = 0;
                column = 0;
                break;
            case 2:
                row = 0;
                column = 1;
                break;
            case 3:
                row = 0;
                column = 2;
                break;
            case 4:
                row = 1;
                column = 0;
                break;
            case 5:
                row = 1;
                column = 1;
                break;
            case 6:
                row = 1;
                column = 2;
                break;
            case 7:
                row = 2;
                column = 0;
                break;
            case 8:
                row = 2;
                column = 1;
                break;
            case 9:
                row = 2;
                column = 2;
                break;
        }
        if (state[row + rowOffset][column + columnOffset] != '.') //If a token already exists in this location. 
            return false;
        state[row + rowOffset][column + columnOffset] = player;
        return true;
    }
    
    /**
     * Evaluates an argument to see if the token in the particular position can be replaced. 
     * @param state the state to check if a position can be replaced. 
     * @param args the arguments to check for. 
     * @return
     */
    public static boolean canReplace(char[][] state, String args) {
        int row = 0;
        int column = 0;
        int corner = Integer.parseInt(((Character) args.charAt(0)).toString()); 
        int position = Integer.parseInt(((Character) args.charAt(2)).toString());
        int rowOffset;
        int columnOffset;
        if (corner == 1) {
            rowOffset = 0;
            columnOffset = 0;
        } else if (corner == 2) {
            rowOffset = 0;
            columnOffset = 3;
        } else if (corner == 3) {
            rowOffset = 3;
            columnOffset = 0;
        } else {
            rowOffset = 3;
            columnOffset = 3; 
        }
        switch (position) {
            case 1:
                row = 0;
                column = 0;
                break;
            case 2:
                row = 0;
                column = 1;
                break;
            case 3:
                row = 0;
                column = 2;
                break;
            case 4:
                row = 1;
                column = 0;
                break;
            case 5:
                row = 1;
                column = 1;
                break;
            case 6:
                row = 1;
                column = 2;
                break;
            case 7:
                row = 2;
                column = 0;
                break;
            case 8:
                row = 2;
                column = 1;
                break;
            case 9:
                row = 2;
                column = 2;
                break;
        }
        if (state[row + rowOffset][column + columnOffset] != '.') //If a token already exists in this location. 
            return false;
        return true;
    }
    
    /**
     * Rotates a state given a direction and square to rotate it in. Note: does NOT alter the original state. 
     * @param state the state to rotate. 
     * @param dir the direction to rotate the state. 
     */
    public static void rotate(char[][] state, String dir) {
        try {
            int corner = Integer.parseInt(((Character) dir.charAt(0)).toString());
            char direction = Character.toLowerCase(dir.charAt(1));
            int rowOffset;
            int columnOffset;
            if (corner == 1) {
                rowOffset = 0;
                columnOffset = 0;
            } else if (corner == 2) {
                rowOffset = 0;
                columnOffset = 3;
            } else if (corner == 3) {
                rowOffset = 3;
                columnOffset = 0;
            } else {
                rowOffset = 3;
                columnOffset = 3; 
            }
            char[][] rotatedCorner;
            if (direction == 'r')
                rotatedCorner = rotateRight(state, rowOffset, columnOffset);
            else
                rotatedCorner = rotateLeft(state, rowOffset, columnOffset);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++)
                    state[i + rowOffset][j + columnOffset] = rotatedCorner[i][j];
            }
        } catch (Exception e) {
            System.out.println("Error in rotation.");
        }
    }
    
    /**
     * Creates a copy of a 3x3 char array which is a "rotated" to right version of the variables at the given row and
     * column offset. 
     * @param state the state to alter. 
     * @param rowOffset the row offset (calculated by rotate())
     * @param columnOffset the column offset (calculated by rotate())
     * @return a 3x3 matrix of a given corner rotated to the left.
     */
    private static char[][] rotateRight(char[][] state, int rowOffset, int columnOffset) {
        char[][] rotatedCorner = new char[3][3];
        int currentRow = 0;
        int currentColumn = 2; 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                rotatedCorner[currentRow++][currentColumn] = state[i + rowOffset][j + columnOffset];
            currentRow = 0;
            currentColumn--;
        }
        return rotatedCorner;
    }
    
    /**
     * Creates a copy of a 3x3 char array which is a "rotated" to the left version of the variables at the given row and
     * column offset. 
     * @param state the state to alter. 
     * @param rowOffset the row offset (calculated by rotate())
     * @param columnOffset the column offset (calculated by rotate())
     * @return a 3x3 matrix of a given corner rotated to the left.
     */
    private static char[][] rotateLeft(char[][] state, int rowOffset, int columnOffset) {
        char[][] rotatedCorner = new char[3][3];
        int currentRow = 2;
        int currentColumn = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                rotatedCorner[currentRow--][currentColumn] = state[i + rowOffset][j + columnOffset];
            currentRow = 2;
            currentColumn++;
        }
        return rotatedCorner;
    }
    
    /**
     * Copies a given state. 
     * @param state the state to copy. 
     * @return a copied a state. 
     */
    public static char[][] copyState(char[][] state) {
        char[][] copy = new char[state.length][state[0].length];
        for (int i = 0; i < state.length; i++) 
            for (int j = 0; j < state[i].length; j++)
                copy[i][j] = state[i][j];
        return copy;
    }
    
    /**
     * Returns a character formatted based on the following:
     * 'n' - If the state is not a winning state. 
     * 'w' - If the w character is the winner. 
     * 'b' - If the b character is the winner.
     * 't' - If the b and w character both win. Aka a tie. 
     * @return a character indicating if a winner, tie or no win has been found in the game. 
     */
    public static char isWinningState(char[][] state) {
        boolean[] horiz_vertWinners = isHorizontalOrVerticalWin(state);
        boolean[] diagWinners = isDiagonalWin(state);
        char conclusion = 'n';
        if (horiz_vertWinners[1] || diagWinners[1]) //If w is a winner.
            conclusion = 'w';
        if (horiz_vertWinners[0] || diagWinners[0]) { //If b is a winner. 
            if (conclusion == 'w')  //If w has already been found to be a winner, there is a tie. 
                conclusion = 't';
            else
                conclusion = 'b';
        }
        return conclusion; 
    }
    
    /**
     * Returns a boolean array which indexes correspond to:
     * - 0: a 'b' token win.
     * - 1: a 'w' token win. 
     * Only checks for horizontal or vertical win conditions!
     * @param state the state to check for win conditions. 
     * @return an array of size two with win conditions in the javadoc specified indexes. 
     */
    public static boolean[] isHorizontalOrVerticalWin(char[][] state) {
        boolean[] winners = {false, false};
        //Horizontal Check:
        for (int i = 0; i < 6; i++) { //Check each row. 
            int currentCounter = 0;
            char lastToken = '.';
            for (int j = 0; j < 6; j++) {
                if (state[i][j] != '.' && state[i][j] == lastToken) { //If this token is the same as the previous, increment counter. 
                    currentCounter++;
                    if (currentCounter == 5) { //if current counter == 5, a winner has been detected. Assures last token was the winning token.  
                        break;
                    }
                } else { 
                    if (j > 1) //If j is above index 0 or 1, there is no way five in a row can exist. 
                        break;
                    currentCounter = 1;
                    lastToken = state[i][j];
                }
            }
            if (currentCounter == 5)
                alterWinnerArray(winners, lastToken);
        }
        //Vertical Check: 
        for (int i = 0; i < 6; i++) { //Check each column. 
            int currentCounter = 0;
            char lastToken = '.';
            for (int j = 0; j < 6; j++) {
                if (state[j][i] != '.' && state[j][i] == lastToken) { //If this token is the same as the previous, increment counter. 
                    currentCounter++;
                    if (currentCounter == 5) { //if current counter == 5, a winner has been detected. Assures last token was the winning token.  
                        break;
                    }
                } else { 
                    if (j > 1) //If j is above index 0 or 1, there is no way five in a row can exist. 
                        break;
                    currentCounter = 1;
                    lastToken = state[j][i];
                }
            }
            if (currentCounter == 5)
                alterWinnerArray(winners, lastToken);
        }
        return winners; 
    }
    
    /**
     * Returns a boolean array which indexes correspond to:
     * - 0: a 'b' token win.
     * - 1: a 'w' token win. 
     * Only checks for diagonal win conditions!
     * @param state the state to check for win conditions. 
     * @return an array of size two with win conditions in the javadoc specified indexes. 
     */
    public static boolean[] isDiagonalWin(char[][] state) {
        boolean[] winners = {false, false};
        //Diagonal (left to right check)
        int currentCounter = 0;
        char lastToken = state[0][0];
        for (int i = 0; i < 6; i++) { //Center diagonal. 
            if (state[i][i] != '.' && state[i][i] == lastToken) {
                currentCounter++;
                if (currentCounter == 5) 
                    break;
            } else {
                if (i > 1) //If i is above index 0 or 1, there is no way five in a row can exist. 
                    break;
                currentCounter = 1;
                lastToken = state[i][i];
            }
        }
        if (currentCounter == 5)
            alterWinnerArray(winners, lastToken);
        //Above center diagonal.
        lastToken = state[0][1];
        currentCounter = 0;
        for (int i = 0; i < 5; i++) {
            if (state[i][i + 1] != '.' && state[i][i + 1] == lastToken)
                currentCounter++;
            else if (i > 0) //to have a win under this condition, every single position must be filled out by the same token.
                break;
        }
        if (currentCounter == 5)
            alterWinnerArray(winners, lastToken);
        //Below center diagonal.
        lastToken = state[1][0];
        currentCounter = 0;
        for (int i = 0; i < 5; i++) {  
            if (state[i + 1][i] != '.' && state[i + 1][i] == lastToken) 
                currentCounter++;
            else if (i > 0) //to have a win under this condition, every single position must be filled out by the same token.
                break;
        }
        if (currentCounter == 5) {
            alterWinnerArray(winners, lastToken);
        }
        //Diagonal (Right to left)
        lastToken = state[5][0];
        currentCounter = 0;
        int currentRow = 5; //Needed for proper iteration through columns. 
        for (int i = 0; i < 6; i++) { //Center diagonal.
            if (state[currentRow][i] != '.' && state[currentRow][i] == lastToken) {
                currentCounter++;
                if (currentCounter == 5) 
                    break;
            } else {
                if (i > 1) //If i is above index 0 or 1, there is no way five in a row can exist. 
                    break;
                currentCounter = 1;
                lastToken = state[i][i];
            }
            currentRow--;
        }
        if (currentCounter == 5) {
            alterWinnerArray(winners, lastToken);
        }
        //Above center diagonal.
        lastToken = state[4][0];
        currentCounter = 0;
        currentRow = 4;
        for (int i = 0; i < 5; i++) {  
            if (state[currentRow][i] != '.' && state[currentRow][i] == lastToken) 
                currentCounter++;
            else if (i > 0) //to have a win under this condition, every single position must be filled out by the same token.
                break;
            currentRow--;
        }
        if (currentCounter == 5) {
            alterWinnerArray(winners, lastToken);
        }
        //Below Center Diagonal
        lastToken = state[4][1];
        currentCounter = 0;
        currentRow = 5;
        for (int i = 1; i < 6; i++) {  
            if (state[currentRow][i] != '.' && state[currentRow][i] == lastToken) 
                currentCounter++;
            else if (i > 0) //to have a win under this condition, every single position must be filled out by the same token.
                break;
            currentRow--;
        }
        
        if (currentCounter == 5) {
            alterWinnerArray(winners, lastToken);
        }
        return winners; 
    }
    
    /**
     * Alters the winners array based on the last token. 
     * @param winners the winners array. 
     * @param lastToken the last token (aka. the winner). 
     */
    public static void alterWinnerArray(boolean[] winners, char lastToken) {
        if (lastToken == 'w')
            winners[1] = true;
        else if (lastToken == 'b')
            winners[0] = true; 
    }
    
    /**
     * Parses an argument to check if it is valid. 
     * @param arg the argument to check for validity. 
     * @return true/false based on if the argument is valid.
     */
    public static boolean isValidArgument(String arg) {
        try {
            int b1 = Integer.parseInt(((Character) arg.charAt(0)).toString());
            int b2 = Integer.parseInt(((Character) arg.charAt(4)).toString());
            int p = Integer.parseInt(((Character) arg.charAt(2)).toString()); 
            char d = Character.toLowerCase(arg.charAt(5));
            if (!(b1 < 5 && b1 > 0) || !(b2 < 5 && b2 > 0) || !(p < 10 && p > 0) || !(d != 'l' || d != 'r'))
                return false; 
        } catch (Exception e) {
            return false; 
        }
        return true; 
    }
    
    /**
     * Searches a state to see if any future moves can be made. Returns false if board is filled. 
     * @param The state to check for all positions being filled. 
     * @return true if board has potential moves to be made, false otherwise. 
     */
    public static boolean canMakeMove(char[][] state) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (state[i][j] == '.')
                    return true; 
            }
        }
        return false;
    }
    
    /**
     * Evaluates two states and determines if they are equal (same tokens). 
     * @param state1 the first state. 
     * @param state2 the second state.
     * @return true if the states are equal, false otherwise. 
     */
    public static boolean equals(char[][] state1, char[][] state2) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (state1[i][j] != state2[i][j])
                    return false;
            }
        }
        return true; 
    }
    
    /**
     * Prints a state. 
     * @param state
     */
    public static void printState(char[][] state) {
        System.out.println(" +-------+-------+ ");
        for (int i = 0; i < state.length; i++) {
            if (i == 3)
                System.out.println(" +-------+-------+ ");
            for (int j = 0; j < state[i].length; j++) {
                if (j == 0) {
                    System.out.print(" | ");
                }
                System.out.print(state[i][j] + " ");
                if (j == 5 || j == 2)
                    System.out.print("| ");
            }
            System.out.println();
        }
        System.out.println(" +-------+-------+ ");
    }
    
    /**
     * Prints a state followed by the input state. 
     * @param state
     */
    public static void printInputState(char[][] state) {
        System.out.println("   Current State            Input Key     ");
        System.out.println(" +-------+-------+      +-------+-------+ ");
        for (int i = 0; i < state.length; i++) {
            if (i == 3)
                System.out.println(" +-------+-------+      +-------+-------+ ");
            for (int j = 0; j < state[i].length; j++) { //Print player state
                if (j == 0) {
                    System.out.print(" | ");
                }
                System.out.print(state[i][j] + " ");
                if (j == 5 || j == 2)
                    System.out.print("| ");
            }
            //Print input key
            
            if (i == 1) //Ensure proper spacing between prints. 
                System.out.print("   1 ");
            else if (i == 4)
                System.out.print("   3 ");
            else
                System.out.print("     ");
            for (int j = 0; j < inputState[i].length; j++) { //Print input key
                if (j == 0) {
                    System.out.print("| ");
                }
                System.out.print(inputState[i][j] + " ");
                if (j == 5 || j == 2)
                    System.out.print("| ");
            }
            if (i == 1) //Ensure proper spacing between prints. 
                System.out.println("2");
            else if (i == 4)
                System.out.println("4");
            else
                System.out.println();
        }
        System.out.println(" +-------+-------+      +-------+-------+ ");
    }
    
}
