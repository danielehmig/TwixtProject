package edu.up.game;

import java.io.Serializable;

/**
 * To support remote play, this game framework has two types of Games: local
 * games and remote games that are represented by a proxy. Both types adhere to
 * this common interface.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version November 2012
 * @see LocalGame
 * @see ProxyGame
 */

public interface Game extends Serializable {

	/**
	 * specifies whose turn it is
	 * 
	 * @return the index of the player whose turn it is. In a game with N
	 *         players, this must be a number in the range [0..N-1]
	 */
	public abstract int whoseTurn();

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
	 * applies a given action to the Game object.
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

	
}
