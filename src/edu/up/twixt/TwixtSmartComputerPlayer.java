package edu.up.twixt;

import java.util.Vector;

import edu.up.game.GameAction;

import edu.up.game.GameComputerPlayer;

/** 
	This class defines the smart version of our AI player. The AI will choose a position based
	on whether they have a piece a knight's move away. Also, it will take into account how many empty
	positions are a knight's move away from THOSE positions. Lastly, it will connect any pieces that it can.
	
	@author Daniel Ehmig
	@author Josh McCleary
	@author Sherry Liao
*/

public class TwixtSmartComputerPlayer extends GameComputerPlayer {
	
	//Computer is light or dark player
	private int playerType;
	
	//first three turns will be random
	private static int turnCount = 0;
	
	//reference to board state
	protected static TwixtPiece[][] pieceMatrix;
	
	//which row the AI will add their piece
	private int rowToAdd;
	
	//which col the AI will add their piece
	private int colToAdd;
	
	/**
	 * Constructor creates a Smart Computer Player Object
	 */
	public TwixtSmartComputerPlayer(){
		super();
	}
	
	/**
     * This method contains a set of algorithms that the smart AI will use to make a move. 
     */
	@Override
	protected GameAction calculateMove() {
		
		//If the AI goes first, the board will need to be initialized
		if(!TwixtGame.boardInitialized){
			TwixtGame.boardInitialized = true;
			TwixtGame.initializeBoard();
		}
		
		pieceMatrix = TwixtGame.pieceMatrix;
		

		if(game.whoseTurn() == 0 || game.whoseTurn() == 2){
			playerType = TwixtPiece.LIGHT_PEG;
		}
		else{
			playerType = TwixtPiece.DARK_PEG;
		}

		if(turnCount <= 1){
			turnCount++;
			if(playerType == TwixtPiece.LIGHT_PEG){
				if(turnCount == 1){
					colToAdd = 0;
				}
				else{
					colToAdd = TwixtGame.NUM_PEGS-1;
				}
				rowToAdd = (int)(Math.random() * (TwixtGame.NUM_PEGS-2) + 1);
			}
			else{
				if(turnCount == 1){
					rowToAdd = 0;
				}
				else{
					rowToAdd = TwixtGame.NUM_PEGS-1;
				}
				colToAdd = (int)(Math.random() * (TwixtGame.NUM_PEGS-2) + 1);
			}
			
		}
		//first three moves are random
		else if(turnCount > 1 && turnCount < 5){
			turnCount++;
			boolean findingSpot = true;
			while(findingSpot){

				colToAdd = (int)(Math.random() * (TwixtGame.NUM_PEGS-1));
				rowToAdd = (int)(Math.random() * (TwixtGame.NUM_PEGS-1));

				if(pieceMatrix[rowToAdd][colToAdd].getPlayerType() == TwixtPiece.EMPTY){

					findingSpot = false;

				}

				//don't choose home rows!
				if(playerType == TwixtPiece.DARK_PEG){
					if(colToAdd == 0 || colToAdd == TwixtGame.NUM_PEGS-1){
						findingSpot = true;
					}
				}
				else{
					if(rowToAdd == 0 || rowToAdd == TwixtGame.NUM_PEGS-1){
						findingSpot = true;
					}
				}

			}
		}

		//ok these moves are only semi-random
		else{
			boolean addingPiece = true;
			//each position on the board will be assigned a value
			int[][] posValues = new int[TwixtGame.NUM_PEGS][TwixtGame.NUM_PEGS];
			while(addingPiece){

				//increase value of positions that are a knight's move away from an existing piece
				for(int i = 0; i < pieceMatrix.length; ++i){
					for(int j = 0; j < pieceMatrix[i].length; ++j){

						if(pieceMatrix[i][j].getPlayerType() == playerType){

							for(int k = 0; k < pieceMatrix.length; ++k){
								for(int p = 0; p < pieceMatrix.length; ++p){
									boolean increment = false;

									if(k == i-2 && p == j-1){
										increment = true;
									}
									else if(k == i-1 && p == j-2){
										increment = true;
									}
									else if(k == i+1 && p == j-2){
										increment = true;
									}
									else if(k == i+2 && p == j-1){
										increment = true;
									}
									else if(k == i-2 && p == j+1){
										increment = true;
									}
									else if(k == i-1 && p == j+2){
										increment = true;
									}
									else if(k == i+1 && p == j+2){
										increment = true;
									}
									else if(k == i+2 && p == j+1){
										increment = true;
									}

									if(increment){
										posValues[k][p]++;
									}
								}
							}
						}
					}
				}
				
				for(int i = 0; i < posValues.length; ++i){
					for(int j = 0; j < posValues[i].length; ++j){
						
						if(posValues[i][j] > 0){
							posValues[i][j] = posValues[i][j] + ((int)(Math.random() * 5));
						}
					}
				}


				//don't want the corner
				int mostest = posValues[1][1];

				for(int i = 0; i < posValues.length; ++i){
					for(int j = 0; j < posValues[i].length; ++j){
						boolean validMove = false;

						//don't want pieces in opponent home rows to be chosen
						if(playerType == TwixtPiece.DARK_PEG){
							if(j > 0 && j < TwixtGame.NUM_PEGS-1){
								validMove = true;
							}
						}
						else{
							if(i > 0 && i < TwixtGame.NUM_PEGS-1){
								validMove = true;
							}
						}

						if(validMove){
							//MAY OR MAY NOT BE GEQUAL
							if(posValues[i][j] >= mostest){
								mostest = posValues[i][j];
								rowToAdd = i;
								colToAdd = j;
							}
						}

					}
				}
				
				if(pieceMatrix[rowToAdd][colToAdd].getPlayerType() == TwixtPiece.EMPTY){
					addingPiece = false;
				}
				else{
					posValues[rowToAdd][colToAdd] = 0;
					addingPiece = true;
				}

			}
		}
		
		//we have our row and col to add, now actually add the piece
		int centerX = 30 + (colToAdd * 40);
    	int centerY = 30 + (rowToAdd * 40); 
    	TwixtPiece freshPiece = new TwixtPiece(centerX, centerY, playerType);
    	freshPiece.setAsPermanent();
    	pieceMatrix[rowToAdd][colToAdd] = freshPiece;
    	
		
		//ok, we're done adding a piece; now connect any pieces to the recently added piece
    	for(int k = 0; k < pieceMatrix.length; ++k){
    		for(int p = 0; p < pieceMatrix[k].length; ++p){

    			if(pieceMatrix[k][p].getPlayerType() == playerType){
    				boolean connectPieces = false;

    				if(k == rowToAdd-2 && p == colToAdd-1){
    					connectPieces = true;
    				}
    				else if(k == rowToAdd-1 && p == colToAdd-2){
    					connectPieces = true;
    				}
    				else if(k == rowToAdd+1 && p == colToAdd-2){
    					connectPieces = true;
    				}
    				else if(k == rowToAdd+2 && p == colToAdd-1){
    					connectPieces = true;
    				}
    				else if(k == rowToAdd-2 && p == colToAdd+1){
    					connectPieces = true;
    				}
    				else if(k == rowToAdd-1 && p == colToAdd+2){
    					connectPieces = true;
    				}
    				else if(k == rowToAdd+1 && p == colToAdd+2){
    					connectPieces = true;
    				}
    				else if(k == rowToAdd+2 && p == colToAdd+1){
    					connectPieces = true;
    				}
    				
    				//knight's move away, now check if the bridge will cross any other bridge
    				if(connectPieces){
    					double x1, x2, x3, x4;
    					double y1, y2, y3, y4;
    					x1 = pieceMatrix[rowToAdd][colToAdd].getCenterX();
    					x2 = pieceMatrix[k][p].getCenterX();
    					y1 = pieceMatrix[rowToAdd][colToAdd].getCenterY();
    					y2 = pieceMatrix[k][p].getCenterY();
    					boolean linesIntersect = false;

    					for(int y = 0; y < pieceMatrix.length; ++y){
    						for(int z = 0; z < pieceMatrix[y].length; ++z){
    							if(pieceMatrix[y][z] != pieceMatrix[rowToAdd][colToAdd]){
    								Vector<TwixtPiece> conns = pieceMatrix[y][z].getConnections();

    								for(int index = 0; index < conns.size(); index++){

    									x3 = pieceMatrix[y][z].getCenterX();
    									y3 = pieceMatrix[y][z].getCenterY();
    									TwixtPiece toCheck = conns.get(index);
    									x4 = toCheck.getCenterX();
    									y4 = toCheck.getCenterY();

    									if(TwixtHumanPlayer.linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4)){
    										linesIntersect = true;
    									}
    								}//connections
    							}


    						}//inner for
    					}//outer for
    					if(!linesIntersect){
    						pieceMatrix[rowToAdd][colToAdd].addConnection(pieceMatrix[k][p]);
    						pieceMatrix[k][p].addConnection(pieceMatrix[rowToAdd][colToAdd]);
    					}
    				}
    			}
    		}
    	}

    	
    	

		//OK, THE TURN'S DONE!
		return new EndTurnAction(game.whoseTurn());
	}
	
}
