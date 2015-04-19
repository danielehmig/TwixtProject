package edu.up.game;

import java.io.Serializable;

/**
 * A class that knows how to play the game. The data in this class represent the
 * state of a game. The state represented by an instance of this class can be a
 * complete state (as might be used by the main game activity) or a partial
 * state as it would be seen from the perspective of an individual player.
 * 
 * Each game has a unique state definition, so this abstract base class has
 * little inherent functionality.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew Nuxoll
 * @version September 2012
 */
public abstract class LocalGame implements Game, Serializable {
	/**
	 * satisfy the Serializable interface
	 */
	private static final long serialVersionUID = 16092012L;

	/**
	 * the index of the player whose turn it currently is according to this
	 * state. The subclass is responsible for initializing and maintaining this
	 * variable
	 */
	protected int whoseTurn = 0; // it's assumed player 0 goes first, override
									// as necessary

	/**
	 * the game state contains a copy of the current game configuration
	 */
	private GameConfig myConfig;

	/**
	 * ctor initializes instance variables
	 */
	public LocalGame(GameConfig initConfig, int turn) {
		this.myConfig = initConfig;
		this.whoseTurn = turn;
	}

	/**
	 * @return the value of whoseTurn
	 */
	public int whoseTurn() {
		return this.whoseTurn;
	}

	/**
	 * @return the game configuration for this game
	 */
	public GameConfig getConfig() {
		return this.myConfig;
	}

	/*
	 * ====================================================================
	 * Abstract Methods
	 * 
	 * Create the game specific functionality for this game by sub-classing this
	 * class and implementing the following methods.
	 * --------------------------------------------------------------------
	 */

	/**
	 * getPlayerState
	 * 
	 * gets information about the state of the game from the perspective of a
	 * given player. In many cases, the player is not privy to the entire state
	 * of the game. For example, you can't see your opponent's cards when you
	 * play Go Fish. This method creates a copy of 'this' that contains only the
	 * data visible to a given player.
	 * 
	 * @param playerIndex
	 *            the index of the player asking the question. In a game with N
	 *            players, this must be a number in the range [0..N-1]
	 * @return an object that contains the information that the caller requested
	 */
	public abstract LocalGame getPlayerState(int playerIndex);

	/**
	 * applies a given action to the GameState object making modifications to
	 * the state as necessary.  
	 * 
	 * @param the
	 *            player requesting the action
	 */
	public abstract void applyAction(GameAction action);

	/**
	 * Tells whether the game is over. Each specific game should be able to tell
	 * whether the game is over. The algorithm for determining such should be
	 * put here.
	 * 
	 * @return boolean value that tells whether the game is over
	 */
	public abstract boolean isGameOver();

	/**
	 * @return the id of the player who has won the game. If the game is not
	 *         over yet, the output of this method is undefined.
	 */
	public abstract int getWinnerId();

}// class LocalGame
