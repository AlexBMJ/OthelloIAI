import java.lang.Math;
import java.util.stream.IntStream;
public class WeightedBoardAI implements IOthelloAI{
    private GameState clone(GameState s){
        return new GameState(s.getBoard(), s.getPlayerInTurn());
    }

    public Position decideMove(GameState s){
        int size = s.getBoard().length;
        pv_plus = generatePosValue(size);
        pv_minus = new int[size][size];
        zeroes = new int[size][size];
        for (int i = 0; i<size; i++){
            for (int j = 0; j<size; j++){
                pv_minus[i][j] = -pv_plus[i][j];
            }
        }
        if ( !s.legalMoves().isEmpty() )
            return ABSearch(clone(s));
		else
			return new Position(-1,-1);
        
    }

    private Position ABSearch(GameState s){
        return firstMaxValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, 6, s.getPlayerInTurn());
    }

    private int[][] generatePosValue(int size){
        int[][] posValue = new int[size][size];

        posValue[0][0] = 100;
        posValue[0][1] = -20;
        posValue[1][0] = -20;
        posValue[1][1] = -50;

        posValue[0][size-1] = 100;
        posValue[0][size-2] = -20;
        posValue[1][size-1] = -20;
        posValue[1][size-2] = -50;

        posValue[size-1][0] = 100;
        posValue[size-1][1] = -20;
        posValue[size-2][0] = -20;
        posValue[size-2][1] = -50;

        posValue[size-1][size-1] = 100;
        posValue[size-1][size-2] = -20;
        posValue[size-2][size-1] = -20;
        posValue[size-2][size-2] = -50;

        for (int i = 2; i < size / 2; i++){
            posValue[0][i] = 10 / (i - 1);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[i][0] = 10 / (i - 1);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[size-1][i] = 10 / (i - 1);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[i][size-1] = 10 / (i - 1);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[0][i] = 10 / (size - i - 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[i][0] = 10 / (size - i - 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[size-1][i] = 10 / (size - i - 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[i][size-1] = 10 / (size - i - 2);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[1][i] = -2 / (i - 1 - i % 2);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[i][1] = -2 / (i - 1 - i % 2);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[size-2][i] = -2 / (i - 1 - i % 2);
        }

        for (int i = 2; i < size / 2; i++){
            posValue[i][size-2] = -2 / (i - 1 - i % 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[1][i] = -2 / (size - i - 2 - (i - 1) % 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[i][1] = -2 / (size - i - 2 - (i - 1) % 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[size-2][i] = -2 / (size - i - 2 - (i - 1) % 2);
        }

        for (int i = size - 3; i >= size / 2; i--){
            posValue[i][size-2] = -2 / (size - i - 2 - (i - 1) % 2);
        }

        for (int i = 2; i < size - 2; i++){
            for (int j = 2; j < size - 2; j++){
                posValue[i][j] = 1;
            }
        }


        return posValue;
    }

    private final int[][] POS_VALUE_10 = {
        {100, -20, 10, 5, 2, 2, 5, 10, -20, 100},
        {-20, -50, -2, -2, -1, -1, -2, -2, -50, -20},
        {10, -2, 1, 1, 1, 1, 1, 1, -2, 10},
        {5, -2, 1, 1, 1, 1, 1, 1, -2, 5},
        {2, -1, 1, 1, 0, 0, 1, 1, -1, 2},
        {2, -1, 1, 1, 0, 0, 1, 1, -1, 2},
        {5, -2, 1, 1, 1, 1, 1, 1, -2, 5},
        {10, -2, 1, 1, 1, 1, 1, 1, -2, 10},
        {-20, -50, -2, -2, -1, -1, -2, -2, -50, -20},
        {100, -20, 10, 5, 2, 2, 5, 10, -20, 100}
    };

    private final int[][] POS_VALUE_8 = {
        {100, -20, 10, 5, 5, 10, -20, 100},
        {-20, -50, -2, -2, -2, -2, -50, -20},
        {10, -2, 1, 1, 1, 1, -2, 10},
        {5, -2, 1, 1, 1, 1, -2, 5},
        {5, -2, 1, 1, 1, 1, -2, 5},
        {10, -2, 1, 1, 1, 1, -2, 10},
        {-20, -50, -2, -2, -2, -2, -50, -20},
        {100, -20, 10, 5, 5, 10, -20, 100}
    };

    private final int[][] POS_VALUE_6 = {
        {100, -20, 10, 5, -20, 100},
        {-20, -50, -2, -2, -50, -20},
        {10, -2, 1, 1, -2, 10},
        {5, -2, 1, 1, -2, 5},
        {-20, -50, -2, -2, -50, -20},
        {100, -20, 10, 5, -20, 100}
    };

    private final int[][] POS_VALUE_4 = {
        {100, -20, -20, 100},
        {-20, -50, -50, -20},
        {-20, -50, -50, -20},
        {100, -20, -20, 100}
    };
    private int[][] pv_plus;
    private int[][] pv_minus;
    private int[][] zeroes;

    private int getPostitionValues(GameState s, int me){
        int[][][] pv = new int[3][][];
        pv[0] = zeroes;
        pv[me] = pv_plus;
        pv[3-me] = pv_minus;
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
    
    private Integer utility(GameState s, int me, boolean fin){
        int[] counts = s.countTokens();
        int util = s.getPlayerInTurn()==1? counts[0]-counts[1]:counts[1]-counts[0];
        int placedTileCount = counts[0]+counts[1];
        if (s.getPlayerInTurn() != me)
            util = -util;
        if (fin) {
            if (util > 0) return 1000 - placedTileCount;
            if (util < 0) return -1000 + placedTileCount;
            return 0;
        }
        var board = s.getBoard();
        util = getPostitionValues(s,me);
        if (counts[me - 1] > s.getBoard()[0].length * (s.getBoard().length / 4))
            util -= 100;
        if (s.getPlayerInTurn() != me)
            util = -util;
        return util;
    }

    private Position firstMaxValue(GameState s, int alpha, int beta, int count, int me){
        boolean fin = s.isFinished();
        if (fin || count <= 0) 
            return null;
        int v = Integer.MIN_VALUE;
        Position move = null;
        var moves = s.legalMoves();
        if (moves.isEmpty())
            moves.add(new Position(-1, -1));
        int[] vs = new int[moves.size()];
        IntStream.range(0, moves.size()).parallel().forEach(i->{
            Position a = moves.get(i);
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            vs[i] = minValue(sPrime,alpha,beta, count-1, me);
        });
        for(int i = 0; i < vs.length; i++){
            if (vs[i]>v){
                v = vs[i];
                move = moves.get(i);
            }
        }
        return move;
    }
    private int maxValue(GameState s, int alpha, int beta, int count, int me){
        boolean fin = s.isFinished();
        if (fin || count <= 0) 
            return utility(s,me, fin);
        int v = Integer.MIN_VALUE;
        var moves = s.legalMoves();
        if (moves.isEmpty())
            moves.add(new Position(-1, -1));
        for(Position a : moves){
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            int v2 = minValue(sPrime,alpha,beta, count-1, me);
            if (v2>v){
               v = v2;
               alpha = Math.max(alpha, v);
            }
            if (v >= beta) 
                return v;
        }
        return v;
    }

    private int minValue(GameState s, int alpha, int beta, int count, int me){
        boolean fin = s.isFinished();
        if (fin || count <= 0) 
            return utility(s,me, fin);
        int v = Integer.MAX_VALUE;
        var moves = s.legalMoves();
        if (moves.isEmpty())
            moves.add(new Position(-1, -1));
        for(Position a : moves){
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            int v2 = maxValue(sPrime,alpha,beta, count-1, me);
            if (v2<v){
               v = v2;
               alpha = Math.min(alpha, v);
            }
            if (v <= alpha) 
                return v;
        }
        return v;
    }

}
