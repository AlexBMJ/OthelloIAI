import java.lang.Math;
import java.util.stream.IntStream;
public class WeightedBoardAI implements IOthelloAI{
    private GameState clone(GameState s){
        return new GameState(s.getBoard(), s.getPlayerInTurn());
    }

    private int[] weights = {100, 15, 1, -5, -25, -40};

    public Position decideMove(GameState s){
        if (s.legalMoves().isEmpty())
			return new Position(-1,-1);
        return ABSearch(clone(s));
        
    }
    private int cme;
    private Position ABSearch(GameState s){
        int size = s.getBoard().length;
        int me = s.getPlayerInTurn();
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
        return firstMaxValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, 6, me);
    }

    private int[][] generatePosValue(int size){
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

        // System.out.println("Position values:");
        // for (int i = 0; i < size; i++){
        //     for (int j = 0; j < size; j++){
        //         System.out.printf("%03d ", posValue[i][j]);
        //     }
        //     System.out.println();
        // }
        return posValue;
    }

    private int[][][] pv = null;
    private int getPostitionValues(GameState s, int me){
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
        int[] counts = s.countTokens();
        int diff = counts[me - 1] - counts[2-me];
        int placedTileCount = counts[0]+counts[1];
        if (fin) {
            if (diff > 0) return 1000 - placedTileCount;
            if (diff < 0) return -1000 + placedTileCount;
            return 0;
        }
        return -diff + getPostitionValues(s,me);
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
