import java.lang.Math;
public class SmorterAI implements IOthelloAI{
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
        return maxValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, 6).b;
    }
    
    private Integer utility(GameState s, boolean max){
        int[] counts = s.countTokens();
        int util = counts[0]-counts[1];
        int placedTileCount = counts[0]+counts[1];
        if (placedTileCount > s.getBoard()[0].length * (s.getBoard().length / 2))
            return max ? -util : util;
        return max ? util : -util;
    }

    private Pair<Integer,Position> maxValue(GameState s, int alpha, int beta, int count){
        if (s.isFinished() || count <= 0) 
            return new Pair<>(utility(s,true), null);
        int v = Integer.MIN_VALUE;
        Position move = null;
        var moves = s.legalMoves();
        if (moves.isEmpty())
            moves.add(new Position(-1, -1));
        for(Position a : moves){
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            Pair<Integer, Position> min_choice = minValue(sPrime,alpha,beta, count-1);
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

    private Pair<Integer,Position> minValue(GameState s, int alpha, int beta, int count){
        if (s.isFinished() || count <= 0) 
            return new Pair<>(utility(s,false), null);
        int v = Integer.MAX_VALUE;
        Position move = null;
        var moves = s.legalMoves();
        if (moves.isEmpty())
            moves.add(new Position(-1, -1));
        for(Position a : moves){
            GameState sPrime = clone(s);
            sPrime.insertToken(a);
            Pair<Integer, Position> max_choice = maxValue(sPrime,alpha,beta, count-1);
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
