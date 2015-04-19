package edu.up.game;

import java.io.Serializable;

import edu.up.twixt.QuitAction;
import edu.up.twixt.TwixtGame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * class GameMainActivity
 * 
 * is the main activity for the game framework. To create a new game, create a
 * sub-class of this class that implements its abstract methods below.
 * 
 * @author Andrew M. Nuxoll
 * @author Alex Hanemann
 * @date November 2012
 */
public abstract class GameMainActivity extends Activity implements OnTouchListener {

	/*
	 * ====================================================================
	 * Static Variables
	 * --------------------------------------------------------------------
	 */
	public static final String CONFIG = "config";
	public static final String GAME = "game";
	public static final String TEXT = "text";
	
	/*
	 * ====================================================================
	 * Instance Variables
	 * --------------------------------------------------------------------
	 */
	// stores the game configuration for the current game. This variable
	// is initialized in onCreate
	private GameConfig myConfig = null;

	// A reference to the object representing the game itself. This is the
	// object that knows the rules of the game. This variable is initialized in
	// launchGame.
	private Game game = null;

	// This is a reference to the TextView that is visible between player turns
	private TextView hubTextView = null;
	
	// This is the layout the TextView is displayed on
	private RelativeLayout hubLayout = null;

	/*
	 * ====================================================================
	 * Abstract Methods
	 * 
	 * To create a game using the game framework you must create a subclass of
	 * GameMainActivity that implements the following methods.
	 * --------------------------------------------------------------------
	 */
	/**
	 * Creates a default, game-specific configuration for the current game.
	 * 
	 * IMPORTANT: The default configuration must be a legal configuration!
	 * 
	 * @return an instance of the GameConfig class that defines a default
	 *         configuration for this game. (The default may be subsequently
	 *         modified by the user if this is allowed.)
	 */
	public abstract GameConfig createDefaultConfig();

	/**
	 * createLocalGame
	 * 
	 * Creates a new game with the specified number of players. For example, if
	 * you were creating tic-tac-toe, you would implement this method to return
	 * an instance of your TicTacToeGameImpl class which, in turn, would be a
	 * subclass of {@link GameImpl}.
	 * 
	 * @param config
	 *            The final configuration for this game.
	 * @return a new, game-specific instance of a sub-class of the LocalGame
	 *         class.
	 */
	public abstract LocalGame createLocalGame(GameConfig config);

	/**
	 * Creates a "proxy" player that acts as an intermediary between a local
	 * game and a player somewhere else on the internet.
	 * 
	 * @return the ProxyPlayer object that was created
	 */
	public abstract ProxyPlayer createRemotePlayer();

	/**
	 * Creates a "proxy" game that acts as an intermediary between a local
	 * player and a game that is somewhere else on the net.
	 * 
	 * @param hostName
	 *            the name of the machine where the game resides. (e.g.,
	 *            "upibmg.egr.up.edu")
	 * @return the ProxyGame object that was created
	 */
	public abstract ProxyGame createRemoteGame(String hostName);

	/*
	 * ====================================================================
	 * Public Methods
	 * --------------------------------------------------------------------
	 */
	/**
	 * onCreate
	 * 
	 * "main" for the game framework
	 */
	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize the layout
		setContentView(R.layout.game_hub_layout);
		this.hubTextView = (TextView) this.findViewById(R.id.hubTextView);
		this.hubLayout = (RelativeLayout) this.findViewById(R.id.hubLayout);
		hubLayout.setOnTouchListener(this);
		this.hubTextView.setTextSize(36);

		if (savedInstanceState != null) {
			// Restore instance state
			game = (Game) savedInstanceState.get(GAME);
			myConfig = (GameConfig) savedInstanceState.get(CONFIG);
			hubTextView.setText((CharSequence) savedInstanceState.get(TEXT));
		} else {
			createGameConfiguration();
		}

	}// onCreate

	/**
	 * Saves game and configuration information
	 */
	@Override 
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(CONFIG, myConfig);
		outState.putSerializable(GAME, game);
		outState.putString(TEXT, (String) hubTextView.getText());
	}


	/**
	 * If the game is over, returns to the config screen to start a new one
	 */
	public boolean onTouch(View view, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			hubTextView.setText(R.string.start_the_game);
			createGameConfiguration();
		}
		return true;
	}
	
	/**
	 * Whenever an activity returns a result to this main activity, it is
	 * handled here. Currently, the following intent types are possible:
	 * <ul>
	 * <li>GameConfig.USER_CONFIG - the user has finished configuring an new
	 * game
	 * <li>GamePlayer.YOUR_TURN - a player returns its action for its turn
	 * </ul>
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_CANCELED) {
			// The back button was pressed, or something else that canceled the activity. Restart.
			createGameConfiguration();
			TwixtGame.initializeBoard();
			return;
		}
		
		if (requestCode == GamePlayer.YOUR_TURN) {
			// Retrieve the action
			GameAction action = (GameAction) data
					.getSerializableExtra(GamePlayer.GAME_ACTION);
			if (action == null) {
				System.err
						.println("ERROR:  selected action missing from player's reply.  Exiting...");
				System.exit(-1);
			}
			
			if(action instanceof QuitAction){
				System.exit(0);
			}
			// Modify the game state using the action
			game.applyAction(action);

			// On to the next player's turn
			nextTurn();
		}// if
		else if (requestCode == GameConfig.USER_CONFIG) {
			// Retrieve the action
			GameConfig config = (GameConfig) data
					.getSerializableExtra(GameConfig.GAME_CONFIG);
			if (config == null) {
				System.err
						.println("ERROR:  Game configuration not returned from game config activity.  Exiting...");
				System.exit(-1);
			}

			// Set the configuration and launch the game
			this.myConfig = config;
			launchGame(config);
		}// elseif
		else
		{
			//Dunno what happened.  Probably the user hit the Back button
			//Try re-asking the player activity for its move
			nextTurn();
		}

	}// onActivityResult

	/*
	 * ====================================================================
	 * Helper Methods
	 * --------------------------------------------------------------------
	 */

	/**
	 * createGameConfiguration
	 * 
	 * this method launches a GameConfigActivity to interact with the user and
	 * initialize a GameConfig object that defines the number and type of
	 * players for this game.
	 */
	protected void createGameConfiguration() {

		// Start with the default configuration
		GameConfig myConfig = createDefaultConfig();

		// launch the game config activity to finish initializing the
		// configuration with user input (if allowed)
		if (myConfig.isUserModifiable()) {
			Intent configIntent = new Intent(this, GameConfigActivity.class);
			configIntent.putExtra(GameConfig.GAME_CONFIG,
					(Serializable) myConfig);
			startActivityForResult(configIntent, GameConfig.USER_CONFIG);
		}
		else
		{
			this.myConfig = myConfig;
			launchGame(myConfig);
		}

	}// createGameConfiguration

	/**
	 * Creates the game and players, and starts the game.
	 * 
	 * @param config
	 *            is the configuration for this game
	 */
	private final void launchGame(GameConfig config) {
		
		// Set the title text with the game's name
		this.setTitle(config.getGameName());

		// create the game
		if (config.isLocal()) { // local game
			game = createLocalGame(config);

			// verify we have a game
			if (game == null) {
				System.err.println("Game creation failed.  Exiting...");
				System.exit(-1);
			}
		} else { // remote game
			ProxyGame pg = createRemoteGame(config.getIpAddr());

			// if the game-creation operation failed, give error message
			// and exit
			if (pg == null || !pg.isComplete()) {
				System.err
						.println("Could not make connection to server.  Exiting...");
				System.exit(-1);
			} else {
				game = pg;
			}
		}// else (remote game)

		// begin by asking the first player to make the first move
		nextTurn();

	}// playGame

	/**
	 * nextTurn
	 * 
	 * determines whose turn it is and requests a move from that player. This
	 * method is called repeatedly as the game is played.
	 */
	public void nextTurn() {

		// Get the id and state for the current player
		int playerIndex = game.whoseTurn();
		LocalGame state = game.getPlayerState(playerIndex);

		// If the game is over notify the user and stop
		if (state.isGameOver()) {
			notifyGameOver();
			return;
		}// if

		// update the hub textview to reflect whose turn it is
		this.hubTextView.setText(myConfig.getSelName(game.whoseTurn())
				+ "'s turn.");

		// %%%TODO: check to see if I've already asked this player for a move

		// Launch the player's activity to get a move back
		GamePlayerType gpt = myConfig.getType(playerIndex);
		Intent intent = null;
		try {
			intent = new Intent(this, Class.forName(gpt.playerClassName));
		} catch (ClassNotFoundException e) {
			messageBox("ERROR launching player activity: "
					+ gpt.playerClassName + ".  Class not found!  Exiting...");
			e.printStackTrace();
		}
		intent.putExtra(GamePlayer.GAME_STATE, (Serializable) state);
		startActivityForResult(intent, GamePlayer.YOUR_TURN);

		// %%%TODO: start a timer so request can timeout?

	}// nextTurn

	/**
	 * tells the user the game is over and allows the user to start a new game
	 */
	private void notifyGameOver() {
		// Extract the winner's id
		if (this.game == null)
			return;
		int winner = this.game.getWinnerId();

		// Tell the user who won
		String winnerName;
		
		if(winner == 0){
			winnerName = "Red Team";
			this.hubTextView.setBackgroundColor(Color.rgb(220, 20, 60));
		}
		else{
			winnerName = "Blue Team";
			this.hubTextView.setBackgroundColor(Color.rgb(106, 90, 205));
		}
		
		this.hubTextView.setText("Game Over.\n" + winnerName
				+ " is the winner. Touch anywhere to start a new game!");
	}// notifyGameOver

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

}// class GameMainActivity
