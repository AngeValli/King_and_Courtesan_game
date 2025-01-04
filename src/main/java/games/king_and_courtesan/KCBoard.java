/*Class representing the board of the game, containing a constructor to load a board representation from a string object loaded from a file*/
package games.king_and_courtesan;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import java.lang.Math;

import iialib.games.model.IBoard;
import iialib.games.model.Score;

public class KCBoard implements IBoard<KCMove, KCRole, KCBoard> {
	
	
	private static int DEFAULT_GRID_SIZE = 6;

	
	// --------- Class Attribute ---------
	
	public static int GRID_SIZE = DEFAULT_GRID_SIZE;

	private enum SQUARE {
	    EMPTY(0), BLUE(1), RED(2);

		private final int value;
	
	private SQUARE(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	};

    private static final KCPosition BLUE_KING_START = new KCPosition(GRID_SIZE - 1, GRID_SIZE - 1);
    private static final KCPosition RED_KING_START = new KCPosition(0,0);

	// ---------------------- Attributes ---------------------

    private SQUARE[][] boardGrid; // On représente le plateau comme un carré (rotation à 45°) les bleus sont en *haut à droite* et les rouges en *bas à gauche*

    private KCPosition blueKing;
    private KCPosition redKing;


	// ---------------------- Constructors ---------------------
	
    public KCBoard() {
	 boardGrid = new SQUARE[GRID_SIZE][GRID_SIZE];
		for (int i = 0; i < GRID_SIZE; i++)
			for (int j = 0; j < GRID_SIZE; j++)
				boardGrid[i][j] = SQUARE.EMPTY;

		// initial board state
		for (int i = 0; i < GRID_SIZE - 1; i++)
			for (int j = 0; j < GRID_SIZE - 1 - i; j++)
				boardGrid[i][j] = SQUARE.RED;
		
	       	for (int i = GRID_SIZE - 1; i > 0; i--)
			for (int j = i; j > 0; j--)
				boardGrid[i][GRID_SIZE - j] = SQUARE.BLUE;

		blueKing = new KCPosition(BLUE_KING_START);
		redKing = new KCPosition(RED_KING_START);
	}

    private KCBoard(SQUARE[][] other, KCPosition bk, KCPosition rk) {
		boardGrid = new SQUARE[GRID_SIZE][GRID_SIZE];
		for (int i = 0; i < GRID_SIZE; i++)
			System.arraycopy(other[i], 0, boardGrid[i], 0, GRID_SIZE);
		blueKing = bk;
		redKing = rk;
	}
	
	public KCBoard(String board) {
        boardGrid = new SQUARE[GRID_SIZE][GRID_SIZE];
        String[] rows = board.split("\n");
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
				if (rows[i].charAt(j) == 'B') {
					boardGrid[GRID_SIZE-1-i][j] = SQUARE.BLUE;
				} else if (rows[i].charAt(j) == 'R') {
					boardGrid[GRID_SIZE-1-i][j] = SQUARE.RED;
				} else if (rows[i].charAt(j) == '-') {
					boardGrid[GRID_SIZE-1-i][j] = SQUARE.EMPTY;
				}
			}
		}
		for (int i = GRID_SIZE; i < GRID_SIZE+2; i++) {
			int length_row = rows[i].length();
			int i_position = Character.getNumericValue(rows[i].charAt(length_row-4)); // the EOF character
			int j_position = Character.getNumericValue(rows[i].charAt(length_row-2));
			if (i == GRID_SIZE) {
				blueKing = new KCPosition(i_position, j_position);
			} else if (i == GRID_SIZE+1) {
				redKing = new KCPosition(i_position, j_position);
			}
		}
	}
	
	public KCBoard copy() {
	    return new KCBoard(boardGrid,blueKing,redKing);
	}

	
	// --------------------- Methods ---------------------

    public void play_(KCMove move, KCRole playerRole){
		
		KCPosition currKing = getCurrentKing(playerRole);
		boolean movingKing = currKing.equals(move.start);
		
		boolean isSwapping = boardGrid[move.end.lin][move.end.col] == (playerRole == KCRole.BLUE ? SQUARE.BLUE :  SQUARE.RED);
		// déplacer le jeton
		boardGrid[move.end.lin][move.end.col] = (playerRole == KCRole.BLUE) ? SQUARE.BLUE : SQUARE.RED;
		// Si on déplace un roi, en tenir compte
		if(movingKing){
		    currKing.lin = move.end.lin;
		    currKing.col = move.end.col;
		}
		// On ne fait rien pour le roi adverse si on le prends. La vérification pour la victoire vérifiera l'invariant que la position du roi est une case de sa couleur 
		// Vider l'ancienne case si on a pas bougé de roi ou fait de swap
		if(!isSwapping)
			boardGrid[move.start.lin][move.start.col] = SQUARE.EMPTY;
		
    }
    
	@Override
	public KCBoard play(KCMove move, KCRole playerRole) {
		KCBoard newBoard = this.copy();
		newBoard.play_(move, playerRole);
		return newBoard;
	}

	@Override
	public ArrayList<KCMove> possibleMoves(KCRole playerRole) {
	    ArrayList<KCMove> allPossibleMoves = new ArrayList<>();
	    int direction = getDirection(playerRole);
	    SQUARE curr_color = getCurrentColor(playerRole);
	    SQUARE opp_color =  getOppositeColor(playerRole);
	    KCPosition curr_king = getCurrentKing(playerRole);
	    for (int i = 0; i < GRID_SIZE; i++) { 		// lines
			for (int j = 0; j < GRID_SIZE; j++) { 		// columns
			    if(boardGrid[i][j] == curr_color){
				// capturer
				for(int k = -1; k < 2; k++){
				    for(int l = -1; l < 2; l++){
					if((k != 0 || l != 0) && i + k >= 0 && i + k < GRID_SIZE && j + l >= 0 && j + l < GRID_SIZE ) {
					    if(boardGrid[i+k][j+l] == opp_color)
						allPossibleMoves.add(new KCMove(new KCPosition(i,j), new KCPosition(i+k,j+l)));
					}
				    }
				}
				// deplacer + echange du roi
				boolean movingKing = curr_king.equals(new KCPosition(i,j));
				//System.out.println(movingKing);
				if(isValidForwardMove(i+direction,j,movingKing,curr_color))
				    allPossibleMoves.add(new KCMove(new KCPosition(i,j), new KCPosition(i+direction,j)));
				if(isValidForwardMove(i+direction,j+direction,movingKing,curr_color))
				    allPossibleMoves.add(new KCMove(new KCPosition(i,j), new KCPosition(i+direction,j+direction)));
				if(isValidForwardMove(i,j+direction,movingKing,curr_color))
				    allPossibleMoves.add(new KCMove(new KCPosition(i,j), new KCPosition(i,j+direction)));
			    }
			}
		}
	    return allPossibleMoves;

	}

    private boolean isValidForwardMove(int line, int col, boolean movingKing,SQUARE curr_color){
	return line >= 0 && line < GRID_SIZE && col >= 0 && col < GRID_SIZE && (boardGrid[line][col] == SQUARE.EMPTY // avancer 
										|| (boardGrid[line][col] == curr_color && movingKing) // échange du roi
										);
    }
    

	@Override
	public boolean isValidMove(KCMove move, KCRole playerRole) {
	    KCPosition currKing = getCurrentKing(playerRole);
	    SQUARE curr_color = getCurrentColor(playerRole);
	    SQUARE opp_color = getOppositeColor(playerRole);
	    int direction = getDirection(playerRole);

	    int sl = move.start.lin;
	    int sc = move.start.col;
	    int el = move.end.lin;
	    int ec = move.end.col;

	    boolean isKingMoving = currKing.equals(move.start);


	    return (sl >= 0 && sc >= 0 && sl < GRID_SIZE && sc < GRID_SIZE) // in bounds start pos
		&& (el >= 0 && ec >= 0 && el < GRID_SIZE && ec < GRID_SIZE) // in bounds end pos
		&& boardGrid[sl][sc] == curr_color // moving an existing pawn of the player
		&& ( (
		        (
		          (sl + direction == el && sc == ec)
			  || (sl + direction == el && sc + direction == ec)
			  || (sl == el && sc + direction == ec)
		        ) // moving to one of the three forward square
			&& (boardGrid[el][ec] != curr_color ||(boardGrid[el][ec] == curr_color && isKingMoving)) // moving forward or king swap
		     ) 
		     ||  (
			    move.start != move.end && (sl - el) >= -1 &&  (sl - el) <= 1 &&  (sc - ec) >= -1 &&  (sc - ec) <= 1 // move to one of the eight neighbours
			    && boardGrid[el][ec] == opp_color
			  ) // on a pawn of the opposite player 
		   );
	}

	@Override
	public boolean isGameOver() {
	    // vérifier si un roi a été capturé (la position du roi n'est pas une case de sa couleur)
	    return boardGrid[blueKing.lin][blueKing.col] != SQUARE.BLUE
	    || boardGrid[redKing.lin][redKing.col] != SQUARE.RED	
	    // vérifier si un roi a atteint la position de départ du roi adverse
		||  blueKing.equals(RED_KING_START) || redKing.equals(BLUE_KING_START);

	}

    	@Override
	public ArrayList<Score<KCRole>> getScores() {
		ArrayList<Score<KCRole>> scores = new ArrayList<Score<KCRole>>();
		    if (blueVictory()) {
				scores.add(new Score<KCRole>(KCRole.RED,Score.Status.LOOSE,0));
				scores.add(new Score<KCRole>(KCRole.BLUE,Score.Status.WIN,1));
			}
		    else if (redVictory()) {
			scores.add(new Score<KCRole>(KCRole.BLUE,Score.Status.LOOSE,0));
				scores.add(new Score<KCRole>(KCRole.RED,Score.Status.WIN,1));
			}			
		return scores;
	}

    public boolean blueVictory() {
	return  blueKing.equals(RED_KING_START) || boardGrid[redKing.lin][redKing.col] != SQUARE.RED;
    }

    public boolean redVictory() {
	return redKing.equals(BLUE_KING_START) || boardGrid[blueKing.lin][blueKing.col] != SQUARE.BLUE;
    }
	


    private SQUARE[][] copyGrid() {
		SQUARE[][] newGrid = new SQUARE[GRID_SIZE][GRID_SIZE];
		for (int i = 0; i < GRID_SIZE; i++)
			System.arraycopy(boardGrid[i], 0, newGrid[i], 0, GRID_SIZE);
		return newGrid;
	}

    private int getDirection(KCRole playerRole){
	return  (playerRole == KCRole.BLUE) ? -1 : 1;
    }
    
    private KCPosition getCurrentKing(KCRole playerRole){
	return  (playerRole == KCRole.BLUE) ? blueKing : redKing;
    }

    private SQUARE getCurrentColor(KCRole playerRole){
	return  (playerRole == KCRole.BLUE) ? SQUARE.BLUE : SQUARE.RED;
    }

    private SQUARE getOppositeColor(KCRole playerRole){
	return  (playerRole == KCRole.BLUE) ? SQUARE.RED : SQUARE.BLUE;
    }

	public int getCurrentNbPlayers(KCRole playerRole){
		int nbPlayers = 0;
		for (int i = 0; i < GRID_SIZE; i++) { 		// lines
			for (int j = 0; j < GRID_SIZE - 1; j++) { 		// columns
				if (boardGrid[i][j] == getCurrentColor(playerRole)) nbPlayers++;
			}
		}
		return nbPlayers;
	}

	public int KingDistToOppositeCamp(KCRole playerRole) {
		KCPosition kingPos = getCurrentKing(playerRole);
		int DistToOrigin = (playerRole == KCRole.BLUE) ? Math.abs(kingPos.lin - GRID_SIZE) + Math.abs(kingPos.col - GRID_SIZE) : kingPos.lin + kingPos.col; // Manhattan distance
		return (int) Math.pow(GRID_SIZE,2) - DistToOrigin;
	}

	public int KingNearestNeighbors(KCRole playerRole) {
		int nbfound = 0;
		int nbEnnemy = 0;
		int NbNearestNeighbors = 3;
		KCPosition kingPos = getCurrentKing(playerRole);
		double NearestDist = GRID_SIZE;

		// Compute distances and store in a matrix
		Set<Double> available_distances = new HashSet<Double>();
		double[][] distances = new double[GRID_SIZE][GRID_SIZE];
		for (int i = 0; i < GRID_SIZE; i++) { 		// lines
			for (int j = 0; j < GRID_SIZE - 1; j++) { 		// columns
				if (boardGrid[i][j] == SQUARE.EMPTY || ((i == kingPos.lin) && (j == kingPos.col))) {
					distances[i][j] = Math.pow(GRID_SIZE,2);
				} else {
					double currDist = Math.sqrt(Math.pow(i - kingPos.lin, 2) + Math.pow(j - kingPos.col, 2));
					available_distances.add(currDist);
					distances[i][j] = currDist;
					if (currDist < NearestDist) {NearestDist = currDist;};
				}
			}
		}

		// Select NbNearestNeighbors nearest neighbors
		for (int i = 0; i < GRID_SIZE; i++) { 		// lines
			for (int j = 0; j < GRID_SIZE - 1; j++) { 		// columns
				if (distances[i][j] == Collections.min(available_distances)) {
					nbfound++;
					if (boardGrid[i][j] == getOppositeColor(playerRole)){nbEnnemy++;};
				}
				if (nbfound==NbNearestNeighbors) break;
			}
		if (nbfound==NbNearestNeighbors) break;
		}
		return nbEnnemy;
	}

	
	public String toString() {
		StringBuilder retstr = new StringBuilder(new String(""));
		char line = 'A';
		for (int i = GRID_SIZE - 1; i >= 0; i--) {
			retstr.append((char) ((int) line + i));
			retstr.append(" ");
			for (int j = 0; j < GRID_SIZE; j++)
				if (boardGrid[i][j] == SQUARE.EMPTY)
					retstr.append("-");
				else if (boardGrid[i][j] == SQUARE.BLUE)
					retstr.append("B");
				else // damier[i][j] == RED
					retstr.append("R");
			retstr.append("\n");
		}
		retstr.append("  ");
		for (int j = 0; j < GRID_SIZE; j++)
			retstr.append(j);
		retstr.append("\n");	
		retstr.append("BLUE KING Position: ");
		retstr.append(blueKing);
		retstr.append("\n");
		retstr.append("RED KING Position: ");
		retstr.append(redKing);
		retstr.append("\n");
		return retstr.toString();
	}

    	public int[][] getIntGrid() {
	    // WARNING: TODO, DOES NOT INDICATE KINGS POSITIONS
		int[][] intGrid = new int[GRID_SIZE][GRID_SIZE];
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				intGrid[i][j] = boardGrid[GRID_SIZE-1 - i][j].getValue();
			}
		}
		
		return intGrid;
	}

}
