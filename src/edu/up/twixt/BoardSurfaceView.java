package edu.up.twixt;

import java.io.Serializable;
import java.util.Vector;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

/**
 * This class defines the board that is drawn on the screen to represent the location of 
 * each game piece and bridge. The user interacts with this class because it is defined as
 * a surface view that responds to touch events. 
 * 
 * @author Daniel Ehmig
 * @author Sherry Liao
 * @author Josh McCleary
 *
 */
public class BoardSurfaceView extends SurfaceView implements View.OnTouchListener, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 180058517232596922L;

	/**
     * tan rectangle representing the board, drawn on the surface view
     */

    private Rect boardRect;

    /**
     * defines the painting attributes for the board rectangle
     */

    private Paint boardPaint;

    /**
     * defines paint attributes for the red home rows and the red player's pegs
     */

    private Paint redLinePaint;

    /**
     * defines paint attributes for the blue home rows and the blue player's pegs
     */

    private Paint blueLinePaint;

    /**
     * defines paint attributes for an empty hole on the board
     */

    private Paint holePaint;

    /**
     * Defines the paint attributes for a highlighted peg.
     */

    private Paint highlightPaint;

    /**
     * height of the board rectangle
     */

    public static final int BOARD_HEIGHT = 750;

    /**
     * width of the board rectangle
     */

    public static final int BOARD_WIDTH = 750;
    
    /**
     * radius of each hole
     */
    
    public static final int RADIUS = 10;
    
    /**
     * center x coord of the first hole
     */
    
    public static final int FIRST_X = 30;
    
    /**
     * center y coord of the first hole
     */
    
    public static final int FIRST_Y = 30;
    
    /**
     * distance between the centers of each hole
     */
    
    public static final int CENT_DIST = 40;

    
    
    //player is light or dark
    private int playerType;
    
    //reference to the board state
    protected static TwixtPiece[][] pieceMatrix;
    
    //first piece selected when making a bridge
    private TwixtPiece firstPieceSelected;
    
    //the piece that was currently added this turn
    private TwixtPiece currentPiece = null;
    
    private TwixtHumanPlayer player = null;
    private TwixtGame game;

    /**
     * constructor just call init()
     * @param context The application context
     */

    public BoardSurfaceView (Context context) {
    	super(context);
    	init();
    }


    /**
     * constructor just calls init
     * @param context	The application context
     * @param attrs
     */

    public BoardSurfaceView (Context context, AttributeSet attrs) {
    	super(context, attrs);
    	init();
    }


    /**
     * constructor just calls init
     * @param context
     * @param attrs
     * @param defStyle
     */

    public BoardSurfaceView (Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
    	init();
    }


    /**
     * Initializes attributes of the surface view
     */

    public void init() {
    	
    	
    	boardRect = new Rect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
    	boardPaint = new Paint();
    	boardPaint.setColor(Color.rgb(239, 228, 176));
    	
    	redLinePaint = new Paint();
    	redLinePaint.setColor(Color.RED);
    	redLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    	redLinePaint.setStrokeWidth(5);
    	
    	blueLinePaint = new Paint();
    	blueLinePaint.setColor(Color.BLUE);
    	blueLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    	blueLinePaint.setStrokeWidth(5);
    	
    	holePaint = new Paint();
    	holePaint.setColor(Color.BLACK);
    	holePaint.setStyle(Paint.Style.STROKE);
    	
    	highlightPaint = new Paint();
    	highlightPaint.setColor(Color.GREEN);
    	highlightPaint.setStyle(Paint.Style.FILL);
    	this.setWillNotDraw(false);
    	
    	
    }
    
    /**
     * This method draws the board, the current peg positions, and the current bridge positions

		@param canvas	The canvas that will be drawn on
     */

    @Override
	public void onDraw(Canvas canvas) {

    	canvas.drawRect(boardRect, boardPaint);

    	//to ensure corners are not drawn
    	boolean draw = true;
    	
    	if(player != null){
    		game = player.getGame();
    		pieceMatrix = TwixtGame.getBoardState();
    		if(game.whoseTurn() == 0 || game.whoseTurn() == 2){
    			playerType = TwixtPiece.LIGHT_PEG;
    		}
    		else{
    			playerType = TwixtPiece.DARK_PEG;
    		}
    	}
    	//draw each of the pieces on the board based on their current state
    	for(int i = 0; i < pieceMatrix.length; ++i){
    		for(int j = 0; j < pieceMatrix[i].length; ++j){
    			if(i == 0 && j == 0){
    				draw = false;
    			}
    			else if(i == 17 && j == 0){
    				draw = false;
    			}
    			else if(i == 17 && j == 17){
    				draw = false;
    			}
    			else if(i == 0 && j == 17){
    				draw = false;
    			}
    			else{
    				draw = true;
    			}
    			if(draw){

    				//only draw the permanently set pieces and the previously placed piece
    				if(pieceMatrix[i][j].isThisSet() || pieceMatrix[i][j] == currentPiece){
    					
    					//draw the pieces that are highlighted while placing bridges
    					if(pieceMatrix[i][j].getIsHighlighted()){

    						canvas.drawCircle(FIRST_X + (j*CENT_DIST), FIRST_Y + (i*CENT_DIST),
    								RADIUS, highlightPaint);

    					}
    					
    					//draw blue pieces and bridges if the player is light
    					else if(pieceMatrix[i][j].getPlayerType() == TwixtPiece.LIGHT_PEG){

    						canvas.drawCircle(FIRST_X + (j*CENT_DIST), FIRST_Y + (i*CENT_DIST),
    								RADIUS, blueLinePaint);
  						
    					}
    					//draw red pieces and bridges if the player is dark
    					else if(pieceMatrix[i][j].getPlayerType() == TwixtPiece.DARK_PEG){

    						canvas.drawCircle(FIRST_X + (j*CENT_DIST), FIRST_Y + (i*CENT_DIST),
    								RADIUS, redLinePaint);
 						
    					}

    					
    				}
    				//the hole doesn't have a piece placed on it
    				else{
    					canvas.drawCircle(FIRST_X + (j*CENT_DIST), FIRST_Y + (i*CENT_DIST),
    							RADIUS, holePaint);
    				}

    			}//if(draw)
    		}//inner for
    	}//outer for
    	
    	//now draw connections
    	for(int i = 0; i < pieceMatrix.length; ++i){
    		for(int j = 0; j < pieceMatrix[i].length; ++j){
    			if(pieceMatrix[i][j].getPlayerType() != TwixtPiece.EMPTY){
    				
    				Vector<TwixtPiece> conns = pieceMatrix[i][j].getConnections();
    				for(int k = 0; k < conns.size(); ++k){
    					if(pieceMatrix[i][j].getPlayerType() == TwixtPiece.LIGHT_PEG){
    						canvas.drawLine(pieceMatrix[i][j].getCenterX(), pieceMatrix[i][j].getCenterY(), 
    								conns.get(k).getCenterX(), conns.get(k).getCenterY(), blueLinePaint);
    						
    					}
    					else{
    						canvas.drawLine(pieceMatrix[i][j].getCenterX(), pieceMatrix[i][j].getCenterY(), 
    								conns.get(k).getCenterX(), conns.get(k).getCenterY(), redLinePaint);
    					}
    					
    				}
    			}
    		}
    	}

    	//draw the home row borders
    	canvas.drawLine(50, 50, 690, 50, redLinePaint);
    	canvas.drawLine(50, 50, 50, 690, blueLinePaint);
    	canvas.drawLine(50, 690, 690, 690, redLinePaint);
    	canvas.drawLine(690, 690, 690, 50, blueLinePaint);


    }//onDraw


    /**
     * Callback method for touch events on the surface view; based on where a player touched,
     * it will add a peg in that location or set a connection for a bridge

		@param view	View that was touched
		@param event	Contains information about the touch event
     * @return 
     */
    public boolean onTouch(View view, MotionEvent event) {
    	
    	int x = (int)event.getX();
    	int y = (int)event.getY();
    	
    	if(event.getAction() == MotionEvent.ACTION_DOWN){
    		if(player != null){
    			if(isValidMove(x,y)){
    				if(player.addingPiece){
    					
    					clearPreviouslyAdded();
    					currentPiece = new TwixtPiece(x, y, playerType);
    					pieceMatrix[currentPiece.getRow()][currentPiece.getCol()] = currentPiece;
    			
    				}
    				else if(player.connectingPiece){
    					if(!player.firstPieceSelected){
    						player.firstPieceSelected = true;
    						firstPieceSelected = new TwixtPiece(x, y, playerType);
    						setHighlightedPieces();
    					}
    					else{
    						
    						TwixtPiece second = new TwixtPiece(x, y, playerType);
    						int firstRow = firstPieceSelected.getRow();
    						int firstCol = firstPieceSelected.getCol();
    						int secondRow = second.getRow();
    						int secondCol = second.getCol();
  
    						pieceMatrix[firstRow][firstCol].addConnection(pieceMatrix[secondRow][secondCol]);
    						pieceMatrix[secondRow][secondCol].addConnection(pieceMatrix[firstRow][firstCol]);
    						pieceMatrix[firstRow][firstCol].setAsPermanent();
    						pieceMatrix[secondRow][secondCol].setAsPermanent();
    						
    						//If they bridge the piece they added this turn, it becomes permanent - essentially
    						if(pieceMatrix[firstRow][firstCol] == currentPiece || pieceMatrix[secondRow][secondCol] == 
    								currentPiece){
    							player.getPegButton().setEnabled(false);
    						}
    						player.firstPieceSelected = false;
    						clearHighlightedPieces();
    					}
    				}
    			}
    		}
    	}
    	this.invalidate();
		return true;
    
    }
    
    /**
     * This method determines which pieces should be highlighted for the user to see
     * which bridges they can make with the current piece.
     * 
     */
    private void setHighlightedPieces(){
    	
    	
    	int row = firstPieceSelected.getRow();
    	int col = firstPieceSelected.getCol();
    	
    	for(int i = 0; i < pieceMatrix.length; ++i){
    		for(int j = 0; j < pieceMatrix[i].length; ++j){
    			int id = pieceMatrix[i][j].getPlayerType();
    			boolean toHighlight = false;
    			
    			if(playerType == id){
        			//check if piece is knight's move away
        			if(i == row-2 && j == col-1){
        				toHighlight = true;
        			}
        			else if(i == row-1 && j == col-2){
        				toHighlight = true;
        			}
        			else if(i == row+1 && j == col-2){
        				toHighlight = true;
        			}
        			else if(i == row+2 && j == col-1){
        				toHighlight = true;
        			}
        			else if(i == row-2 && j == col+1){
        				toHighlight = true;
        			}
        			else if(i == row-1 && j == col+2){
        				toHighlight = true;
        			}
        			else if(i == row+1 && j == col+2){
        				toHighlight = true;
        			}
        			else if(i == row+2 && j == col+1){
        				toHighlight = true;
        			}
        			
        			if(toHighlight){
        				pieceMatrix[i][j].setIsHighlighted(true);
        			}
    			}
    			
    		}//inner for
    	}//outer for
    	
    	//Determine if connecting a bridge to the highlighted pieces
    	//would cross any existing bridges
    	int xOne, xTwo, xThree, xFour;
    	int yOne, yTwo, yThree, yFour;
    	xTwo = firstPieceSelected.getX();
    	yTwo = firstPieceSelected.getY();
    	for(int i = 0; i < pieceMatrix.length; ++i){
    		for(int j = 0; j < pieceMatrix[i].length; ++j){
    			
    			//found a highlighted piece, now check if bridge to current piece is valid
    			if(pieceMatrix[i][j].getIsHighlighted()){
    				
    				TwixtPiece hPiece = pieceMatrix[i][j];
    				xOne = hPiece.getX();
    				yOne = hPiece.getY();
    				
    				//go through every piece (except the highlighted one!), and check
    				//its if the bridge blocks the way
    				for(int k = 0; k < pieceMatrix.length; ++k){
    					for(int p = 0; p < pieceMatrix[k].length; ++p){
    						
    						if(pieceMatrix[k][p] != hPiece){
    							
    							xThree = pieceMatrix[k][p].getX();
    							yThree = pieceMatrix[k][p].getY();
    							Vector<TwixtPiece> conns = pieceMatrix[k][p].getConnections();
    							
    							for(int index = 0; index < conns.size(); index++){
    								
    								TwixtPiece toCheck = conns.get(index);
    								xFour = toCheck.getX();
    								yFour = toCheck.getY();
    								
    								if(TwixtHumanPlayer.linesIntersect(xOne, yOne, xTwo, yTwo, xThree, yThree,
    										xFour, yFour)){ 
    									
    									hPiece.setIsHighlighted(false); 
    								}//if
    									
    							}//connections for loop
    							
    						}//if
    					}//third inner for
    				}//second inner for
    			}//if 
    		}//inner for
    	}//outer for
    	
    }//setHighlightedPieces
    
    /**
     * This method determines whether the player's move was legal. The move must 
     * satisfy the following conditions:
     * -The player did not try to add a piece in their opponent's home rows
     * -The player did not try to add a piece where a piece already existed
     * -When connecting pieces, the player touched their own piece to initiate the connection
     * -When connecting pieces, the player touched a highlighted piece to connect to
     * 
     * @param x	The x coordinate of the player's touch
     * @param y	The y coordinate of the player's touch
     * @return Whether the player's move was valid
     */
    private boolean isValidMove(int x, int y){
    	
    	//token twixt piece to check location against
    	TwixtPiece toCheck = new TwixtPiece(x, y, -1);
    	
    	
    	boolean validMove = false;
    	
    	//if the player touched in an opponent's home row, tis' 
    	//automatically an invalid move
    	if(game.whoseTurn() == 0 || game.whoseTurn() == 2){
    		if(toCheck.getRow() == 0 || toCheck.getRow() == TwixtGame.NUM_PEGS-1){
    			return false;
    		}
    	}
    	else if(game.whoseTurn() == 1 || game.whoseTurn() == 3){
    		if(toCheck.getCol() == 0 || toCheck.getCol() == TwixtGame.NUM_PEGS-1){
    			return false;
    		}
    	}
 
    	if(player.connectingPiece){
    		if(player.firstPieceSelected){
    			
    			//if the piece is highlighted, it better darn well be a valid move
    			if(pieceMatrix[toCheck.getRow()][toCheck.getCol()].getIsHighlighted()){
    				validMove = true;
    			}
    			
    			//ok, the user wanted to bridge a different one of their pieces instead
    			else if(pieceMatrix[toCheck.getRow()][toCheck.getCol()].getPlayerType() == TwixtPiece.LIGHT_PEG){
        			if(game.whoseTurn() == 0 || game.whoseTurn() == 2){
        				validMove = true;
        				player.firstPieceSelected = false;
        				clearHighlightedPieces();
        			}
        		}
        		else if(pieceMatrix[toCheck.getRow()][toCheck.getCol()].getPlayerType() == TwixtPiece.DARK_PEG){
        			if(game.whoseTurn() == 1 || game.whoseTurn() == 3){
        				validMove = true;
        				player.firstPieceSelected = false;
        				clearHighlightedPieces();
        			}
        		}
    		}
    		
    		//if the player touched one of their pieces, it's all good
    		else if(pieceMatrix[toCheck.getRow()][toCheck.getCol()].getPlayerType() == TwixtPiece.LIGHT_PEG){
    			if(game.whoseTurn() == 0 || game.whoseTurn() == 2){
    				validMove = true;
    			}
    		}
    		else if(pieceMatrix[toCheck.getRow()][toCheck.getCol()].getPlayerType() == TwixtPiece.DARK_PEG){
    			if(game.whoseTurn() == 1 || game.whoseTurn() == 3){
    				validMove = true;
    			}
    		}
    	}
    	else{
    		if(pieceMatrix[toCheck.getRow()][toCheck.getCol()].getPlayerType() == TwixtPiece.EMPTY){
    			validMove = true;
    		}
    	}
    	return validMove;
    	
    }
    
    /**
     * This method clears the player's highlighted pieces after they have connected two pieces together, 
     * or the user decided to bridge a different one of their pieces
     */
    public void clearHighlightedPieces(){
    	
    	for(int i = 0; i < pieceMatrix.length; ++i){
    		for(int j = 0; j < pieceMatrix[i].length; ++j){
    			pieceMatrix[i][j].setIsHighlighted(false);
    		}
    	}
    }
    
    /**
     * This method allows the surface view to have a reference to the player object
     * @param playerRef	The TwixtHumanPlayer object to be referenced
     */
    public void receivePlayerRef(TwixtHumanPlayer playerRef){
    	this.player = playerRef;
    }
    
    /**
     * This method essentially "resets" the board to its state from the beginning
     * of the player's current turn. This is necessary so that the player cannot add more
     * than one piece.
     */
    private void clearPreviouslyAdded(){
    	for(int i = 0; i < pieceMatrix.length; ++i){
    		for(int j = 0; j < pieceMatrix[i].length; ++j){
    			if(!pieceMatrix[i][j].isThisSet()){
    				pieceMatrix[i][j].setPlayerType(TwixtPiece.EMPTY);
    			}
    		}
    	}
    }
    
    
}




