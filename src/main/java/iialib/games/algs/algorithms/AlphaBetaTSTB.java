package iialib.games.algs.algorithms;

import java.util.ArrayList;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;
import iialib.games.model.Player;

/*
 * "The Sooner The Better"
 * Dans certains jeux (où les pièces peuvent revenir à leur position précédente par exemple), 
 * il est possible qu'un joueur ayant une stratégie gagnante n'atteigne jamais la fin de partie 
 * parce que MiniMax repousse dans le temps le noeud de victoire envisagé :
 * 
 *         (A)
 *         / \
 *       [B] [C]+inf
 *       /
 *     (D)
 *     / \
 *   [E] [F]+inf
 *   
 *   AMI joue A -> B (parce que plus loin dans la branche, F est un état de victoire)
 *   ENNEMI joue B -> D
 *   
 *         (D)
 *         / \
 *       [E] [F]+inf
 *       /
 *     (A)
 *     / \
 *   [B] [C]+inf
 *   
 *   AMI joue D -> E (parce que plus loin dans la branche, C est un état de victoire)
 *   ENNEMI joue E -> A
 *   etc.
 * 
 * Une façon de résoudre le problème est de différencier les valeurs associées aux fins de partie
 * pour favoriser les noeuds les moins profonds.
 */

public class AlphaBetaTSTB<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

	// Constants
	private final static int DEPTH_MAX_DEFAULT = 4;

	// Attributes
	private final Role playerMaxRole;
	private final Role playerMinRole;
	private int depthMax = DEPTH_MAX_DEFAULT;
	private IHeuristic<Board, Role> h;
	private int nbNodes;
	private int nbLeaves;

	// --------- Constructors ---------

	public AlphaBetaTSTB(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}

	public AlphaBetaTSTB(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
		this(playerMaxRole, playerMinRole, h);
		this.depthMax = depthMax;
	}

	/*
	 * IAlgo METHODS =============
	 */

	@Override
	public Move bestMove(Board board, Role playerRole) {
		System.out.println("[AlphaBeta]");

		nbNodes = 1; // root node
		nbLeaves = 0;
		int alpha = IHeuristic.MIN_VALUE;
		int beta = IHeuristic.MAX_VALUE;
		int newAlpha;

		// Compute all possible moves for maxPlayer
		ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);
		System.out.println("    * " + allMoves.size() + " possible moves");
		Move bestMove = allMoves.get(0);
		
		for (Move move : allMoves) {
			newAlpha = minMax(board.play(move, playerMaxRole), alpha, beta, 1);
			//System.out.println("Le coup " + move + " a pour valeur minimax " + newAlpha);
			if (newAlpha > alpha) {
				alpha = newAlpha;
				bestMove = move;
			}
		}
		
		System.out.println("    * " + nbNodes + " nodes explored");
		System.out.println("    * " + nbLeaves + " leaves evaluated");
		System.out.println("Best value is: " + alpha);
		return bestMove;
	}

	/*
	 * PUBLIC METHODS ==============
	 */

	public String toString() {
		return "AlphaBeta(ProfMax=" + depthMax + ")";
	}

	/*
	 * PRIVATE METHODS ===============
	 */
	private int maxMin(Board board, int alpha, int beta, int depth) {
		if (depth == depthMax || board.isGameOver()) {
			nbLeaves++;
			return h.eval(board, playerMaxRole);
		}
		else {
			nbNodes++;
			ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);
			int newVal;
			
			for (Move move : allMoves) {
				newVal = minMax(board.play(move,playerMaxRole), alpha, beta, depth+1);
				if (newVal == IHeuristic.MAX_VALUE) {
					newVal -= depth;
				}
				
				alpha = Math.max(alpha, newVal);
				
				if (alpha >= beta) {
					return beta;
				}
			}
			
			return alpha;
		}
	}

	private int minMax(Board board, int alpha, int beta, int depth) {
		if (depth == depthMax || board.isGameOver()) {
			nbLeaves++;
			return h.eval(board, playerMinRole);
		}
		else {
			nbNodes++;
			ArrayList<Move> allMoves = board.possibleMoves(playerMinRole);
			int newVal;
			
			for (Move move : allMoves) {
				newVal = maxMin(board.play(move,playerMinRole), alpha, beta, depth+1);
				if (newVal == IHeuristic.MIN_VALUE) {
					newVal += depth;
				}
				
				beta = Math.min(beta, newVal);
				
				if (alpha >= beta) {
					return alpha;
				}
			}
			
			return beta;
		}
	}

}
