/*Class implementing a challenger to play King and Courtesan game randomly*/
package games.king_and_courtesan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Set;

import iialib.games.algs.AIPlayer;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.algorithms.RandomChoice;
import iialib.games.contest.NameGenerator;
import iialib.games.model.IChallenger;

public class KCRandomChallenger implements IChallenger {
	
	private final String name;
	private KCRole roleFriend;
	private KCRole roleEnemy;
	private AIPlayer<KCMove, KCRole, KCBoard> player;
	private KCBoard board;

	public KCRandomChallenger() {
		name = NameGenerator.pickName();
		board = new KCBoard();
	}
	
	@Override
	public String teamName() {
		return name;
	}

	@Override
	public void setRole(String role) {
		roleFriend = KCRole.valueOf(role);
		roleEnemy = (roleFriend == KCRole.BLUE ? KCRole.RED : KCRole.BLUE);
		
		GameAlgorithm<KCMove, KCRole, KCBoard> algFriend = new RandomChoice<KCMove, KCRole, KCBoard>(roleFriend);
		
		player = new AIPlayer<KCMove, KCRole, KCBoard>(roleFriend, algFriend);
	}

	@Override
	public void iPlay(String move) {
		KCMove internalMove = KCMove.valueOf(move);
		board.play_(internalMove, roleFriend);
	}

	@Override
	public void otherPlay(String move) {
		KCMove internalMove = KCMove.valueOf(move);
		board.play_(internalMove, roleEnemy);
	}

	@Override
	public String bestMove() {
		KCMove internalMove = player.bestMove(board);
		return internalMove.toString();
	}

	@Override
	public String victory() {
		System.out.println("Victory !");
		return "Youpi !";
	}

	@Override
	public String defeat() {
		System.out.println("Defeat !");
		return "Snif...";
	}

	@Override
	public String tie() {
		System.out.println("Tie !");
		return "Ouf !";
	}

	// @Override
	// public String timeout() {
	// 	System.out.println("Timeout !");
	// 	return "Timeout !";
	// }

	// @Override
	// public String illegal_move() {
	// 	System.out.println("Illegal_move !");
	// 	return "Illegal_move !";
	// }
	
	@Override
	public void setBoardFromFile(String fileName) {
		BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            StringBuilder res = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                res.append(line);
                res.append("\n");
                line = reader.readLine();
            }
            reader.close();
            this.board = new KCBoard(res.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public Set<String> possibleMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBoard() {
		return this.board.toString();
	}

	public KCBoard currentBoard() {
		return this.board;
	}

	public void setBoard(KCBoard board) {
		this.board = board;
	}

	public void saveBoardToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(this.board.toString());
            writer.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

