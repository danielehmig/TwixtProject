package edu.up.twixt;

import edu.up.game.*;

/** 
Class that creates the GameConfig, LocalGame, ProxyPlayer(s), and ProxyGame.

	@author Sherry Liao
	@author Daniel Ehmig
	@author Josh McCleary
*/

public class TwixtMainActivity extends GameMainActivity {

	/**
     * Creates a GameConfig.

	@return	GameConfig 	new GameConfig created
     */
	@Override
	public GameConfig createDefaultConfig() {
		// Define the allowed player types
		GamePlayerType[] playerTypes = new GamePlayerType[3];
		playerTypes[0] = new GamePlayerType("Local Human Player", false,
				"edu.up.twixt.TwixtHumanPlayer");
		playerTypes[1] = new GamePlayerType("Random AI Player", false,
				"edu.up.twixt.TwixtEasyComputerPlayer");
		playerTypes[2] = new GamePlayerType("Smart AI Player", false,
				"edu.up.twixt.TwixtSmartComputerPlayer");

		// Create a game configuration class for Counter
		GameConfig defaultConfig = new GameConfig(playerTypes, 2, 4, "Twixt");

		// Add the default players
		defaultConfig.addPlayer("Human", 0);
		defaultConfig.addPlayer("Human 2", 0);
		
		
		return defaultConfig;
	}

	
	/**
     * Creates a LocalGame.

	@param config	GameConfig where LocalGame is to be created
	@return LocalGame	new LocalGame that was created
     */
	@Override
	public LocalGame createLocalGame(GameConfig config) {
		return new TwixtGame(config, 0);
	}

	/**
     * Creates a new ProxyPlayer.

		@return 	ProxyPlayer 	new ProxyPlayer created
     */
	@Override
	public ProxyPlayer createRemotePlayer() {
		
		return null;
	}

	/**
     * Creates a ProxyGame.

		@param 	hostName	name of ProxyGame
		@return ProxyGame	new ProxyGame created				
     */
	@Override
	public ProxyGame createRemoteGame(String hostName) {
		
		return null;
	}
	
}
