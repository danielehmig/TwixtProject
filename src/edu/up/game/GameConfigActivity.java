package edu.up.game;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.widget.TabHost.*;

public class GameConfigActivity extends Activity implements
		View.OnClickListener {

	/**
	 * this id is used to identify the Local Game tab
	 */
	public static final String LOCAL_TAB = "Local Game Tab";

	/**
	 * this id is used to identify the Remote Game tab
	 */
	public static final String REMOTE_TAB = "Remote Game Tab";

	/**
	 * contains the game configuration this activity will be used to initialize
	 */
	GameConfig config = null;

	// Each of these is initialized to point to various GUI controls
	TableLayout playerTable = null;
	Vector<TableRow> tableRows = new Vector<TableRow>();

	// The id of the current tab that is active
	String currTab = LOCAL_TAB;
	
	private static int playerCount;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_config_main);

		// Init the GUI with the GameConfig object from the given intent
		this.config = (GameConfig) getIntent().getSerializableExtra(
				GameConfig.GAME_CONFIG);
		initGUI();

	}// onCreate

	/**
	 * initializes the pages in the tabbed dialog
	 */
	protected void initTabs() {
		// Setup the tabbed dialog on the layout and add the content of each tab
		TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();
		TabSpec localTabSpec = tabHost.newTabSpec(LOCAL_TAB);
		localTabSpec.setContent(R.id.localGameTab);
		localTabSpec
				.setIndicator(getResources().getString(R.string.local_game));
		TabSpec remoteTabSpec = tabHost.newTabSpec(REMOTE_TAB);
		remoteTabSpec.setContent(R.id.remoteGameTab);
		remoteTabSpec.setIndicator(getResources().getString(
				R.string.remote_game));
		tabHost.addTab(localTabSpec);
		tabHost.addTab(remoteTabSpec);

		// listen for tab change events so I know whether it is a local or
		// remote game
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				GameConfigActivity.this.currTab = tabId;
			}
		});

	}// initTabs

	/**
	 * initialize the rows in the player table
	 */
	protected void initTableRows() {
		playerCount = 0;
		// put a row in the table for each player in the config
		this.playerTable = (TableLayout) findViewById(R.id.configTableLayout);
		for (int i = 0; i < config.getNumPlayers(); ++i) {

			// add the row
			TableRow row = addPlayer();

			// Set the player name
			TextView playerName = (TextView) row
					.findViewById(R.id.playerNameEditText);
			playerName.setText(config.getSelName(i));

			// Set the initial selection for the spinner
			GamePlayerType[] selTypes = config.getSelTypes();
			GamePlayerType[] availTypes = config.getAvailTypes();
			Spinner typeSpinner = (Spinner) row
					.findViewById(R.id.playerTypeSpinner);
			for (int j = 0; j < availTypes.length; ++j) {
				if (selTypes[i].typeName.equals(availTypes[j].typeName)) {
					typeSpinner.setSelection(j);
					break;
				}
			}
			

		}// for

	}// initTableRows

	/**
	 * places the data from this.config into the GUI.
	 * 
	 */
	protected void initGUI() {
		// do nothing without a game config
		if (this.config == null)
			return;

		// Set the title text using the game's name
		this.setTitle(config.getGameName() + " Game Configuration");

		// place the pages in the tabbed dialog
		initTabs();

		// Insert a row for each player in the current config
		initTableRows();

		// Set myself as the listener for the buttons
		View v = findViewById(R.id.addPlayerButton);
		v.setOnClickListener(this);
		v = findViewById(R.id.saveConfigButton);
		v.setOnClickListener(this);
		v = findViewById(R.id.playGameButton);
		v.setOnClickListener(this);

	}// initGUI

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_main, menu);
		return true;
	}

	/**
	 * this method is called whenever the user clicks on a button.
	 * 
	 * <p>
	 * NOTE: With the current layout it could either be a Button or ImageButton.
	 */
	public void onClick(View button) {
		// Add Player Button
		if (button.getId() == R.id.addPlayerButton) {
			addPlayer();
			if(this.tableRows.size() == 3){
				addPlayer();
				Toast.makeText(this, "No 3 Players for you!", Toast.LENGTH_SHORT).show();
			}
			this.playerTable.invalidate(); // show the user the change
		}

		// Delete Player Button
		else if (button.getId() == R.id.delPlayerButton) {
			// Search the existing players to find out which delete button got
			// clicked
			int size = this.tableRows.size();
			for (int i = 0; i < size; i++) {
				TableRow row = tableRows.elementAt(i);
				View v = row.findViewById(R.id.delPlayerButton);
				if (v == button) {
					// found it! remove from the layout and the list
					if(size == 4){
						removePlayer(this.tableRows.elementAt(size - 2));
						removePlayer(this.tableRows.elementAt(size - 3));
						break;
					}
					else{
						removePlayer(row);
					}

				}
			}


		}// else if (delete button)
		
		//Save Config Button
		else if (button.getId() == R.id.saveConfigButton)
		{
			messageBox("Sorry, this hasn't been implemented yet.");
		}
		
		//Start Game Button
		else if (button.getId() == R.id.playGameButton)
		{
			//Extract the data and return it to the main app
			GameConfig finalConfig = scrapeData();
			Intent intent = this.getIntent();
			intent.putExtra(GameConfig.GAME_CONFIG, (Serializable) finalConfig);
			setResult(RESULT_OK, intent);
			finish();
		}

	}// onClick

	/**
	 * removePlayer
	 * 
	 * removes the player in the table associated with a given TableRow object
	 * 
	 * <p>
	 * NOTE: this method will refuse to delete a row if the total would drop
	 * below the minimum allowed by the game configuration.
	 */
	private void removePlayer(TableRow row) {
		// first, make sure that we won't exceed the min number of players
		if (this.tableRows.size() <= config.getMinPlayers()) {
			messageBox("Sorry, removing a player would drop below the minimum allowed.");
			return;
		}

		this.playerTable.removeView(row);
		this.tableRows.remove(row);
		playerCount--;

	}// removePlayer

	/**
	 * addPlayer
	 * 
	 * adds a new, blank row to the player table and initializes instance
	 * variables and listeners appropriately
	 * 
	 * @return a reference to the TableRow object that was created or null on
	 *         failure
	 */
	private TableRow addPlayer() {
		// first, make sure that we won't exceed the max number of players
		if (this.tableRows.size() >= config.getMaxPlayers()) {
			messageBox("Sorry, adding another player would exceed the maximum allowed.");
			return null;
		}

		// add the row
		TableRow row = (TableRow) getLayoutInflater().inflate(
				R.layout.player_list_row, playerTable, false);

		// Initialize the values in the Spinner control
		GamePlayerType[] availTypes = config.getAvailTypes();
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (GamePlayerType gpt : availTypes) {
			adapter.add(gpt.typeName);
		}
		Spinner typeSpinner = (Spinner) row
				.findViewById(R.id.playerTypeSpinner);
		typeSpinner.setAdapter(adapter);
		

		// set myself up as the button listener for the button
		ImageButton delButton = (ImageButton) row
				.findViewById(R.id.delPlayerButton);
		delButton.setOnClickListener(this);
		
		if(playerCount == 0 || playerCount == 2){
			row.setBackgroundColor(Color.argb(150, 0, 0, 255));
		}
		else{
			row.setBackgroundColor(Color.argb(150, 255, 0, 0));
		}
		
		// add the row to the right lists and layout
		this.tableRows.add(row);
		playerTable.addView(row);
		playerCount++;

		return row;
	}// addPlayer

	/**
	 * scrapeData
	 * 
	 * retrieves all the data from the GUI and creates a new GameConfig object
	 * with it
	 */
	public GameConfig scrapeData() {
		// First make a copy of the original config without the players
		GameConfig result = new GameConfig(config.getAvailTypes(),
				config.getMinPlayers(), config.getMaxPlayers(),
				config.getGameName());

		// Set remote/local
		if (this.currTab.equals(REMOTE_TAB)) {
			result.setLocal(false);
		} else {
			result.setLocal(true);
		}

		// Retrieve the info for each player and add to the config
		for (TableRow row : this.tableRows) {
			//player name
			EditText nameEditor = (EditText) row
					.findViewById(R.id.playerNameEditText);
			String name = nameEditor.getText().toString();
			
			//If we have a null name, create a default
			if (name.equals(""))
				name = "Player";
			
			//index of player type
			Spinner typeSpinner = (Spinner) row
					.findViewById(R.id.playerTypeSpinner);
			int selIndex = typeSpinner.getSelectedItemPosition();
			
			//add to the config
			result.addPlayer(name, selIndex);
		}//for
		
		//Set the remote name
		EditText remoteNameEditText = (EditText)findViewById(R.id.remoteNameEditText);
		String remoteName = remoteNameEditText.getText().toString();
		result.setRemoteName(remoteName);

		//Set the IP address
		EditText ipAddrEditText = (EditText)findViewById(R.id.remoteIPAddrEditText);
		String ipAddr = ipAddrEditText.getText().toString();
		result.setIpAddr(ipAddr);

		return result;
	}
	

}// class GameConfigActivity