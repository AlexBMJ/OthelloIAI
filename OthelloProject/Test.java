import java.util.ArrayList;
import java.util.Random;
public class Test{
    public static void main(String[] args){
        int t = Integer.parseInt(args[0]);
        while (t-->0){
            IOthelloAI[] AIs = new IOthelloAI[]{new DumAI(), new SmortAI(), new SmorterAI(), new SmortestAI(), new AgressiveAI(), new LoserAI()};
            IOthelloAI[] RandAIs = new IOthelloAI[25];
            Random rand = new Random();
            for (int i = 0; i < RandAIs.length; i++) {
                RandAIs[i] = new RandAI(rand.nextInt());
            }
            boolean debug = false;
            IOthelloAI player;
            ArrayList<Integer> scores1 = new ArrayList<>();
            for (IOthelloAI player1 : AIs){
                int totalScore = 0;
                for (IOthelloAI player2 : RandAIs){
                    if (debug)
                        System.out.println("player1: " + player1.getClass().getSimpleName() + " vs player2: " + player2.getClass().getSimpleName());
                    for (int size = 4; size <= 10; size+=2){

                        int score = 0;
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
                            score++;
                        }else if (x<0){
                            score--;
                        }
                        if (debug){
                            if (score>0){
                                System.out.println("Player1 " + player1.getClass().getSimpleName() + " win");
                            }else if (score<0){
                                System.out.println("Player2 " + player2.getClass().getSimpleName() + " win");
                            } else {
                                System.out.println("Tie");
                            }
                        }
                        totalScore += score;
                    }
                }
                if (debug){
                    System.out.println("\tPlayer1 score: "+ totalScore);
                    System.out.println();
                }
                scores1.add(totalScore);
            }
            ArrayList<Integer> scores2 = new ArrayList<>();
            for (IOthelloAI player2 : AIs){
                int totalScore = 0;
                for (IOthelloAI player1 : RandAIs){
                    if (debug){
                        System.out.println("player1: " + player1.getClass().getSimpleName() + " vs player2: " + player2.getClass().getSimpleName());
                    }
                    for (int size = 4; size <= 10; size+=2){

                        int score = 0;
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
                            score++;
                        }else if (x<0){
                            score--;
                        }
                        if (debug){
                            if (score>0){
                                System.out.println("Player1 " + player1.getClass().getSimpleName() + " win");
                            }else if (score<0){
                                System.out.println("Player2 " + player2.getClass().getSimpleName() + " win");
                            } else {
                                System.out.println("Tie");
                            }
                        }
                        totalScore += score;
                    }
                }
                if (debug){
                    System.out.println("\tPlayer1 score: "+ totalScore);
                    System.out.println();
                }
                scores2.add(-totalScore);
            }
            System.out.println("When first to move:");
            for(int i = 0; i<AIs.length; i++){
                System.out.println("\t"+AIs[i].getClass().getSimpleName() + " score: "+ scores1.get(i));
            }
            System.out.println("When second to move:");
            for(int i = 0; i<AIs.length; i++){
                System.out.println("\t"+AIs[i].getClass().getSimpleName() + " score: "+ scores2.get(i));
            }
        }
    }
}
