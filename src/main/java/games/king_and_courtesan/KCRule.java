/*Class implementing the interface IRule used to define the rules of the game within the Referee*/
package games.king_and_courtesan;

import iialib.games.contest.IRule;

import java.util.ArrayList;

public class KCRule implements IRule {

    // Maximum waiting time for a player to compute a game move (in seconds)
	private static int TIMEOUT = 600;
	// Maximum waiting time in total for a player in a game (in seconds)
	private static int TOTAL_TIMEOUT = 600;//480;
	
	private final KCBoard board;

    public KCRule(){
        board = new KCBoard();
    }

    public KCRule(KCBoard b){
        board = b;
    }

    @Override
    public IRule play(String CommunicatedMove, String role) {
    	KCRole internalRole = KCRole.valueOf(role);
    	KCMove internalMove = KCMove.valueOf(CommunicatedMove);
        return new KCRule(board.play(internalMove, internalRole));
    }

    @Override
    public ArrayList<String> possibleMoves(String role) {
    	KCRole internalRole = KCRole.valueOf(role);
        ArrayList<KCMove> allInternalMoves = board.possibleMoves(internalRole);
        ArrayList<String> allMoves = new ArrayList<>();
        for (KCMove internalMove : allInternalMoves) {
            allMoves.add(internalMove.toString());
        }
        return allMoves;
    }

    @Override
    public ArrayList<IRule> successors(String role) {
    	KCRole internalRole = KCRole.valueOf(role);
        ArrayList<KCMove> allInternalMoves = board.possibleMoves(internalRole);
        ArrayList<IRule> ruleSuccessors =  new ArrayList<IRule>();
        for (KCMove internalMove : allInternalMoves) {
            ruleSuccessors.add(new KCRule(board.play(internalMove, internalRole)));
        }
        return ruleSuccessors;
    }

    @Override
    public boolean isValidMove(String CommunicatedMove, String role) {
    	KCRole internalRole = KCRole.valueOf(role);
    	KCMove internalMove;
    	try {
    		internalMove = KCMove.valueOf(CommunicatedMove);
    	}
    	catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		catch (IllegalArgumentException e) {
			return false;
		}
    	catch (java.lang.NullPointerException e) {
    		return false;
    	}
    	return board.isValidMove(internalMove, internalRole);
    }

    @Override
    public boolean isGameOver() {
        return board.isGameOver();
    }

    @Override
    public boolean isWinner(String role) {
    	KCRole internalRole = KCRole.valueOf(role);
    	if (internalRole == KCRole.BLUE) {
    		return board.blueVictory();
    	}
    	else {
    		return board.redVictory();
    	}
    }

    @Override
    public boolean isTie() {
        return false;
    }

    @Override
    public String getFirstRole() {
        return KCRole.RED.toString();
    }

    @Override
    public String getSecondRole() {
    	return KCRole.BLUE.toString();
    }

    @Override
    public int getTimeout() {
        return TIMEOUT;
    }

    @Override
    public int getTotalTimeout() {
        return TOTAL_TIMEOUT;
    }

    @Override
    public String toString() {
        return board.toString();
    }

	@Override
	public int[][] getBoard() {
		return board.getIntGrid();
	}

}
