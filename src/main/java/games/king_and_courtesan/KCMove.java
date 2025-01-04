/*Class implementing the interface IMove*/
package games.king_and_courtesan;

import iialib.games.model.IMove;

public class KCMove implements IMove {
	
	public final KCPosition start;
	public final KCPosition end;
    
	KCMove(KCPosition start, KCPosition end){
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
		String StartToCommunicate = start.toMove();
		String EndToCommunicate = end.toMove();
        return StartToCommunicate + "-" + EndToCommunicate;
    }

	public static KCMove valueOf(String move) {
		String[] positions = move.split("-");
		KCPosition start = KCPosition.valueOf(positions[0]);
		KCPosition end = KCPosition.valueOf(positions[1]);
		return new KCMove(start, end);
	}
	
	public boolean equals(KCMove other) {
		return start == other.start && end == other.end;
	}

}
