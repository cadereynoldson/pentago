package pentago;

import java.util.HashMap;
import java.util.List;

/** 
 * Stores information about a state of pentago, along with methods to expand this node as a tree. 
 * @author Cade Reynoldson
 */
public class GameTreeNode {
    
    /** The state of this node. */
    private char[][] state;
    
    private final int depth;
    
    /** The score (established VIA. minimax or evaluation function) of this node. */
    private int score;
    
    /** The children of this node, mapped to the argument that generated them from this state.*/
    private HashMap<String, GameTreeNode> children;
    
    /** The token of this level of node. This is the current player which is to make a move. Childeren of this node will have the opposite token. */
    private char token;
    
    /** The parent game tree node. */
    private GameTreeNode parent;
    
    /** Indicates if node prioritizes towards max or min. */
    private boolean maximizer;
    
    /** Indicates if this node has been evaluated. */
    private boolean beenEvaluated;
    
    /** The arguments provided to generate this node. */
    private String args;
    
    /**
     * Creates a new instance of a game tree node.
     * @param parent the parent of this node. 
     * @param state the state of this node. 
     * @param token the token of this node. 
     * @param depth the depth of this node. 
     * @param maximizer whether this node is a maximizer or not. 
     */
    public GameTreeNode(GameTreeNode parent, char[][] state, char token, int depth, boolean maximizer, String args) {
        this.parent = parent;
        this.state = state;
        this.token = token;
        this.depth = depth;
        this.maximizer = maximizer;
        this.args = args; 
        beenEvaluated = false; 
        children = new HashMap<String, GameTreeNode>();
    }
    
    /**
     * Expands a node to a specified depth. 
     * @param toDepth
     */
    public long expand(int toDepth) {
        if (depth < toDepth) { //If depth of node is less than the desired depth, expand and then expand children. 
            long numExpanded = expand(); //expand node
            if (numExpanded == 0) { //If this node tries to expand and no nodes are generated, return minimum value. (END OF TREE)
                return Long.MIN_VALUE;
            }
            for (String args : children.keySet()) { //For all children, expand with the current depth level. 
                numExpanded += children.get(args).expand(toDepth);
            }
            return numExpanded;
        } else { //Return 0, indicating this is a leaf. 
            return 0; 
        }
    }
    
    /**
     * Expands the node with all possible moves that coubld be made.
     * @return the number of nodes expanded.   
     */
    public long expand() {
        char nextToken = getOppositeToken();
        for (int i = 1; i < 10; i++) { //Generate commands for positions 1-9. 
            for (int j = 1; j < 5; j++) { //Loop these commands for each block.
                if (GameState.canReplace(state, j + "/" + i)) { //If a position can be replaced.  
                    for (int o = 1; o < 5; o++) {//For each block, generate a node with position replaced and a block rotated.
                        String args1 = j + "/" + i + " " + o + "l";
                        String args2 = j + "/" + i + " " + o + "r";
                        children.put(args1, new GameTreeNode(this, GameState.alter(state, token, args1), nextToken, depth + 1, !maximizer, args1)); //Add with o index rotated right. 
                        children.put(args2, new GameTreeNode(this, GameState.alter(state, token, args2), nextToken, depth + 1, !maximizer, args2)); //Add with o index rotated left. 
                    }
                }
            }
        }
        return (long) children.size();
    }
    
    
    /**
     * Evaluates all leaf nodes of this tree. 
     * @param f the function to use for evaluation. 
     */
    public void evaluate(UtilityFunction f) {
        if (isLeaf() && !beenEvaluated) {  //if this node is a leaf and has not been evaluated. 
            if (f.isAdvanced()) //If advanced evaluation is to take place. 
                f.evaluate(parent.getState(), this);
            else
                f.evaluate(this);
        } else  //Else it is a parent. Evaluate children. 
            for (String key : children.keySet()) 
                children.get(key).evaluate(f);
    }
    
    /**
     * Returns the opposite token of the one that is currently contained. 
     * @return the opposite token of the one that is currently contained. 
     */
    public char getOppositeToken() {
        if (token == 'b')
            return 'w';
        else
            return 'b';
    }
    
    /**
     * Returns the current contained state. 
     * @return the current contained state. 
     */
    public char[][] getState() {
        return state;
    }
    
    /**
     * Returns true if the contained state is equal to the parameterized state. 
     * @param state the state to check for equality. 
     * @return true if the contained state is equal to the parameterized state. 
     */
    public boolean equals(char[][] state) {
        return GameState.equals(this.state, state);
    }
    
    /**
     * Returns the children of this node mapped to the arguments that resulted in their state. 
     * @return the children of this node mapped to the arguments that resulted in their state. 
     */
    public HashMap<String, GameTreeNode> getChildren() {
        return children;
    }
    
    /**
     * Returns the child of this node corresponding to the arguments to generate it. 
     * @param args the arguments used in generating the child node. 
     * @return the child of this node corresponding to the arguments to generate it. 
     */
    public GameTreeNode getChild(String args) {
        return children.get(args);
    }
    
    /**
     * Indicates if this node is a leaf node. 
     * @return
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }
    
    /**
     * Sets the score of this node. Marks the node as evaluated.  
     * @param score the new score. 
     */
    public void setScore(int score) {
        this.score = score; 
        beenEvaluated = true;
    }
    
    /**
     * Returns the parent node of this node. Returns NULL if no parent exists (aka head node).
     * - Mainly used by advanced evaluation function. 
     * @return the parent of the node. 
     */
    public GameTreeNode getParent() {
        return parent;
    }
    
    /**
     * Removes the children of this node who are mapped to the arguments. 
     * @param the list of children arguments to remove. 
     */
    public void removeChildren(List<String> args) {
        for (String s : args)
            children.remove(s);
    }
    
    /***********
     * Getters *
     ***********/
    
    public int getScore() {
        return score; 
    }

    public char getToken() {
        return token; 
    }
    
    public boolean isMaximizer() {
        return maximizer;
    }
    
    public boolean isEvaluated() {
        return beenEvaluated; 
    }
    
    public String getArgs() {
        return args; 
    }
}
