package edu.up.twixt;

import edu.up.game.GameAction;


/** 
   When the user selects this action from the pause menu, the current state 
	of the game saves and returns to the main menu.
 */


public class QuitAction extends GameAction {



    /**
	 * 
	 */
	private static final long serialVersionUID = -2676773724685901476L;

	/**
     * Initializes the QuitAction object
		@param source	The player who selected to save and quit.
     */

    public QuitAction (int source) {
    	super(source);
    }


}
