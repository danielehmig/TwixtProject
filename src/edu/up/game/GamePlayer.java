package edu.up.game;

/**
 * A player who plays a (generic) game. Each class that implements a player for
 * a particular game should implement this interface.
 * 
 * This interface has no methods but does provide constants for tagging data
 * passed between the player and the game engine via Intents. Any class the
 * implements this interface should be an Activity that expects a GameState via
 * the Intent and returns a GameAction. The abstract classes
 * {@link GameComputerPlayer} and {@link GameHumanPlayer} provide a framework
 * for this.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew Nuxoll
 * @version September 2012
 */

public interface GamePlayer {
	/**
	 * This id is used to identify an Android Intent to ask a player to take a
	 * turn
	 */
	public static final int YOUR_TURN = 1;

	/**
	 * This is is used to tag the current game state when it is attached an
	 * Android Intent
	 */
	public static final String GAME_STATE = "GAME_STATE";

	/**
	 * This is used to tag a GameAction when it is attached an Android Intent
	 */
	public static final String GAME_ACTION = "GAME_ACTION";

}// interface GamePlayer
