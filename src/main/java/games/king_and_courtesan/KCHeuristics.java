/*Class implementing the heuristics for AlphaBeta algorithm and its variants*/
package games.king_and_courtesan;

import iialib.games.algs.IHeuristic;

public class KCHeuristics {
    public static IHeuristic<KCBoard, KCRole> hBlue = (board,role) -> {
        if (board.blueVictory()) {
            return IHeuristic.MAX_VALUE;
        }
        if (board.redVictory()) {
            return IHeuristic.MIN_VALUE;
        }
        return 100*(board.getCurrentNbPlayers(KCRole.RED)-board.getCurrentNbPlayers(KCRole.BLUE)) + 10*board.KingDistToOppositeCamp(role) + 5*board.KingNearestNeighbors(role);
    };

    public static IHeuristic<KCBoard, KCRole> hRed = (board,role) -> {
        if (board.redVictory()) {
            return IHeuristic.MAX_VALUE;
        }
        if (board.blueVictory()) {
            return IHeuristic.MIN_VALUE;
        }
        return 100*(board.getCurrentNbPlayers(KCRole.BLUE)-board.getCurrentNbPlayers(KCRole.RED)) + 10*board.KingDistToOppositeCamp(role) + 5*board.KingNearestNeighbors(role);
    };

}

