package edu.up.game;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * An abstract computerized game player player. This is an abstract class, that
 * should be sub-classed to implement different AIs. The subclass must implement
 * the {@link #calculateMove} method.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew Nuxoll
 * @version September 2012
 */
public abstract class GameComputerPlayer extends Activity implements GamePlayer {
	/**
	 * the current game state
	 */
	protected LocalGame game;

	/*
	 * ====================================================================
	 * Abstract Methods
	 * 
	 * Create the game specific functionality for this human player by
	 * sub-classing this class and implementing the following methods.
	 * --------------------------------------------------------------------
	 */

	/**
	 * calculateMove
	 * 
	 * calculates what action the agent will take given the current
	 * {@link #game} state
	 * 
	 * @return a complete, initialized action defining the agent's move
	 */
	protected abstract GameAction calculateMove();

	/*
	 * ====================================================================
	 * Public Methods
	 * --------------------------------------------------------------------
	 */

	/**
	 * onCreate
	 * 
	 * when this activity is created, it extracts the current game state and
	 * updates the 'game' instance variable
	 */
	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Extract the GameState object from the given intent (if present)
		LocalGame state = (LocalGame) getIntent().getSerializableExtra(
				GAME_STATE);
		if (state != null) {
			this.game = state;
		}

		// If the game is over, just exit
		if (state.isGameOver()) {
			System.exit(0);
		}

		// Calculate what move to make
		GameAction action = calculateMove();

		// Return this resulting action to the caller
		Intent intent = this.getIntent();
		intent.putExtra(GamePlayer.GAME_ACTION, (Serializable) action);
		setResult(RESULT_OK, intent);
		finish();

	}// onCreate

}// class GameComputerPlayer
