package edu.up.game;

import java.io.Serializable;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * class GameHumanPlayer
 * 
 * is an abstract base class for a player that is controlled by a human. For any
 * particular game, a subclass should be created that can display the current
 * game state and responds to user commands.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew Nuxoll
 * @date September 2012
 * 
 */
public abstract class GameHumanPlayer extends Activity implements GamePlayer {
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
	 * initializeGUI
	 * 
	 * this method should be overridden by the subclass to inflate the GUI and
	 * also setup any listener methods. You will probably want to call your
	 * updateDisplay method from this one as well. Anything you would normally
	 * do in OnCreate you should do in this method instead.
	 */
	protected abstract void initializeGUI();

	/**
	 * updateDisplay
	 * 
	 * is called whenever the current game state {@link #game} has changed and,
	 * therefore, the user interface needs to be updated. The subclass should
	 * override this method to modify the controls on the current display and
	 * call invalidate() on them as necessary.
	 */
	protected abstract void updateDisplay();

	/*
	 * ====================================================================
	 * Public Methods
	 * --------------------------------------------------------------------
	 */

	/**
	 * onCreate
	 * 
	 * extracts the game state and sets up the game to display its GUI. This
	 * method has been set to final so that a particular game can not override
	 * it. Any initialization you want to do should be done in the initialize
	 * method instead.
	 * 
	 */
	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = this.getActionBar();
		bar.hide();

		// Extract the GameState object from the given intent (if present)
		LocalGame state = (LocalGame) getIntent().getSerializableExtra(
				GAME_STATE);
		if (state != null) {
			this.game = state;
		}

		// show the GUI to the user
		this.setTitle(myName());
		initializeGUI();

	}// onCreate

	/*
	 * ====================================================================
	 * Other Methods
	 * --------------------------------------------------------------------
	 */
	
	/**
	 * takeAction
	 * 
	 * returns the player's action to the game and ends this activity until the
	 * player's next turn. The subclass is responsible for calling this method
	 * when the user has finished using the GUI to select her next turn.
	 */
	protected void takeAction(GameAction action) {
		// Update the display first so the user sees the game result
		//this.game.applyAction(action);
		//updateDisplay();

		// Return the result
		Intent intent = this.getIntent();
		intent.putExtra(GamePlayer.GAME_ACTION, (Serializable) action);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}// takeAction

	/**
	 * messageBox
	 * 
	 * is a handy method for putting a message box on the screen.
	 * 
	 * <p>
	 * CAVEAT: this dialog does not wait for user input, so if there is a switch
	 * to another activity after displaying it, the user will not see the
	 * message.
	 * 
	 * @param msg
	 *            the message to post
	 */
	protected void messageBox(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg);
		builder.setPositiveButton("OK", null);
		AlertDialog alert = builder.create();
		alert.show();
	}// messageBox

	/**
	 * @return this player's name according to the game configuration or null if
	 *         it can not be determined
	 */
	protected String myName() {
		// Verify we have a valid game state
		if (this.game == null)
			return null;

		// Extract the game configuration
		GameConfig config = this.game.getConfig();
		if (config == null)
			return null;

		// Extract the name
		int currPlayerIndex = this.game.whoseTurn();
		return config.getSelName(currPlayerIndex);
	}// myName

}// class GameHumanPlayer
