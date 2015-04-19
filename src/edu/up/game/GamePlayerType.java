package edu.up.game;

import java.io.Serializable;

/**
 * class GamePlayerType
 * 
 * An instance of this class describes a single type of game player. Typical
 * player types include: "local human player", "remote human player",
 * "easy AI player" and "hard AI player".
 * 
 * @author Andrew Nuxoll
 * @version July 2012
 * @see GameConfig
 */

public class GamePlayerType implements Cloneable, Serializable {

	/** satisfy the Serializable interface */
	private static final long serialVersionUID = 26072012L;

	/**
	 * this is a short description of the player type used in GUI widgets
	 */
	public String typeName;

	/**
	 * if set to true, this indicates that this type of player will connect via
	 * the network
	 */
	public boolean isRemoteClient;

	/**
	 * this is the fully qualified name of the class that will provides moves
	 * from a player of this type. For example, for a local human player in a
	 * chess game you might specify the string "edu.up.chess.ChessHumanPlayer"
	 * 
	 * IMPORTANT: All player classes (AI, human or remote) must be a subclass of
	 * Activity and must implement the GamePlayer interface and must be
	 * registered in your AndroidManifest.xml file.
	 */
	public String playerClassName;

	/** ctor provided for convenience to initialize instance variables */
	public GamePlayerType(String typeName, boolean isRemoteClient,
			String playerClassName) {
		this.typeName = typeName;
		this.isRemoteClient = isRemoteClient;
		this.playerClassName = playerClassName;
	}

	/**
	 * by making this class implement the Cloneable interface, we allow copies
	 * of it to be made. Since GamePlayerType is such a simple class, the
	 * default, shallow copy functionality in java.lang.Object is sufficient.
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (Exception e) {
			return null; // failure!
		}
	}

}// class GamePlayerType
