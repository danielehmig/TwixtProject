//package edu.up.game;
//
///**
// * An instance of this class represents a game running locally (as opposed to a
// * game running remotely on a server).  
// * 
// * @author Steven R. Vegdahl
// * @author Andrew Nuxoll
// * @version September 2012
// */
//public abstract class GameImpl implements Game {
//
//	/**
//	 * the game configuration that was used to create this game
//	 */
//	protected GameConfig gameConfig;
//	
//	/** 
//	 * the current, complete state of the game
//	 */
//	protected GameState gameState = null;
//	
//	/**
//	 * ctor initializes gameConfig
//	 * 
//	 * @param config   the game configuration for this game
//	 */
//	public GameImpl(GameConfig config) {
//		this.gameConfig = config;
//	}
//
//	/**
//	 * @return the game state should know whose turn it is
//	 */
//	public int whoseTurn() {
//		if 
//		return this.whoseTurn;
//	}
//
//	/**
//	 * getPlayerState
//	 * 
//	 * gets information about the state of the game from the perspective of a
//	 * given player. In many cases, the player is not privy to the entire state
//	 * of the game. For example, you can't see your opponent's cards when you
//	 * play Go Fish. This method creates a copy of 'this' that contains only the
//	 * data visible to a given player.
//	 * 
//	 * @param playerIndex
//	 *            the index of the player asking the question. In a game with N
//	 *            players, this must be a number in the range [0..N-1]
//	 * @return an object that contains the information that the caller requested
//	 */
//	public abstract GameState getPlayerState(int playerIndex);
//
//	/**
//	 * applies a given action to the GameState object making modifications to
//	 * the state as necessary.
//	 * 
//	 * @param the
//	 *            player requesting the action
//	 */
//	public abstract void applyAction(GameAction action);
//
//	/**
//	 * Tells whether the game is over. Each specific game should be able to tell
//	 * whether the game is over. The algorithm for determining such should be
//	 * put here.
//	 * 
//	 * @return boolean value that tells whether the game is over
//	 */
//	public abstract boolean isGameOver();
//
//	/**
//	 * @return the id of the player who has won the game. If the game is not
//	 *         over yet, the output of this method is undefined.
//	 */
//	public abstract int getWinnerId();
//
//}
