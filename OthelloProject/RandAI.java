import java.util.ArrayList;
import java.util.Random;

/**
 * A simple OthelloAI-implementation. The method to decide the next move just
 * returns a random legal move that it finds. 
 */
public class RandAI implements IOthelloAI{
    private Random rand;

    public RandAI(){
        rand = new Random();
    }

    public RandAI(int seed){
        rand = new Random(seed);
    }

	/**
	 * Returns random legal move
	 */
	public Position decideMove(GameState s){
		ArrayList<Position> moves = s.legalMoves();
		if ( !moves.isEmpty() )
			return moves.get(rand.nextInt(moves.size()));
		else
			return new Position(-1,-1);
	}
	
}
