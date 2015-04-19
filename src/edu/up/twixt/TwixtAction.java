package edu.up.twixt;

import edu.up.game.GameAction;


/** 
   This class represents the GameAction associated with calling Twixt 
	in a four player game. Only one can be instantiated per team per game.
 */


public class TwixtAction extends GameAction {



    /**
	 * 
	 */
	private static final long serialVersionUID = 1958706028489701495L;

	/**
     * Initializes the TwixtAction object in order for TwixtGame to receive the correct action in applyAction
		@param source	The player who selected the action.
     */

    public TwixtAction (int source) {
    	super(source);
    }


}
