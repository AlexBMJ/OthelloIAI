import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
        Arrays.asList(AIs).parallelStream().forEach(player1 -> {
            ArrayList<Integer> totalw = new ArrayList();
            ArrayList<Integer> totalt = new ArrayList();
            ArrayList<Integer> totall = new ArrayList();
            Arrays.asList(RandAIs).parallelStream().forEach(player2 -> {
                if (debug)
                    System.out.println("player1: " + player1.getClass().getSimpleName() + " vs player2: " + player2.getClass().getSimpleName());
                for (int size = 4; size <= 10; size+=2){

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
                        totalw.add(1);
                    }else if (x<0){
                        totall.add(1);
                    } else {
                        totalt.add(1);
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
                System.out.println("\tPlayer1 w: "+ totalw.size() + " t: " + totalt.size() + " l: " + totall.size());
                System.out.println();
            }
            w1.add(totalw.size());
            t1.add(totalt.size());
            l1.add(totall.size());
        });
        ArrayList<Integer> w2 = new ArrayList<>();
        ArrayList<Integer> t2 = new ArrayList<>();
        ArrayList<Integer> l2 = new ArrayList<>();
        Arrays.asList(AIs).parallelStream().forEach(player2 -> {
            ArrayList<Integer> totalw = new ArrayList();
            ArrayList<Integer> totalt = new ArrayList();
            ArrayList<Integer> totall = new ArrayList();
            Arrays.asList(RandAIs).parallelStream().forEach(player1 -> {
                if (debug)
                    System.out.println("player1: " + player1.getClass().getSimpleName() + " vs player2: " + player2.getClass().getSimpleName());
                for (int size = 4; size <= 10; size+=2){

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
                        totall.add(1);
                    }else if (x<0){
                        totalw.add(1);
                    } else {
                        totalt.add(1);
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
                System.out.println("\tPlayer1 w: "+ totalw.size() + " t: " + totalt.size() + " l: " + totall.size());
                System.out.println();
            }
            w2.add(totalw.size());
            t2.add(totalt.size());
            l2.add(totall.size());
        });
        t*=4;
        System.out.println("When first to move:");
        for(int i = 0; i<AIs.length; i++){
            System.out.println("\t"+AIs[i].getClass().getSimpleName() + " \n\tw%: "+ (int)(w1.get(i)/(float)t*100) + " t%: "+ (int)(t1.get(i)/(float)t*100) + " l%: "+ (int)(l1.get(i)/(float)t*100));
        }
        System.out.println("When second to move:");
        for(int i = 0; i<AIs.length; i++){
            System.out.println("\t"+AIs[i].getClass().getSimpleName() + " \n\tw%: "+ (int)(w2.get(i)/(float)t*100) + " t%: "+ (int)(t2.get(i)/(float)t*100) + " l%: "+ (int)(l2.get(i)/(float)t*100));
        }
    }
}
