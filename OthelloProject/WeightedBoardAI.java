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

    private int getPostitionValue(GameState s, Position p){
        return generatePosValue(s.getBoard().length)[p.col][p.row];

        // for (int i = 0; i < weightedBoard.length; i++){
        //     for (int j = 0; j < weightedBoard[i].length; j++){
        //         System.out.print(weightedBoard[i][j] + " ");
        //     }
        //     System.out.println();
        // }

        // System.exit(0);

        // switch (s.getBoard().length) {
        //     case 10: return POS_VALUE_10[p.col][p.row];
        //     case 8: return POS_VALUE_8[p.col][p.row];
        //     case 6: return POS_VALUE_6[p.col][p.row];
        //     case 4: return POS_VALUE_4[p.col][p.row];
        // };
        // throw new IllegalArgumentException("Board size not supported");
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
        util = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == me)
                    util += getPostitionValue(s, new Position(i, j));
                else if (board[i][j] == 3 - me)
                    util -= getPostitionValue(s, new Position(i, j));
            }
        }
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
