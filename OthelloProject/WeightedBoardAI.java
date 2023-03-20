import java.lang.Math;
public class WeightedBoardAI implements IOthelloAI{
    private class Pair<A, B>{
        public A a;
        public B b;
        public Pair(A a, B b){
            this.a=a;
            this.b=b;
        }
    }

    private GameState clone(GameState s){
        return new GameState(s.getBoard(), s.getPlayerInTurn());
    }

    public Position decideMove(GameState s){
        if ( !s.legalMoves().isEmpty() )
            return ABSearch(clone(s));
		else
			return new Position(-1,-1);
        
    }

    private Position ABSearch(GameState s){
        return maxValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, 4, s.getPlayerInTurn()).b;
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

    private int getPostitionValues(GameState s, int me){
        int[][] pv = null;
        int[][] board = s.getBoard();
        switch (board.length) {
            case 10: 
                pv = POS_VALUE_10;
                break;
            case 8: 
                pv = POS_VALUE_8;
                break;
            case 6: 
                pv = POS_VALUE_6;
                break;
            case 4: 
                pv = POS_VALUE_4;
                break;
            default:
                throw new IllegalArgumentException("Board size not supported");
        };
        int res = 0;
        for(int i = 0; i < pv.length; i++){
            for(int j = 0; j < pv[0].length; j++){
                if (board[i][j] == me){
                    res += pv[i][j];
                } else if (board[i][j] == 3-me){
                    res -= pv[i][j];
                }
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

    private Pair<Integer,Position> maxValue(GameState s, int alpha, int beta, int count, int me){
        boolean fin = s.isFinished();
        if (fin || count <= 0) 
            return new Pair<>(utility(s,me, fin), null);
        int v = Integer.MIN_VALUE;
        Position move = null;
        var moves = s.legalMoves();
        if (moves.isEmpty())
            moves.add(new Position(-1, -1));
        for(Position a : moves){
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            Pair<Integer, Position> min_choice = minValue(sPrime,alpha,beta, count-1, me);
            int v2 = min_choice.a;
            Position a2 = min_choice.b;
            if (v2>v){
               v = v2;
               move = a;
               alpha = Math.max(alpha, v);
            }
            if (v >= beta) 
                return new Pair<Integer,Position>(v,move);
        }
        return new Pair<Integer,Position>(v,move);
    }

    private Pair<Integer,Position> minValue(GameState s, int alpha, int beta, int count, int me){
        boolean fin = s.isFinished();
        if (fin || count <= 0) 
            return new Pair<>(utility(s,me, fin), null);
        int v = Integer.MAX_VALUE;
        Position move = null;
        var moves = s.legalMoves();
        if (moves.isEmpty())
            moves.add(new Position(-1, -1));
        for(Position a : moves){
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            Pair<Integer, Position> max_choice = maxValue(sPrime,alpha,beta, count-1, me);
            int v2 = max_choice.a;
            Position a2 = max_choice.b;
            if (v2<v){
               v = v2;
               move = a;
               alpha = Math.min(alpha, v);
            }
            if (v <= alpha) 
                return new Pair<Integer,Position>(v,move);
        }
        return new Pair<Integer,Position>(v,move);
    }

}
