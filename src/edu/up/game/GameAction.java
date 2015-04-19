package edu.up.game;

import java.io.Serializable;

/**
 * An action for a generic game.  A game action is something that a player
 * tells the game that it wants to do (e.g., put an 'X' on the top-left
 * tic-tac-toe square).  The game will then decide whether the player is
 * allowed to perform that action before effecting the action on the
 * players behalf.  Most real games will subclass GameMoveAction (which
 * is a subclass of GameAction) to define actions that are relevant to the
 * particular game.  A GameAction contains the player as part of its state;
 * this way the game always knows what player sent it the action.
 * <P>
 * Several "generic" of GameAction classes are already defined.  These
 * include GameQuitAction, GameQuitAcknoledgeAction and GameMoveAction.
 *
 * @author Steven R. Vegdahl 
 * @version 2 July 2001
 */
public abstract class GameAction implements Serializable {
	/**
	 * satisfy the Serializable interface
	 */
	private static final long serialVersionUID = 15092012L;
	
	// the player who generated the request
    private int source;

    /**
     * constructor for GameAction
     *
     * @param source   the index of the player who created the action
     */
    public GameAction(int source) {
        this.source = source;
    }

    /**
     * tells the player who created the action
     *
     * @return the player who created the action
     *
     */
    public int getSource() {
        return source;
    }

}//class GameAction
