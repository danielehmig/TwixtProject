package edu.up.twixt;

import java.util.Vector;

import edu.up.game.*;

/**
 * This class represents the Random Computer Player. The AI will choose to add a piece at a random, empty
 * location on the board. It then proceeds to connect that piece to any that are a knight's move away. 
 * 
 * @author Daniel Ehmig
 * @author Sherry Liao
 * @author Josh McCleary
 */
public class TwixtEasyComputerPlayer extends GameComputerPlayer
{	
	//computer is light or dark player
	private int playerType;
	
	//reference to board state
	protected static TwixtPiece[][] pieceMatrix;


	/**
	 * Constructor for objects of type TwixtEasyComputerPlayer
	 */
	public TwixtEasyComputerPlayer()
	{
		super();
	}

	/**
	 * Action to perform when a move is requested. The AI places a peg in a random location and 
	 * connects any bridges. It then proceeds to end its turn.
	 */

	protected GameAction calculateMove() {
		
		//if the computer is first, initialize the board
		if(!TwixtGame.boardInitialized){
			TwixtGame.initializeBoard();
			TwixtGame.boardInitialized = true;
		}
		
		pieceMatrix = TwixtGame.pieceMatrix;
		if(game.whoseTurn() == 0 || game.whoseTurn() == 2){
			playerType = TwixtPiece.LIGHT_PEG;
		}
		else{
			playerType = TwixtPiece.DARK_PEG;
		}
 
		//Add a random piece
		boolean placingPeg = true;

		int randCol = 0;
		int randRow = 0;

		//random row or col may not be valid, so loopdeedoop!
		while(placingPeg){

			randCol = (int)(Math.random() * (TwixtGame.NUM_PEGS-1));
			randRow = (int)(Math.random() * (TwixtGame.NUM_PEGS-1));

			if(pieceMatrix[randRow][randCol].getPlayerType() == TwixtPiece.EMPTY){
				placingPeg = false;
			}

			//don't choose home rows!
			if(playerType == TwixtPiece.DARK_PEG){
				if(randCol == 0 || randCol == TwixtGame.NUM_PEGS-1){
					placingPeg = true;
				}
			}
			else if(playerType == TwixtPiece.LIGHT_PEG){
				if(randRow == 0 || randRow == TwixtGame.NUM_PEGS-1){
					placingPeg = true;
				}
			}
		}
		
		//add the new piece
		int centerX = 30 + (randCol * 40);
    	int centerY = 30 + (randRow * 40);
    	TwixtPiece temp = new TwixtPiece(centerX, centerY, playerType);
    	temp.setAsPermanent();
		pieceMatrix[randRow][randCol] = temp;

		//OK, connect some pieces!
		for(int k = 0; k < pieceMatrix.length; ++k){
			for(int p = 0; p < pieceMatrix[k].length; ++p){

				if(pieceMatrix[k][p].getPlayerType() == playerType){
					
					boolean connectPieces = false;
					if(k == randRow-2 && p == randCol-1){
						connectPieces = true;
					}
					else if(k == randRow-1 && p == randCol-2){
						connectPieces = true;
					}
					else if(k == randRow+1 && p == randCol-2){
						connectPieces = true;
					}
					else if(k == randRow+2 && p == randCol-1){
						connectPieces = true;
					}
					else if(k == randRow-2 && p == randCol+1){
						connectPieces = true;
					}
					else if(k == randRow-1 && p == randCol+2){
						connectPieces = true;
					}
					else if(k == randRow+1 && p == randCol+2){
						connectPieces = true;
					}
					else if(k == randRow+2 && p == randCol+1){
						connectPieces = true;
					}
					
					//ok, the piece is a knight's move away, now check if the bridge will cross any other bridges
					if(connectPieces){
						double x1, x2, x3, x4;
						double y1, y2, y3, y4;
						x1 = pieceMatrix[randRow][randCol].getCenterX();
						x2 = pieceMatrix[k][p].getCenterX();
						y1 = pieceMatrix[randRow][randCol].getCenterY();
						y2 = pieceMatrix[k][p].getCenterY();
						boolean linesIntersect = false;
						
						for(int y = 0; y < pieceMatrix.length; ++y){
							for(int z = 0; z < pieceMatrix[y].length; ++z){
								if(pieceMatrix[randRow][randCol] != pieceMatrix[y][z]){
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
							pieceMatrix[randRow][randCol].addConnection(pieceMatrix[k][p]);
							pieceMatrix[k][p].addConnection(pieceMatrix[randRow][randCol]);
						}
						
					}//if connectPieces
					
				}//if playerType
			}//inner for
		}//outer for
		
		//OK, THE TURN'S DONE
		return new EndTurnAction(game.whoseTurn());
	}
}
					