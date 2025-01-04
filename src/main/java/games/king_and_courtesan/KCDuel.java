/*Main file for playing the duels between challengers, implementing the Game Server using java sockets*/
package games.king_and_courtesan;

import iialib.games.contest.GameServer;

public class KCDuel {
	
    public static void main(String[] args) {
        KCRule game = new KCRule();
        GameServer.run(args, game);
    }

}
