package edu.up.twixt;

import edu.up.game.GameAction;


/** 
   When a user selects to surrender, this action is created to indicate
	that the user wants to surrender and subsequently lose.
 */


public class SurrenderAction extends GameAction {



    /**
	 * 
	 */
	private static final long serialVersionUID = 1036768529860117866L;

	/**
     * Initializes the SurrenderAction object so the TwixtGame can apply the correct action.
		@param source	The player who selected to surrender.
     */

    public SurrenderAction (int source) {
    	super(source);
    }


}
