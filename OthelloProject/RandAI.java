import java.util.ArrayList;
import java.util.Random;

/**
 * A simple OthelloAI-implementation. The method to decide the next move just
 * returns the first legal move that it finds. 
 * @author Mai Ajspur
 * @version 9.2.2018
 */
public class RandAI implements IOthelloAI{
    Random rand = new Random(); 
	/**
	 * Returns first legal move
	 */
	public Position decideMove(GameState s){
		ArrayList<Position> moves = s.legalMoves();
		if ( !moves.isEmpty() )
			return moves.get(rand.nextInt(moves.size()));
		else
			return new Position(-1,-1);
	}
	
}
