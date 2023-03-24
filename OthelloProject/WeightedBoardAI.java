import java.lang.Math;
import java.util.stream.IntStream;
public class WeightedBoardAI implements IOthelloAI{
    private GameState clone(GameState s){
        return new GameState(s.getBoard(), s.getPlayerInTurn());
    }

    private int[] weights = {100, 15, 1, -7, -25, -40};

    public Position decideMove(GameState s){
        // if we have one or more legal moves start Alpha Beta Minimax search to find optimal move.
        if (s.legalMoves().isEmpty())
			return new Position(-1,-1);
        return ABSearch(clone(s));
        
    }
    private int cme;
    private Position ABSearch(GameState s){
        int size = s.getBoard().length;
        int me = s.getPlayerInTurn();
        // if the current weigths are invalid recompute them. we have a set of 
        // 0 weigths for the unocupied positions, another set for the current 
        // player (me) and we invert that set for the opponent (3-me).
        if (pv == null || cme != me || pv[0].length != size ){
            cme = me;
            int[][] pv_plus = generatePosValue(size);
            int[][] pv_minus = new int[size][size];
            int[][] zeroes = new int[size][size];
            for (int i = 0; i<size; i++){
                for (int j = 0; j<size; j++){
                    pv_minus[i][j] = -pv_plus[i][j];
                }
            }
            pv = new int[3][][];
            pv[0] = zeroes;
            pv[me] = pv_plus;
            pv[3-me] = pv_minus;
        }
        //then start the recursive algorithm.
        return firstMaxValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, 7, me);
    }

    private int[][] generatePosValue(int size){
        // generate a set of weigths to incentivise certain positions.
        int[][] posValue = new int[size][size];

        posValue[0][0] = weights[0];
        posValue[0][1] = weights[4];
        posValue[1][0] = weights[4];
        posValue[1][1] = weights[5];

        posValue[0][size-1] = weights[0];
        posValue[0][size-2] = weights[4];
        posValue[1][size-1] = weights[4];
        posValue[1][size-2] = weights[5];

        posValue[size-1][0] = weights[0];
        posValue[size-1][1] = weights[4];
        posValue[size-2][0] = weights[4];
        posValue[size-2][1] = weights[5];

        posValue[size-1][size-1] = weights[0];
        posValue[size-1][size-2] = weights[4];
        posValue[size-2][size-1] = weights[4];
        posValue[size-2][size-2] = weights[5];

        for (int i = 2; i < size / 2; i++){
            posValue[0][i] = weights[1] / (i - 1);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[i][0] = weights[1] / (i - 1);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[size-1][i] = weights[1] / (i - 1);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[i][size-1] = weights[1] / (i - 1);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[0][i] = weights[1] / (size - i - 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[i][0] = weights[1] / (size - i - 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[size-1][i] = weights[1] / (size - i - 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[i][size-1] = weights[1] / (size - i - 2);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[1][i] = weights[3] / (i - 1 - i % 2);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[i][1] = weights[3] / (i - 1 - i % 2);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[size-2][i] = weights[3] / (i - 1 - i % 2);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[i][size-2] = weights[3] / (i - 1 - i % 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[1][i] = weights[3] / (size - i - 2 - (i - 1) % 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[i][1] = weights[3] / (size - i - 2 - (i - 1) % 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[size-2][i] = weights[3] / (size - i - 2 - (i - 1) % 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[i][size-2] = weights[3] / (size - i - 2 - (i - 1) % 2);
        }

        for (int i = 2; i < size - 2; i++){
            for (int j = 2; j < size - 2; j++){
                posValue[i][j] = weights[2];
            }
        }
        return posValue;
    }
    
    private int[][][] pv = null;
    private int getPostitionValues(GameState s, int me){
        // compute the sum of the weighted values of the pieces on the board.
        int[][] board = s.getBoard();
        int res = 0;
        int size = board.length;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                res += pv[board[i][j]][i][j];
            }
        }
        return res;
    }
    private int utility(GameState s, int me, boolean fin){
        // compute a utility value for how good we evaluate a position to be.
        int[] counts = s.countTokens();
        int diff = counts[me - 1] - counts[2-me];
        int placedTileCount = counts[0]+counts[1];
        // if the game is over check who won.
        if (fin) {
            if (diff > 0) return 1000 - placedTileCount;
            if (diff < 0) return -1000 + placedTileCount;
            return 0;
        }
        // else return a value that incentivises controlling few but good pieces 
        // on the board.
        return -diff + getPostitionValues(s,me);
    }

    // Version of maxValue which gets called initially to utilize multi-threading
    private Position firstMaxValue(GameState s, int alpha, int beta, int count, int me){
        boolean fin = s.isFinished();
        if (fin || count <= 0) // We evaluate the game-tree branch and return if the game is over or if the cut-off has been met
            return null;
        int v = Integer.MIN_VALUE;
        Position move = null;
        var moves = s.legalMoves();
        if (moves.isEmpty())    // If there are no legal moves we add Position(-1,-1) to signify that we pass the turn
            moves.add(new Position(-1, -1));
        int[] vs = new int[moves.size()]; // array for the evaluation value of each branch
        IntStream.range(0, moves.size()).parallel().forEach(i->{ // calculate vs values in parallel
            Position a = moves.get(i);
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            vs[i] = minValue(sPrime,alpha,beta, count-1, me);
        });
        // chose move with highest evaluation
        for(int i = 0; i < vs.length; i++){
            if (vs[i]>v){
                v = vs[i];
                move = moves.get(i);
            }
        }
        return move;
    }

    // maxValue aims for the game-tree branches which will gain the player the most points that it can be sure to achieve given the opponents actions
    private int maxValue(GameState s, int alpha, int beta, int count, int me){
        boolean fin = s.isFinished();
        if (fin || count <= 0)  // We evaluate the game-tree branch and return if the game is over or if the cut-off has been met
            return utility(s,me, fin);
        int v = Integer.MIN_VALUE;
        var moves = s.legalMoves();
        if (moves.isEmpty())    // If there are no legal moves we add Position(-1,-1) to signify that we pass the turn
            moves.add(new Position(-1, -1));
        for(Position a : moves){        // Loop through the legal moves
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            int v2 = minValue(sPrime,alpha,beta, count-1, me);
            if (v2>v){  // Update alpha if minValue makes a move that can gain us more points
               v = v2;
               alpha = Math.max(alpha, v);
            }
            if (v >= beta) // Cut off branch if the opponent would never allow us to take this branch
                return v;
        }
        return v;
    }

    // minValue aims for the game-tree branches which it can be sure will gain the opponent the least amount of points
    private int minValue(GameState s, int alpha, int beta, int count, int me){
        boolean fin = s.isFinished();
        if (fin || count <= 0) // We evaluate the game-tree branch and return if the game is over or if the cut-off has been met
            return utility(s,me, fin);
        int v = Integer.MAX_VALUE;
        var moves = s.legalMoves();
        if (moves.isEmpty())    // If there are no legal moves we add Position(-1,-1) to signify that we pass the turn
            moves.add(new Position(-1, -1));
        for(Position a : moves){        // Loop through the legal moves
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            int v2 = maxValue(sPrime,alpha,beta, count-1, me);
            if (v2<v){  // Update beta if maxValue makes a move that allows us to lower the amount of points they will be able to get in the end
               v = v2;
               beta = Math.min(beta, v);
            }
            if (v <= alpha) // Cut off branch if the opponent would never allow us to take this branch
                return v;
        }
        return v;
    }

}
