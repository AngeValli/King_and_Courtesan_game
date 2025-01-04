package iialib.games.algs.algorithms;

import java.util.ArrayList;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;
import iialib.games.model.Player;

public class MiniMax<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

	// Constants
	private final static int DEPTH_MAX_DEFAUT = 4;

	// Attributes
	private final Role playerMaxRole;

	private final Role playerMinRole;

	private int depthMax = DEPTH_MAX_DEFAUT;

	private IHeuristic<Board, Role> h;

	//
	private int nbNodes;
	private int nbLeaves;

	// --------- Constructors ---------

	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}

	//
	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
		this(playerMaxRole, playerMinRole, h);
		this.depthMax = depthMax;
	}

	/*
	 * IAlgo METHODS =============
	 */

	@Override
	// public Move bestMove(Board board, Player player, IHeuristic<Board,Player> h)
	// {
	public Move bestMove(Board board, Role playerRole) {
		System.out.println("[MiniMax]");

		this.nbNodes = 1; // root node
		this.nbLeaves = 0;
		Move bestMove = null;

		// Compute all possible moves for maxPlayer
		ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);

		if (!allMoves.isEmpty()) {
			bestMove = allMoves.get(0);
			int bestMoveHeuristicValue = IHeuristic.MIN_VALUE;
			// It is assumed that the game is not over
			for (Move move : allMoves) {
				Board newBoard = board.play(move,playerMaxRole);
				final int heuristicMove = minMax(newBoard, depthMax - 1);
				System.out.println("Le coup " + move + " a pour valeur minimax " + heuristicMove);
				if (heuristicMove > bestMoveHeuristicValue) {
					bestMoveHeuristicValue = heuristicMove;
					System.out.println("Le coup " + move + " est meilleur que " + bestMove);
					bestMove = move;
				}
			}
		}
		return bestMove;
	}

	/*
	 * PUBLIC METHODS ==============
	 */

	public String toString() {
		return "MiniMax(ProfMax=" + depthMax + ")";
	}

	/*
	 * PRIVATE METHODS ===============
	 */
	private int maxMin(Board board, int depth) {
		// maxPlayer is on turn
		if (depth == 0 || board.isGameOver()) {
			nbLeaves++;
			return h.eval(board, playerMaxRole); // ce ne devrait pas être playerMin ??? et l'inverse pour l'autre ?
		} else {
			nbNodes++;
			int maxValue = IHeuristic.MIN_VALUE;

			ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);
			for (Move move : allMoves) {
				Board newBoard = board.play(move,playerMaxRole);
				maxValue = Math.max(maxValue, minMax(newBoard, depth - 1));
				// System.out.println("maxValue for " + nPlateau + " = "+ maxValue);
			}
			// System.out.println("maxValue " + plateau + " = "+ maxValue);
			return maxValue;
		}
	}

	private int minMax(Board board, int depth) {
		// minPlayer is on turn
		if (depth == 0 || board.isGameOver()) {
			nbLeaves++;
			return h.eval(board, playerMaxRole); 
		} else {
			nbNodes++;
			int minValue = IHeuristic.MAX_VALUE;
			ArrayList<Move> allMoves = board.possibleMoves(playerMinRole);
			for (Move move : allMoves) {
				Board newBoard = board.play(move,playerMinRole);
				minValue = Math.min(minValue, maxMin(newBoard, depth - 1));
				// System.out.println("minValue for " + nPlateau + " = "+ minValue);
			}

			// System.out.println("minValue = " + minValue);
			return minValue;
		}
	}

}
