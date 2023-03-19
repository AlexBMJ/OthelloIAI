public class Test{
    public static void main(String[] args){
        IOthelloAI[] AIs = new IOthelloAI[]{new DumAI(), new SmortAI(), new SmorterAI(), new SmortestAI()};
        IOthelloAI[] RandAIs = new IOthelloAI[100];
        for (int i = 0; i < RandAIs.length; i++) {
            RandAIs[i] = new RandAI(i);
        }
        IOthelloAI player;
        for (IOthelloAI player1 : AIs){
            for (IOthelloAI player2 : AIs){
                System.out.println("player1: " + player1.getClass().getSimpleName() + " vs player2: " + player2.getClass().getSimpleName());
                int totalScore = 0;
                for (int size = 4; size <= 16; size+=2){
                    int score = 0;
                    System.out.print("\t" +size + "x" + size);
                    GameState game = new GameState(size, 1);
                    System.out.print(": Player1 to start => ");
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
                        System.out.println("Player1 " + player1.getClass().getSimpleName() + " win");
                    }else if (x<0){
                        score--;
                        System.out.println("Player2 " + player2.getClass().getSimpleName() + " win");
                    } else {
                        System.out.println("Tie");
                    }
                    totalScore += score;
                }
                System.out.println("\tPlayer1 score: "+ totalScore);
            }
            System.out.println();
        }
    }
}
