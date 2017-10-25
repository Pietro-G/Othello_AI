/**
 *
 * @author Pietro
 */
public class FINAL_PG {

    /**
     * @param args the command line arguments
     */
    String[][] board = new String[8][8];
    public static void main(String[] args) 
    {
        Othello myGame = new Othello();
        while(myGame.endGame == false)
        {
            myGame.playerTurn();
            myGame.displayBoard();
            myGame.isGameOver();
            myGame.getPlayerMove();
        }    
    }
    
}
