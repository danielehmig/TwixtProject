package edu.up.twixt;

import java.io.Serializable;
import java.util.Vector;



/** 
 * Class that defines a peg. Pegs are placed by the users in order to build a path between the two ends of their home rows.
 * The position of each peg is defined by the instance variables row and col, which are used as indices in TwixtPiece[][] (game board). 
 * The pegs that a peg is connected to is stored in the vector connections. Initially, a peg is initialized as an EMPTY peg and isHighlighted is set to false. 
 * As the game progress, the peg's playerType will be changed to either LIGHT_PEG (red team) or DARK_PEG (blue team). 
 * The instance variable isHighlighted is changed to true if the user decides to place a bridge, and the peg is a valid peg to connect (a knights move away).
 *
 * @author Daniel Ehmig
 * @author Josh McCleary
 * @author Sherry Liao
 * 
 */

public class TwixtPiece implements Serializable {
	
	/**
	 * Serial Identification
	 */
	private static final long serialVersionUID = -2795273471792432950L;
	
	// The type of player associated with this peg (nobody, red team, blue team)
	private int playerType; 
	
	// The row that the peg is contained in
    private int row;
    
    // The row that the peg is contained in
    private int col; 
    
    // A vector of pegs that are connected with this peg
    private Vector<TwixtPiece> connections = new Vector<TwixtPiece>(); 
    
    // Boolean that states whether or not a peg is
    // highlighted. A peg is highlighted for use with making bridges.
    private boolean isHighlighted = false; 
    
    // Boolean that defines whether the piece can be modified.
	// If isSet is false, it can be modified, else, it can not.
    private boolean isSet = false; 
    
    // Constant representing a peg that is not in use
    public static final int EMPTY = -1; 
    
    // Constant that says that a peg is associated with the red team
    public static final int LIGHT_PEG = 0; 
    
    // Constant that says that a peg is associated with the blue team
    public static final int DARK_PEG = 1; 
    
    private int x; // x coordinate of the twixt piece
    private int y; // y coordinate of the twixt piece
    
    //used to determine row and col based on x,y coords
    private int[] range = new int[TwixtHumanPlayer.BOARD_SIZE];
    
    private int boardSize;
    private int centerX;
    private int centerY;
    private boolean connChecked = false;
    
    //number of connections that is used in isGameOver
    protected int tempNumConnections;
    
    //the actual number of connections, never decrements
    private int numConnections;

    /**
     * Constructor
	 * Initializes row, col, sets peg type to EMPTY, sets isHighlighted to false.
	 * 
	 * @param	xCoord   Row that peg is in
	 * @param 	yCoord   Column that peg is in
     */
    public TwixtPiece (int xCoord, int yCoord, int n_playerType) {
    	this.x = xCoord;
    	this.y = yCoord;
    	boardSize = TwixtHumanPlayer.BOARD_SIZE;
    	
    	for(int i = 0; i < range.length; ++i){
    		range[i] = i * (boardSize/TwixtGame.NUM_PEGS);
    	}
    	
    	
    	this.row = determinePosition(yCoord);
    	this.col = determinePosition(xCoord);
    	playerType = n_playerType;
    	
    	centerX = 30 + (col * 40);
    	centerY = 30 + (row * 40);
    }
    

    /**
     * Returns the connections vector
	 *
	 * @return Vector<int[]>   the vector that contains the pegs that this peg is connected with
     */
    public Vector<TwixtPiece> getConnections() {
		return connections;
    }

    /**
     * Returns the type of peg this peg is (EMPTY, LIGHT_PEG, DARK_PEG)
	 *
	 * @return	int		Returns playerType
     */
    public int getPlayerType() {
		return playerType;
    }

    /**
     * Returns the value of isHighlighted
	 *
	 * @param 		piece		TwixtPiece that is examined
	 * @return  	boolean  	Returns true if peg is highlighted, false if it is not
     */
    public boolean getIsHighlighted() {
		return isHighlighted;
    }

    /**
     * Returns the row that the peg is in.
	 *
	 * @return 	int		The row that the peg is in
     */
    public int getRow() {
		return row;
    }


    /**
     * Returns the column that the peg is in.
	 *
	 * @return 	int		The column that the peg is in
     */
    public int getCol() {
		return col;
    }


    /**
     * Adds a peg to the connection vector if a new connection with this peg has been made with another.
	 *
	 * @param 	 connection		An array that contains information about the position of the peg connected with this peg	
     */

    public void addConnection(TwixtPiece piece) {
    	connections.add(piece);
    	this.numConnections++;
    	this.tempNumConnections++;
    }


    /**
     * Sets the playerType of the TwixtPiece. 
	 *
	 * @param 	type 	Integer that represents the player type (EMPTY, LIGHT_PEG, DARK_PEG)
     */
    public void setPlayerType(int type) {
    	playerType = type;
    }


    /**
     * Sets the isSet boolean to true.
     */
    public void setAsPermanent( ) {
    	isSet = true;
    }


    /**
     * Returns value of isSet.
     */
    public boolean isThisSet() {
		return isSet;
    }

    /**
     * Sets isHighlighted to true. Used to show the user which pieces are legal pieces to make a bridge with.
     */
    public void setIsHighlighted(boolean b) {
    	isHighlighted = b;
    }


    /**
     * This method returns the row/col for the given coordinate
     * @param coordinate	The coordinate (x or y) to check
     * @return	The row or col that the coordinate falls in
     */
    public int determinePosition(int coordinate){
    	
    	for(int i = 0; i < range.length; ++i){
    		if(coordinate <= range[i]){
    			return i-1;
    		}
    	}
    	return 0;
    }
    
    /**
     * Methods that describe themselves.
     */
    public int getX(){
    	return this.x;
    }
    
    public int getY(){
    	return this.y;
    }
    
    public int getCenterX(){
    	return this.centerX;
    }
    
    public int getCenterY(){
    	return this.centerY;
    }
    
    public void setChecked(boolean checked){
    	connChecked = checked;
    }
    
    public boolean isChecked(){
    	return this.connChecked;
    }
    
    public void resetTempConnections(){
    	tempNumConnections = numConnections;
    }
    
    public void setRow(int row){
    	this.row = row;
    }
    
    public void setCol(int col){
    	this.col = col;
    }
}
