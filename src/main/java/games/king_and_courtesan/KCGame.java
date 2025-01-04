/*Main file to launch the game locally*/
package games.king_and_courtesan;

import java.util.ArrayList;

import iialib.games.algs.AIPlayer;
import iialib.games.algs.AbstractGame;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.algorithms.AlphaBeta;
import iialib.games.algs.algorithms.AlphaBetaTSTB;
import iialib.games.algs.algorithms.AlphaBetaRnd;
import iialib.games.algs.algorithms.RandomChoice;

public class KCGame extends AbstractGame<KCMove, KCRole, KCBoard> {
	
	public KCGame(ArrayList<AIPlayer<KCMove, KCRole, KCBoard>> players, KCBoard initialBoard) {
		super(players, initialBoard);
	}

	public static void main(String[] args) {

		KCRole roleB = KCRole.BLUE;
		KCRole roleR = KCRole.RED;

		GameAlgorithm<KCMove, KCRole, KCBoard> algB = new RandomChoice<KCMove, KCRole, KCBoard>(
				roleB);

		//GameAlgorithm<KCMove, KCRole, KCBoard> algB = new AlphaBetaTSTB<KCMove, KCRole, KCBoard>(
		//	roleB, roleR, KCHeuristics.hBlue, 6);

		GameAlgorithm<KCMove, KCRole, KCBoard> algR = new RandomChoice<KCMove, KCRole, KCBoard>(
				roleR);

		AIPlayer<KCMove, KCRole, KCBoard> playerB = new AIPlayer<KCMove, KCRole, KCBoard>(
				roleB, algB);

		AIPlayer<KCMove, KCRole, KCBoard> playerR = new AIPlayer<KCMove, KCRole, KCBoard>(
				roleR, algR);

		ArrayList<AIPlayer<KCMove, KCRole, KCBoard>> players = new ArrayList<AIPlayer<KCMove, KCRole, KCBoard>>();

		players.add(playerR); // First Player
		players.add(playerB); // Second Player

		// Setting the initial Board
		KCBoard initialBoard = new KCBoard();

		// Setting the board from a file
		// KCRandomChallenger challenger = new KCRandomChallenger();
		// challenger.setBoardFromFile("./src/main/java/games/king_and_courtesan/example_file_to_read.txt");
		// KCBoard initialBoard = challenger.currentBoard();
		//System.out.println(initialBoard);
		
		// Make a move and save the new board to a file
		// ArrayList<KCMove> moves = initialBoard.possibleMoves(roleR);
		//System.out.println(moves);
		// initialBoard.play_(moves.get(0), roleR);
		//System.out.println(initialBoard);*/
		// challenger.setBoard(initialBoard);
		// challenger.saveBoardToFile("./src/main/java/games/king_and_courtesan/test.txt");

		KCGame game = new KCGame(players, initialBoard);
		game.runGame();
	}

}
