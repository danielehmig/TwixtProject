package edu.up.twixt;


import java.io.Serializable;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.TextView;
import edu.up.game.GameHumanPlayer;
import edu.up.game.R;

/** 
This class defines the human player of the Twixt Game. It holds
most of the GUI information, including buttons and text views.

@author Daniel Ehmig
@author Josh McCleary
@author Sherry Liao
*/

public class TwixtHumanPlayer extends GameHumanPlayer implements
		View.OnClickListener, Serializable, AnimatorUpdateListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1929787316365365235L;

	/**
     * set to true when the player is in "add piece mode"
     */

    protected boolean addingPiece = true;

    /**
     * set to true when the player is "place bridge" mode
     */

    protected boolean connectingPiece;

    /**
     * defines the surface view on the player's GUI which is the game board
     */

    private BoardSurfaceView board;

    /**
     * GUI button pressed to put the user in "place bridge" mode
     */

    protected Button linkButton;

    /**
     * opens a contextual menu where the user can view rules, surrender, or save and quit
     */

    protected Button pauseButton;

    /**
     * user selects this button to end his or her turn
     */

    protected Button endTurn;

    /**
     * text view that displays whose turn it is
	changes color to the current player's color
     */

    protected TextView turnTextView;

    /**
     * Reference to "Place Peg" button on the GUI.
     */

    protected Button placePegButton;

    /**
     * Represents the button on the player's GUI for calling Twixt during a 4-player game.
     */

    protected Button twixtButton;
    
    /**
     * says whether the first piece was selected when bridging two pieces
     */
    
    protected boolean firstPieceSelected = false;
    
    
    /**
     * the width/height of the board
     */
    public static int BOARD_SIZE = 750;
    
    //first team called twixt or not
    private static boolean twixtCalledOne = false;
    
    //second team called twixt or not
    private static boolean twixtCalledTwo = false;
    
    //cycle through alpha values for the current player text view
    //in order to achieve a flashing effect
    private ValueAnimator animator = ValueAnimator.ofInt(255, 0);
    
    //after the peg and bridge buttons are unselected, we want
    //them to look like regular buttons again
    private Drawable originalPegState = null;
    
    private Drawable originalLinkState = null;
    
    

    /**
     * initializes the graphical user interface, sets the buttons to their counterparts in the XML, 
     * and tells the surface view to draw the board
     */
	@Override
	protected void initializeGUI() {
		
		setContentView(R.layout.human_player);
		
		
		animator.setDuration(1000);
		animator.addUpdateListener(this);
		animator.setRepeatCount(5);
		animator.setRepeatMode(ValueAnimator.REVERSE);
		((TwixtGame)game).setGameOver(false);
		
		//initializes all of the gui elements
    	linkButton = (Button)this.findViewById(R.id.placeBridge);
    	pauseButton = (Button)this.findViewById(R.id.pause);
    	endTurn = (Button)this.findViewById(R.id.endTurn);
    	turnTextView = (TextView)this.findViewById(R.id.whoseTurn);
    	placePegButton = (Button)this.findViewById(R.id.placePeg);
    	twixtButton = (Button)this.findViewById(R.id.twixt);
    	placePegButton.setEnabled(true);
    	originalPegState = placePegButton.getBackground();
    	originalLinkState = linkButton.getBackground();
    	placePegButton.setBackgroundColor(Color.YELLOW);
    	
    	board = (BoardSurfaceView)this.findViewById(R.id.boardView);
    	board.setOnTouchListener(board);
    	board.receivePlayerRef(this);
    	   	
    	if(!TwixtGame.boardInitialized){
    		TwixtGame.boardInitialized = true;
    		TwixtGame.initializeBoard();
    	}
    	
    	//Twixt button only enabled if num players is 4
    	if(this.game.getConfig().getNumPlayers() == 4){
    		twixtButton.setEnabled(true);
    	}
    	else{
    		twixtButton.setEnabled(false);
    	}
    	
    	//each team cannot call twixt more than once
    	if(this.game.whoseTurn() == 0 || this.game.whoseTurn() == 2){
    		if(twixtCalledOne){
    			twixtButton.setEnabled(false);
    		}
    	}
    	else{
    		if(twixtCalledTwo){
    			twixtButton.setEnabled(false);
    		}
    	}
    	
    	linkButton.setOnClickListener(this);
    	pauseButton.setOnClickListener(this);
    	twixtButton.setOnClickListener(this);
    	placePegButton.setOnClickListener(this);
    	endTurn.setOnClickListener(this);
    	
    	//pause button opens a contextual menu
    	this.registerForContextMenu(pauseButton);
    	
		updateDisplay();
	}//initializeGUI

	/**
     * This method sets the text view to display the current player. It then updates the surface view.
     */
	
	@Override
	protected void updateDisplay() {
		turnTextView.setTextColor(Color.rgb(239, 228, 176));
		turnTextView.setText(this.myName());
		animator.start();
		//Toast.makeText(getApplicationContext(), "PLAYER " + game.whoseTurn() + "'s turn", Toast.LENGTH_LONG).show();
		board.invalidate();
	}

	/**
     * Callback method for listening for click events; takes the appropriate action depending on what button was clicked

		@param view	The view that was clicked.
     */
	public void onClick(View button) {
		if(button == pauseButton){
			
    		this.openContextMenu(button);
    	}
    	else if(button == linkButton){
    		connectingPiece = true;
    		addingPiece = false;
    		//highlight button to let user know that it is currently selected
    		linkButton.setBackgroundColor(Color.YELLOW);
    		placePegButton.setBackgroundDrawable(originalPegState);
    		
    	}
    	else if(button == placePegButton){
    		addingPiece = true;
    		connectingPiece = false;
    		placePegButton.setBackgroundColor(Color.YELLOW);
    		linkButton.setBackgroundDrawable(originalLinkState);
    	}
    	else if(button == endTurn){
    		
    		board.clearHighlightedPieces();
    		takeAction(new EndTurnAction(this.game.whoseTurn()));
    		
    	}
    	else if(button == twixtButton){
    		takeAction(new TwixtAction(this.game.whoseTurn()));
    		if(this.game.whoseTurn() == 0 || this.game.whoseTurn() == 2){
    			twixtCalledOne = true;
    		}
    		else{
    			twixtCalledTwo = true;
    		}
    	}
	}
	
	/**
     * Overridden method to create the contextual menu in the activity class

	@param menu	The menu that the XML def is inflated to
	@param view	The button which opens the menu
	@param menuInfo	Additional information about the menu being created
     */

    @Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, view, menuInfo);
    	MenuInflater inflater = this.getMenuInflater();
    	inflater.inflate(R.menu.pause_options, menu);
    }
    
    /**
     * This method is called when an item in the contextual pause menu is selected.
     * 
     * @param item	The item that was selected in the contextual menu
		@return 	Whether the action was consumed
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
		switch(item.getItemId()){
		
		case R.id.viewRules:
			
			//start a new activity that displays the rules
			Intent intent = new Intent(this, ViewRulesActivity.class);
			startActivity(intent);
			return true;
			
		case R.id.surrender:
			((TwixtGame)game).setGameOver(true);
			
			//to get game framework to invoke isGameOver()
			this.takeAction(new SurrenderAction(this.game.whoseTurn()));
			return true;
			
		case R.id.saveAndQuit:
			
			this.takeAction(new QuitAction(this.game.whoseTurn()));
			return true;
			
		default:
			return false;
		}	
    
    }
    
    /**
     * This method determines if two lines intersect given the coordinates of both lines. 
     * 
     * @param x1	First x coord of the first line
     * @param y1	First y coord of the first line
     * @param x2	Second x coord of the first line
     * @param y2	Second y coord of the first line
     * @param x3	First x coord of the second line
     * @param y3	First y coord of the second line
     * @param x4	Second x coord of the second line
     * @param y4	Second y coord of the second line
     * @return	Whether the lines intersect 
     */
    public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3,
    		double x4, double y4){
    	
    	//if any line is zero length, well then they don't intersect
    	if((x1 == x2 && y1 == y2) || (x3 == x4 && y3 == y4)){
			return false;
		}
		
		double ax = (x2-x1)*.92;
	    double ay = (y2-y1)*.92;
	    double bx = (x3-x4)*.92;
	    double by = (y3-y4)*.92;
	    double cx = x1-x3;
	    double cy = y1-y3; 
	    
	    double alphaNumerator = by*cx - bx*cy;
	    double commonDenominator = ay*bx - ax*by;
		
	    if (commonDenominator > 0){
	    	if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
	    		return false;
	    	}
	    }else if (commonDenominator < 0){
	    	if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
	    		return false;
	    	}
	    }
	    double betaNumerator = ax*cy - ay*cx;
	    if (commonDenominator > 0){
	    	if (betaNumerator < 0 || betaNumerator > commonDenominator){
	    		return false;
	    	}
	    }else if (commonDenominator < 0){
	    	if (betaNumerator > 0 || betaNumerator < commonDenominator){
	    		return false;
	    	}
	    }
    	return true;
    }
    
    
    /**
     * This method listens for change in the animation's value. 
     * It animates the current player text view.
     */
	public void onAnimationUpdate(ValueAnimator animation) {
		int value = (Integer) animation.getAnimatedValue();
		if(this.game.whoseTurn() == 0 || this.game.whoseTurn() == 2){
			turnTextView.setBackgroundColor(Color.argb(value, 0, 0, 255));
		}
		else{
			turnTextView.setBackgroundColor(Color.argb(value, 255, 0, 0));
		}
		
	}
	
	public Button getPegButton(){
		return placePegButton;
	}
	
	public TwixtGame getGame(){
    	return (TwixtGame)this.game;
    }
    
    

}// class TwixtHumanPlayer
