package edu.up.game;

import java.io.Serializable;
import java.util.*;

/**
 * GameConfig class
 * <P>
 * This class describes a user-specified configuration for playing a game. It
 * includes information about the number and type of players in the game and
 * other related meta-data.
 * 
 * A game initializes this class with some information and additional data is
 * provided by the user or loaded from a previously-saved configuration.
 * 
 * @author Andrew Nuxoll
 * @version July 2012
 * @see GameConfigActivity
 */
public class GameConfig implements Serializable {
	/**
	 * satisfy the Serializable interface
	 */
	private static final long serialVersionUID = 26072012L;

	/**
	 * used to identify an Intent from the GameConfig activity
	 */
	public static final int USER_CONFIG = 101;

	/**
	 * this string is used as unique ID for config data in Android intents
	 */
	public static final String GAME_CONFIG = "Game Configuration Data";

	/** a list of all valid player types that the user chooses */
	private GamePlayerType[] availTypes;

	/** a list of the names of each player */
	private Vector<String> selNames = new Vector<String>();

	/**
	 * a list of the type of each player. Each type in the list is associated
	 * with the player whose name is at the same index in selNames. Thus, this
	 * vector and selNames must always be the same length. This is managed
	 * automatically via the {@link #addPlayer} and {@link #removePlayer}
	 * methods.
	 */
	private Vector<GamePlayerType> selTypes = new Vector<GamePlayerType>();

	/**
	 * if set to true, indicates the game will be run on the local computer
	 * rather than connecting to a remote server
	 */
	private boolean isLocal;

	/**
	 * if the player is connecting to a game running on another device, this
	 * string is used to store the player's name
	 */
	private String remoteName;

	/**
	 * if the player is connecting to a game running on a another device, this
	 * string contains the IP address of that device
	 */
	private String ipAddr;

	/**
	 * this specifies the minimum number of players required for a legal game.
	 * For example, tic-tac-toe would have a minimum (and maximum, see below) of
	 * two players.
	 */
	private int minPlayers;

	/** this specifies the maximum number of players that the game can handle */
	private int maxPlayers;

	/**
	 * This is the name of the game. This value is used to identify the game
	 * both to the user and to the system (e.g., when saving configuration
	 * data).
	 */
	private String gameName;

	/**
	 * if this boolean is set to false, then this configuration can not be
	 * modified by the user and the configuration activity will be skipped
	 * (taking the players straight to the game). This can be useful when
	 * debugging.
	 */
	private boolean userModifiable;

	/**
	 * to create an instance of this class initial values for some instance
	 * variables must be supplied. The constructs makes a cursory effort to
	 * catch and fix bad input but, ultimately, the caller is responsible for
	 * creating a proper game definition. In particular, you must call addPlayer
	 * sufficient times to make sure that the minimum number of players is met.
	 */
	public GameConfig(GamePlayerType[] availTypes, int minPlayers,
			int maxPlayers, String gameName) {
		this.availTypes = availTypes;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.gameName = gameName;

		// default to a local game
		this.isLocal = true;

		// default remote data (if user goes to a remote game)
		this.ipAddr = "127.0.0.1";
		this.remoteName = "Guest";

		// by default, allow the user to modify the configuration
		this.userModifiable = true;
	}

	/**
	 * @return the availTypes
	 */
	public GamePlayerType[] getAvailTypes() {
		return availTypes;
	}

	/**
	 * addPlayer
	 * 
	 * adds a new player to the configuration
	 * 
	 * @param name
	 *            the player's name
	 * @param typeIndex
	 *            valid index of the player's type in availTypes
	 */
	public void addPlayer(String name, int typeIndex) {
		// catch and ignore illegal input
		if ((name == null) || (name.length() == 0))
			return;
		if ((typeIndex < 0) || (typeIndex >= availTypes.length))
			return;

		// don't go over the maximum
		if (selNames.size() >= this.maxPlayers)
			return;

		// append the new values
		selNames.add(name);
		selTypes.add(this.availTypes[typeIndex]);
	}// addPlayer

	/**
	 * removePlayer
	 * 
	 * removes a player from the configuration
	 * 
	 * @param index
	 *            of the player to remove
	 */
	public void removePlayer(int index) {
		// catch and ignore invalid index
		if ((index < 0) || (index >= selNames.size()))
			return;

		this.selNames.remove(index);
	}// removePlayer

	/**
	 * @return an array of the player names
	 */
	public String[] getSelNames() {
		return (String[]) this.selNames.toArray();
	}// getSelNames

	/**
	 * get the name of the player at a given index
	 * 
	 * @param index
	 *            of the player whose name is wanted
	 * @return the player's name or null if index is invalid
	 */
	public String getSelName(int index) {
		// catch and ignore invalid index
		if ((index < 0) || (index >= selNames.size()))
			return null;

		return this.selNames.elementAt(index);
	}// removePlayer

	/**
	 * @return an array of GamePlayerType objects that correspond to the player
	 *         names in this.selNames
	 */
	public GamePlayerType[] getSelTypes() {
		// fill an array with copies of the objects in this.selTypes
		GamePlayerType[] retVal = new GamePlayerType[selTypes.size()];
		int index = 0;
		for (GamePlayerType gpt : this.selTypes) {
			retVal[index] = (GamePlayerType) gpt.clone();
			++index;
		}

		return retVal;
	}// getSelTypes

	/**
	 * get the type of the player at a given index
	 * 
	 * @param index
	 *            of the player whose type is wanted
	 * @return the player's name or null if index is invalid
	 */
	public GamePlayerType getSelType(int index) {
		// catch and ignore invalid index
		if ((index < 0) || (index >= selNames.size()))
			return null;

		return this.selTypes.elementAt(index);
	}// removePlayer

	/**
	 * retrieves the GamePlayerType object for a specified player
	 * 
	 * @param index
	 *            the index of the player
	 * @return the GamePlayerType object or null if invalid index
	 */
	public GamePlayerType getType(int index) {
		// verify index
		if ((index < 0) || (index >= this.selTypes.size()))
			return null;

		return this.selTypes.elementAt(index);
	}

	/**
	 * @return the isLocal
	 */
	public boolean isLocal() {
		return isLocal;
	}

	/**
	 * @param isLocal
	 *            the isLocal to set
	 */
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	/**
	 * @return the remoteName
	 */
	public String getRemoteName() {
		return remoteName;
	}

	/**
	 * @param remoteName
	 *            the remoteName to set
	 */
	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}

	/**
	 * @return the ipAddr
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	/**
	 * @param ipAddr
	 *            the address to set
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	/**
	 * @return the minPlayers
	 */
	public int getMinPlayers() {
		return minPlayers;
	}

	/**
	 * @return the maxPlayers
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * @return the current number of players
	 */
	public int getNumPlayers() {
		return selNames.size();
	}

	/**
	 * @return the current number of player types
	 */
	public int getNumTypes() {
		return selTypes.size();
	}

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @return the userModifiable
	 */
	public boolean isUserModifiable() {
		return userModifiable;
	}

	/**
	 * @param userModifiable
	 *            the userModifiable to set
	 */
	public void setUserModifiable(boolean userModifiable) {
		this.userModifiable = userModifiable;
	}

}// class GameConfig

