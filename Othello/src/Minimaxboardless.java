import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * The following class implements the minMax algorithm approach to making the opponent of a two player game come up with a move
 * forseeing the opponents future possible move, maximizing his score and minimizing the opponent's possible score. 
 * The main function is 'minMaxing' which branches through possible future game scenarios, utilizing Othelloboardless as a class
 * to emulate future game scenarios to come up with the next move.
 * Main functions minMaxing() and deepCopyMatrix() (needed to revert moves and emulate possible scenarios)
 * @author Pietro
 */
public class Minimaxboardless 
{
    Othelloboardless futureGame; //Class used to emulate future game advancements
    Othello currentGame; //The current instance of the game being played
    String[][] previousBoard;
    
    public Minimaxboardless(Othello game)
    {
        currentGame = game; 
    }
    
    public Integer randomPick() //Just in case someone wants to play against an easy computer... 
    {
        System.out.println("Picking random move");
        Random r = new Random();
        int index = r.nextInt(currentGame.legalMoves.size());
        Iterator<Integer> iter = currentGame.legalNumbersSet.iterator();
        Integer randomMove = iter.next(); 
        System.out.println("Picked: " + randomMove);
        return randomMove;
    }
    
    public Integer minMaxing(int b) //Every unit of b equates to an increase in branching by the factor a factor of two.
    {
        String[][] dummyBoard = deepCopyMatrix(currentGame.board);
        futureGame = new Othelloboardless(dummyBoard);
        String[][] previousBoard;
        Object[] currentMoves = new Object[currentGame.legalNumbersSet.size()];
        int count = 0;//This was meant for mediating the branching level.
        int maxScore = 0; 
        int scoreDiff = 0;
        int maxMove = 0;
        int minMove = 1000;
        int moveDecided = 0;
        currentMoves = currentGame.legalNumbersSet.toArray(); //Gets list of possible moves
        
        //
        for(int i = 0; i < currentMoves.length; i++) //Iterates over all possible moves
        {    
            //A loop or condition could go here in order to increase the branching.
            futureGame.playerMove(dummyBoard, (int) currentMoves[i]); //Goes through all moves            
            futureGame.gameScore(dummyBoard); //Gets the score for every move
            scoreDiff = (futureGame.player2Score - futureGame.player1Score); //Gets the score max for every move
            maxMove = scoreDiff; //Stores the max for the current move                
            
            //This is where the minimum branches of the approach surface:
            futureGame.playerTurn(); //Gives the turn to the other player
            futureGame.getLegalMoves(dummyBoard); //Gets the other player's moves
            Object[] opponentMoves = new Object[futureGame.legalNumbersSet.size()];
            opponentMoves = futureGame.legalNumbersSet.toArray();
            previousBoard = deepCopyMatrix(dummyBoard); //Stores the player's board which contains all their moves
            for(int j = 0; j < opponentMoves.length; j++) //Iterates over all of the other player's moves.
            {
                futureGame.playerMove(dummyBoard, (int) opponentMoves[j]); //Emulates all moves the other player would make
                futureGame.gameScore(dummyBoard);//Sets the score for such move
                scoreDiff = (futureGame.player2Score - futureGame.player1Score); //Gets the score difference
                if (minMove > scoreDiff) //Looks for the lowest score difference, the one she will play
                {
                    minMove = scoreDiff; //If it's the lowest score difference, or even negative, they should play it
                }
                dummyBoard = previousBoard; //Given the move is exhausted and evaluated, re-establishes the previous scenario
            }
            int temp = maxMove - minMove; //Evaluates the current possible move with the optimal move from the opponent
            if(maxScore < temp)
            {
                moveDecided = (int) currentMoves[i]; //Stores the optimal move
            }
            dummyBoard = deepCopyMatrix(currentGame.board); //returns to the original real-time scenario to evaluate all the other moves.
        } //End For-loop that branches the Max branches once, and the Min branches once. 
        System.out.println("Picked: " + moveDecided); //Announces to the human player which move was picked by the 'computer'
        return moveDecided;
    }

    public static String[][] deepCopyMatrix(String[][] input)  //This method was necessary due to .copy() only working for primitive data types, which do not include 2D arrays.
    {
        if (input == null)//ensure the matrix has something in it.
            return null;
        String[][] result = new String[input.length][]; //Result is used to compute the copy
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone(); //Copies all of the primitive elements inside a 2D array
        }
        return result; //Returns a copy that does not share any references with the original 2D array/
    }
    
}
