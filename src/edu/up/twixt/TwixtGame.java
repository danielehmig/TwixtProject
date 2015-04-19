package edu.up.twixt;

import java.util.LinkedList;
import java.util.Queue;
import edu.up.game.GameAction;
import edu.up.game.GameConfig;
import edu.up.game.LocalGame;

/** 
This class defines the game state. The actions that are passed from the players
are implemented here. Also, the state of the game board is defined here.

	@author Daniel Ehmig
	@author Sherry Liao
	@author Josh McCleary
*/

public class TwixtGame extends LocalGame {
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 126363295329375498L;

	/**
     * defines the current state of all the pieces on the game board
     */

    protected static TwixtPiece[][] pieceMatrix = new TwixtPiece[TwixtGame.NUM_PEGS][TwixtGame.NUM_PEGS];
    
    /**
     * the number of pegs in a row of the game board; same for column 
		because the board is always square
     */
    public static final int NUM_PEGS = 18;
    
    //avoids null pointer exceptions for the first player
    protected static boolean boardInitialized = false;
    
    //used to determine if the game is over based on a surrender
    private static boolean surrGameOver = false;
    

    /**
     * This method initializes the local game state given the GameConfig.
     * 
		@param initConfig	The initial configuration of the game.
		@param initTurn	The player who goes first.
     */
	public TwixtGame(GameConfig config, int initTurn) {
		super(config, initTurn);
		
	}
	

	/**
     * This method is used primarily for network games to determine if the GUI is locked when it is not a player's turn.
		@param playerId	The player whose state is being returned.
		@return LocalGame	The state of the game.
     * @playerId
     */
	@Override
	public LocalGame getPlayerState(int playerIndex) {
		TwixtGame copy = new TwixtGame(this.getConfig(), this.whoseTurn);
		return copy;
	}
	
	/**
     * Returns the current state of all the pieces on the game board.
		@return The array of the pieces on the game board.
     */

    public static TwixtPiece[][] getBoardState() {
		return pieceMatrix;
    
    }
    

	/**
     * This method is called by the game framework to apply the current action passed by the player.
		@param action	The action that was passed by the player.
     */
	@Override
	public void applyAction(GameAction action) {
	
		if(action instanceof EndTurnAction){
			endTurn();
		}
		else if(action instanceof TwixtAction){
			if(this.whoseTurn == 3){
				this.whoseTurn = 1;
			}
			else if(this.whoseTurn == 2){
				this.whoseTurn = 0;
			}
			else{
				this.whoseTurn = this.whoseTurn + 2;
			}
			
			//want pieces to be set permanent on Twixt too
			for(int i = 0; i < pieceMatrix.length; ++i){
	    		for(int j = 0; j < pieceMatrix[i].length; ++j){
	    			if(pieceMatrix[i][j].getPlayerType() != TwixtPiece.EMPTY){
	    				pieceMatrix[i][j].setAsPermanent();
	    			}
	    		}
	    	}
		}
		
		
	}

	/**
	 * This method checks if a player has won, a player has surrendered, or a player has chosen to save and quit.
		@return Whether the game is over
	 */
	public boolean isGameOver() {

		boolean marked[][] = new boolean [NUM_PEGS][NUM_PEGS];

		Queue<TwixtPiece> vertex_queue = new LinkedList<TwixtPiece>();

		// set up matrix
		for(int i = 0; i < NUM_PEGS; ++i){
			for(int j = 0; j < NUM_PEGS; ++j){
				marked[i][j] = false;
			}
		}

		if(this.whoseTurn == 0 || this.whoseTurn == 2){
			for(int i = 0; i < NUM_PEGS; ++i){ // go through first row
				if(pieceMatrix[0][i] != null){
					vertex_queue.add(pieceMatrix[0][i]); // run each peg in isPath, if path found, return true
					marked[0][i] = true;
				}
			}
		}

		else{
			for(int i = 0; i < NUM_PEGS; ++i){ // go through first col
				if(pieceMatrix[i][0] != null){
					vertex_queue.add(pieceMatrix[i][0]); // run each peg in isPath, if path found, return true
					marked[i][0] = true;
				}
			} 
		}

		while(vertex_queue.peek() != null){

			for(int i = 0; i < vertex_queue.peek().getConnections().size(); ++i){
				if(this.whoseTurn == 0 || this.whoseTurn == 2){
					if(vertex_queue.peek().getConnections().elementAt(i).getRow() == NUM_PEGS-1){
						return true;
					}	
				}
				else{
					if(vertex_queue.peek().getConnections().elementAt(i).getCol() == NUM_PEGS-1){
						return true;
					}	
				}

				if(!marked[vertex_queue.peek().getConnections().elementAt(i).getRow()][vertex_queue.peek().getConnections().elementAt(i).getCol()])
				{
					marked[vertex_queue.peek().getConnections().elementAt(i).getRow()][vertex_queue.peek().getConnections().elementAt(i).getCol()] = true;
					vertex_queue.add(vertex_queue.peek().getConnections().elementAt(i));
				}
			}
			vertex_queue.remove();

		}
		return false;
	}

	/**
	 * This method returns the ID of the winner.
		@return The player who won.
	 */
	@Override
	public int getWinnerId() {
		
		int winner = 0;
		if(this.whoseTurn == 1 || this.whoseTurn == 3){
			winner = 1;
		}
		
		//clear the board at the end of a game
		TwixtGame.initializeBoard();
		surrGameOver = false;
		return winner;
	}

    /**
     * This method ends the current player's turn.
		@param playerId	The player whose turn is finished.
     */

    public void endTurn() {
    	if(this.getConfig().getNumPlayers() == 2){
    		this.whoseTurn = 1 - this.whoseTurn;
    	}
    	else if(this.getConfig().getNumPlayers() == 3){
    		if(this.whoseTurn == 2){
    			this.whoseTurn = 0;
    		}
    		else{
    			this.whoseTurn++;
    		}
    	}
    	else{
    		if(this.whoseTurn == 3){
    			this.whoseTurn = 0;
    		}
    		else{
    			this.whoseTurn++;
    		}
    	}
    	
    	for(int i = 0; i < pieceMatrix.length; ++i){
    		for(int j = 0; j < pieceMatrix[i].length; ++j){
    			if(pieceMatrix[i][j].getPlayerType() != TwixtPiece.EMPTY){
    				pieceMatrix[i][j].setAsPermanent();
    			}
    		}
    	}
    	
    }
    
    public void setGameOver(boolean value){
    	surrGameOver = value;
    }
    
    /**
     * This method initializes a game board filled with empty pegs for a player to use. 
     */
    public static void initializeBoard(){
    	
    	for(int i = 0; i < pieceMatrix.length; ++i){
    		for(int j = 0; j < pieceMatrix[i].length; ++j){
    			int x = ((TwixtHumanPlayer.BOARD_SIZE/TwixtGame.NUM_PEGS) * i)-20;
    			int y = ((TwixtHumanPlayer.BOARD_SIZE/TwixtGame.NUM_PEGS) * j)-20;
    			TwixtPiece nextPiece = new TwixtPiece(x, y, TwixtPiece.EMPTY);
    			nextPiece.setRow(i);
    			nextPiece.setCol(j);
    			pieceMatrix[i][j] = nextPiece;
    		}
    	}
    }
    
    

}// class TwixtGame
