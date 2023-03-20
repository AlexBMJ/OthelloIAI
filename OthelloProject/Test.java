import java.util.ArrayList;
import java.util.Random;
public class Test{
    public static void main(String[] args){
        int t = args.length > 0 ? Integer.parseInt(args[0]) : 25;
        IOthelloAI[] AIs = new IOthelloAI[]{new DumAI(), new SmortAI(), new SmorterAI(), new SmortestAI(), new AgressiveAI(), new LoserAI()};
        IOthelloAI[] RandAIs = new IOthelloAI[t];
        Random rand = new Random();
        for (int i = 0; i < RandAIs.length; i++) {
            RandAIs[i] = new RandAI(rand.nextInt());
        }
        boolean debug = false;
        IOthelloAI player;
        ArrayList<Integer> w1 = new ArrayList<>();
        ArrayList<Integer> t1 = new ArrayList<>();
        ArrayList<Integer> l1 = new ArrayList<>();
        for (IOthelloAI player1 : AIs){
            int totalw = 0;
            int totalt = 0;
            int totall = 0;
            for (IOthelloAI player2 : RandAIs){
                if (debug)
                    System.out.println("player1: " + player1.getClass().getSimpleName() + " vs player2: " + player2.getClass().getSimpleName());
                for (int size = 4; size <= 10; size+=2){

                    if (debug){
                        System.out.print("\t" +size + "x" + size);
                        System.out.print(": Player1 to start => ");
                    }
                    GameState game = new GameState(size, 1);
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
                        totalw++;
                    }else if (x<0){
                        totall++;
                    } else {
                        totalt++;
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
            }
            if (debug){
                System.out.println("\tPlayer1 w: "+ totalw + " t: " + totalt + " l: " + totall);
                System.out.println();
            }
            w1.add(totalw);
            t1.add(totalt);
            l1.add(totall);
        }
        ArrayList<Integer> w2 = new ArrayList<>();
        ArrayList<Integer> t2 = new ArrayList<>();
        ArrayList<Integer> l2 = new ArrayList<>();
        for (IOthelloAI player2 : AIs){
            int totalw = 0;
            int totalt = 0;
            int totall = 0;
            for (IOthelloAI player1 : RandAIs){
                if (debug)
                    System.out.println("player1: " + player1.getClass().getSimpleName() + " vs player2: " + player2.getClass().getSimpleName());
                for (int size = 4; size <= 10; size+=2){

                    if (debug){
                        System.out.print("\t" +size + "x" + size);
                        System.out.print(": Player1 to start => ");
                    }
                    GameState game = new GameState(size, 1);
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
                        totall++;
                    }else if (x<0){
                        totalw++;
                    } else {
                        totalt++;
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
            }
            if (debug){
                System.out.println("\tPlayer1 w: "+ totalw + " t: " + totalt + " l: " + totall);
                System.out.println();
            }
            w2.add(totalw);
            t2.add(totalt);
            l2.add(totall);
        }
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
