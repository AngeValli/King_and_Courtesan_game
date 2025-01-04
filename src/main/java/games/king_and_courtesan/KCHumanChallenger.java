/*Class implementing a challenger with movements inputed by the user turn-by-turn*/
package games.king_and_courtesan;

import java.util.Scanner;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import iialib.games.contest.NameGenerator;
import iialib.games.model.IChallenger;

public class KCHumanChallenger implements IChallenger {
	
	private final String name;
	private KCRole roleFriend;
	private KCRole roleEnemy;
	private KCBoard board;

	public KCHumanChallenger() {
		name = NameGenerator.pickName();
		board = new KCBoard();
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
	public String teamName() {
		return "Humain - " + name;
	}

	@Override
	public void setRole(String role) {
		roleFriend = KCRole.valueOf(role);
		roleEnemy = (roleFriend == KCRole.BLUE ? KCRole.RED : KCRole.BLUE);
	}

	@Override
	public String bestMove() {
		System.out.println(board);
		System.out.println("\n :");
		System.out.println("Entrer un coup :");
		String entreeClavier = new Scanner(System.in).nextLine();
		return entreeClavier;
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

	@Override
	public Set<String> possibleMoves() {
		// TODO Auto-generated method stub
		return null;
	}

}
