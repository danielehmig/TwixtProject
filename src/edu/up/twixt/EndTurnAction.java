package edu.up.twixt;

import edu.up.game.GameAction;

/** 
   This class defines the end turn action.
 */


public class EndTurnAction extends GameAction{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8578743291639692272L;

	/**
	 * Constructor initializes the action with the source.
	 * @param source	The player who initiated the action
	 */
    public EndTurnAction(int source) {
    	super(source);
    }


}
