import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
public class Test{
    public static void main(String[] args){
        int t = args.length > 0 ? Integer.parseInt(args[0]) : 25;
        IOthelloAI[] AIs = new IOthelloAI[]{new DumAI(), new SmortAI(), new SmorterAI(), new SmortestAI(), new AgressiveAI(), new LoserAI(), new WeightedBoardAI()};
        IOthelloAI[] RandAIs = new IOthelloAI[t];
        Random rand = new Random();
        for (int i = 0; i < RandAIs.length; i++) {
            RandAIs[i] = new RandAI(rand.nextInt());
        }
        boolean debug = false;
        ArrayList<Integer> w1 = new ArrayList<>();
        ArrayList<Integer> t1 = new ArrayList<>();
        ArrayList<Integer> l1 = new ArrayList<>();
        Arrays.asList(AIs).stream().forEach(player1 -> {
            AtomicInteger totalw = new AtomicInteger(0);
            AtomicInteger totalt = new AtomicInteger(0);
            AtomicInteger totall = new AtomicInteger(0);
            Arrays.asList(RandAIs).stream().forEach(player2 -> {
                if (debug)
                    System.out.println("player1: " + player1.getClass().getSimpleName() + " vs player2: " + player2.getClass().getSimpleName());
                for (int size = 4; size <= 8; size+=2){

                    if (debug){
                        System.out.print("\t" +size + "x" + size);
                        System.out.print(": Player1 to start => ");
                    }
                    GameState game = new GameState(size, 1);
                    IOthelloAI player;
                    while (!game.isFinished()){
                        if (game.getPlayerInTurn()==1)
                            player = player1;
                        else 
                            player = player2;
                        Position move = player.decideMove(game);
                        if (move.col == -1 && move.row == -1)
                            game.changePlayer();
                        else 
                            game.insertToken(move);
                    }
                    int[] counts = game.countTokens();

                    int x = counts[0]-counts[1];
                    if (x>0){
                        totalw.updateAndGet(n->n+1);
                    }else if (x<0){
                        totall.updateAndGet(n->n+1);
                    } else {
                        totalt.updateAndGet(n->n+1);
                    }
                    if (debug){
                        if (x>0){
                            System.out.println("Player1 " + player1.getClass().getSimpleName() + " win");
                        }else if (x<0){
                            System.out.println("Player2 " + player2.getClass().getSimpleName() + " win");
                        } else {
                            System.out.println("Tie");
                        }
                    }
                }
            });
            if (debug){
                System.out.println("\tPlayer1 w: "+ totalw.get() + " t: " + totalt.get() + " l: " + totall.get());
                System.out.println();
            }
            w1.add(totalw.get());
            t1.add(totalt.get());
            l1.add(totall.get());
        });
        ArrayList<Integer> w2 = new ArrayList<>();
        ArrayList<Integer> t2 = new ArrayList<>();
        ArrayList<Integer> l2 = new ArrayList<>();
        Arrays.asList(AIs).stream().forEach(player2 -> {
            AtomicInteger totalw = new AtomicInteger(0);
            AtomicInteger totalt = new AtomicInteger(0);
            AtomicInteger totall = new AtomicInteger(0);
            Arrays.asList(RandAIs).stream().forEach(player1 -> {
                if (debug)
                    System.out.println("player1: " + player1.getClass().getSimpleName() + " vs player2: " + player2.getClass().getSimpleName());
                for (int size = 4; size <= 8; size+=2){

                    if (debug){
                        System.out.print("\t" +size + "x" + size);
                        System.out.print(": Player1 to start => ");
                    }
                    GameState game = new GameState(size, 1);
                    IOthelloAI player;
                    while (!game.isFinished()){
                        if (game.getPlayerInTurn()==1)
                            player = player1;
                        else 
                            player = player2;
                        Position move = player.decideMove(game);
                        if (move.col == -1 && move.row == -1)
                            game.changePlayer();
                        else 
                            game.insertToken(move);
                    }
                    int[] counts = game.countTokens();

                    int x = counts[0]-counts[1];
                    if (x>0){
                        totall.updateAndGet(n->n+1);
                    }else if (x<0){
                        totalw.updateAndGet(n->n+1);
                    } else {
                        totalt.updateAndGet(n->n+1);
                    }
                    if (debug){
                        if (x>0){
                            System.out.println("Player1 " + player1.getClass().getSimpleName() + " win");
                        }else if (x<0){
                            System.out.println("Player2 " + player2.getClass().getSimpleName() + " win");
                        } else {
                            System.out.println("Tie");
                        }
                    }
                }
            });
            if (debug){
                System.out.println("\tPlayer1 w: "+ totalw.get() + " t: " + totalt.get() + " l: " + totall.get());
                System.out.println();
            }
            w2.add(totalw.get());
            t2.add(totalt.get());
            l2.add(totall.get());
        });
        //t*=4;
        System.out.println("When first to move:");
        for(int i = 0; i<AIs.length; i++){
            System.out.println("\t"+AIs[i].getClass().getSimpleName() + " \n\tw%: "+ (w1.get(i)/(float)t*100) + " t%: "+ (t1.get(i)/(float)t*100) + " l%: "+ (l1.get(i)/(float)t*100));
        }
        System.out.println("When second to move:");
        for(int i = 0; i<AIs.length; i++){
            System.out.println("\t"+AIs[i].getClass().getSimpleName() + " \n\tw%: "+ (w2.get(i)/(float)t*100) + " t%: "+ (t2.get(i)/(float)t*100) + " l%: "+ (l2.get(i)/(float)t*100));
        }
    }
}
