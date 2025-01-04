/*Class implementing the positions on the board*/
package games.king_and_courtesan;

public class KCPosition {
	
    public int lin, col;

    public KCPosition(int lin, int col) {
		this.col = col;
		this.lin = lin;
    }
    
    public KCPosition(KCPosition other){
    	this.col = other.col;
    	this.lin = other.lin;
    }

    public static KCPosition valueOf(String position) {
      // Lecture des positions sur le format du fichier d'exemple
      // int lin;
      // int col;
      // if (position.charAt(0) == '(') {
      //   lin = Character.getNumericValue(position.charAt(1));
      //   col = Character.getNumericValue(position.charAt(3));
      // } else {
      int lin = (int) position.charAt(0) - (int) 'A';
      int col = Integer.parseInt(position.substring(1));
      // }
      return new KCPosition(lin, col);
    }

    public String toMove() {
      char LinToMove = (char) ((int) 'A' + lin);
      return String.valueOf(LinToMove) + String.valueOf(col);
    }

    @Override
    public String toString() {
        return "(" +  lin + "," + col + ")";
    }

    @Override
    public boolean equals(Object o){
  if (o == this) return true;
  if (o == null || this.getClass() != o.getClass())
    return false;

  KCPosition other = (KCPosition)o;

  return other.col == col && other.lin == lin;

    }

}
