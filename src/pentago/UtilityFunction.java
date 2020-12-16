package pentago;

public class UtilityFunction {
    
    /** The value to add to a blocking bonus. */
    private int blockingBonus;
    
    /** Indicates if advanced evaluation will take place. */
    private boolean advanced;
    
    /**
     * Instantiates the standard version of the utility function. 
     */
    public UtilityFunction() {
        blockingBonus = 0;
        advanced = false;
    }
    
    /**
     * Instantiates a utility function with bonus values to add to a value.
     * @param blockingBonus the blocking bonus to add to a function. 
     * @param winBlockBonus the blocking bonus added to when a win is blocked.
     */
    public UtilityFunction(int blockingBonus) {
        this.blockingBonus = blockingBonus;
        advanced = true; 
    }
    
    /**
     * Evaluates a node based on the orignal states values compared to the current states values.
     * - My attempt at a "smarter" implementation. Adds extra points according to the values the utility function was instantiated with. 
     * - Prioritizes blocking wins.   
     * @param originalState the original state to check.
     * @param toEvaluate the state to evaluate. 
     */
    public void evaluate(char[][] originalState, GameTreeNode toEvaluate) {
        int[] parentTotals = getPossibleWins(originalState, toEvaluate.getOppositeToken());
        int[] currentTotals = getPossibleWins(toEvaluate.getState(), toEvaluate.getToken());
        char isWinner = GameState.isWinningState(toEvaluate.getState());
        if (isWinner == toEvaluate.getToken()) { //If this nodes token is a winner - OPTIMIZE OVER TIES. 
            if (toEvaluate.isMaximizer())
                toEvaluate.setScore(Integer.MAX_VALUE);
            else
                toEvaluate.setScore(Integer.MIN_VALUE); 
        } else {
            int maximizerNerf = currentTotals[0] + ((currentTotals[0] - parentTotals[0]) * blockingBonus);
            int minimizerNerf = currentTotals[1] + ((currentTotals[1] - parentTotals[1]) * blockingBonus); 
            if (toEvaluate.isMaximizer()) { //If this is the maximizer node. 
                toEvaluate.setScore(maximizerNerf - minimizerNerf);
            } else {
                toEvaluate.setScore(minimizerNerf - maximizerNerf);
            }
        }
    }
    
    /**
     * Uses the base evaluation function. Less computationally expensive. 
     * @param toEvaluate
     */
    public void evaluate(GameTreeNode toEvaluate) {
        char isWinner = GameState.isWinningState(toEvaluate.getState());
        if (isWinner == toEvaluate.getToken()) { //If this nodes token is a winner. 
            if (toEvaluate.isMaximizer())
                toEvaluate.setScore(Integer.MAX_VALUE);
            else
                toEvaluate.setScore(Integer.MIN_VALUE); 
        } else {
            int[] wins = getPossibleWins(toEvaluate.getState(), toEvaluate.getToken());
            if (toEvaluate.isMaximizer()) //if this token corresponds to the maximum token.
                toEvaluate.setScore(wins[0] - wins[1]);
            else                                          //Else, it is a minimizer. 
                toEvaluate.setScore(wins[1] - wins[0]); 
        }
    }
    
    /**
     * Returns how many possible wins a player could have.  
     * Corresponding Indexes:
     * 0 - Total possible wins for the parameterized token. 
     * 1 - Total possible wins for the opposite token.
     * @param state the state to check for the win amount. 
     * @param token the token to count possible win states. 
     * @return the count of how many possible wins are available for a token. 
     */
    public int[] getPossibleWins(char[][] state, char token) {
        int[] totals = {0, 0};
        int[] horiz_vert = getPossibleWins_horizVert(state, token);
        int[] diag = getPossibleWins_diag(state, token);
        for (int i = 0; i < 2; i++) {
            totals[i] += horiz_vert[i];
            totals[i] += diag[i];
        }
        return totals;
    }
    
    /**
     * Calculates current amount of horizontal and vertical wins of each row in a 2d array.
     * Corresponding Indexes: 
     * 0 - Total possible wins for the parameterized token. 
     * 1 - Total possible wins for the other token. 
     * @param state the state to check for horizontal and vertical wins.
     * @param token the token to associate with index zero. 
     * @return the current amount of possible wins for both the horizontal and vertial directions. 
     */
    private int[] getPossibleWins_horizVert(char[][] state, char token) {
        int[] wins = {0, 0};
        for (int i = 0; i < 6; i++) {
            int[] vertCounts = {0, 0}; //Stores the counts of each value in the column. 
            int[] horizWins = {0, 0};
            for (int j = 1; j < 5; j++) { //Only loop through middle of the column to keep counts. They're the only values that have any effect on the outcome. 
                alterCount(state, horizWins, token, i, j);
                alterCount(state, vertCounts, token, j, i);
            }
            alter_countsToWinners(wins, horizWins);
            alter_countsToWinners(wins, vertCounts);
        }
        return wins;
    }
    
    /**
     * Counts the possible wins of both of the tokens with the parameterized token in the first index. 
     * @param state the state to check for possible diagonal wins. 
     * @param token the token to put in index 0. 
     * @return the current amount of possible wins for diagonal wins. 
     */
    private int[] getPossibleWins_diag(char[][] state, char token) {
        int[] diagWins = {0, 0};
        int[] center = {0, 0};
        int[] abv_center = {0, 0};
        int[] blw_center = {0, 0};
        int[] revCenter = {0, 0};
        int[] abv_revCenter = {0, 0};
        int[] blw_revCenter = {0, 0};
        int currentRow = 4;
        for (int i = 1; i < 5; i++) {//Center loop
            alterCount(state, center, token, i, i); //Center
            alterCount(state, revCenter, token, currentRow - 1, i); //Reverse Center
        }
        currentRow = 4;
        for (int i = 0; i < 5; i++) {//Above and below.  
            alterCount(state, abv_center, token, i, i + 1);
            alterCount(state, blw_center, token, i + 1, i);
            alterCount(state, abv_revCenter, token, currentRow, i);
            alterCount(state, blw_revCenter, token, currentRow + 1, i + 1);
        }
        alter_countsToWinners(diagWins, center);
        alter_countsToWinners(diagWins, revCenter);
        alter_countsToWinners(diagWins, abv_center);
        alter_countsToWinners(diagWins, blw_center);
        alter_countsToWinners(diagWins, abv_revCenter);
        alter_countsToWinners(diagWins, blw_revCenter);
        return diagWins;
    }
    /**
     * Alters the count of a state given that it isn't a '.' token.
     * Parameters row and column refer to the corresponding index in the 2d array.  
     * @param state the state to alter. 
     * @param counts the counts of b and w tokens.
     * @param token the token to prioritize when altering the count array (will be in first index).
     * @param row the row of the state to take count of. 
     * @param column the column of the state to take count of. 
     */
    private void alterCount(char[][] state, int[] counts, char token, int row, int column) {
        if (state[row][column] == token && state[row][column] != '.')
            counts[0]++;
        else if (state[row][column] != '.')
            counts[1]++;
    }
    
    /**
     * Alters a paramterized array, converts counts to winners assuming that they were calculated on a mutually
     * exclusive bounds of indexing. (No winner can be determined if bout have one entry in the count array). 
     * @param wins the wins array to alter. 
     * @param counts the counts to convert to wins. 
     */
    private void alter_countsToWinners(int[] wins, int[] counts) {
        if ((counts[0] == 0 && counts[1] == 0)) { //if this is true, both players can win this row. 
            wins[0]++;
            wins[1]++;
        } else if (counts[0] > 0 && counts[1] == 0) { //If this condition is true, only the parameterized token can win this row. 
            wins[0]++;
        } else if (counts[1] > 0 && counts[0] == 0) { //If this condition is true, only the other token can win this row. 
            wins[1]++;
        }
    }
    
    /**
     * Returns true if advanced evaluation will take place. 
     * @return
     */
    public boolean isAdvanced() {
        return advanced;
    }
    
}
