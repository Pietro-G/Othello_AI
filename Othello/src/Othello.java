import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Following rules of the game where used: http://www.hannu.se/games/othello/rules.htm. Othello is the class utilized to
 * make sure that the game mechanics are functioning and to play the game in accordance to the rules. It has some main classes
 * such as displayBoard(), isGameOver(), isLegalMove(), isPossibleMove(), playerTurn() which are all essential to the flow of
 * the game. This class is intended to be the main interface and moreover the real-time frame of the game. 
 * @author Pietro
 */

public class Othello 
{
    String[][] board = new String[8][8];
    ArrayList<ArrayList<Object>> legalMoves = new ArrayList<ArrayList<Object>>();
    //ArrayList<Object> changes = new ArrayList<Object>();
    ArrayList<Integer> legalNumbers = new ArrayList();
    Set<Integer> legalNumbersSet = new HashSet(); 
    
    private int turnCount = 0;
    private boolean player1;
    public boolean endGame = false;
    public int player1Score = 0;
    public int player2Score = 0;
    
    
    public Othello() //Obtains the initial othello configuration
    {
       fillBoard(); 
    }
    
    public void displayBoard() 
    {
        getLegalMoves(); //Updates the legal moves to tell to the player
        gameScore(); //Updates the score according to the previous moves by the player
        System.out.print("Legal Moves: ");
        for (int j = 0; j < legalMoves.size(); j++) 
        {
            legalNumbers.add((Integer) legalMoves.get(j).get(0));//Legal moves contains all the possible moves and the pieces that would be flipped if the move was done
        }
        
        legalNumbersSet.addAll(legalNumbers); //Removes any duplicates (as a move can be legal for many reasons)
        for(Integer i: legalNumbersSet)
        {
            System.out.print(i + " ");
        }
        
        
        System.out.print("\n"); //Used simply for aesthetic purposes
        
        
        System.out.print("   ");
        for(int i = 0; i < 8; i++)
        {
            System.out.print(i + "  ");
        } 
        System.out.print("\n");
        for(int row = 0; row < board.length; row++) //Iterates over the rows of the cost matrix
        {
            System.out.print(row + " ");   
            for(int column = 0; column < board.length; column++) //Iterates over the columns of the cost matrix, produces a display on the command line
            {
                if(board[row][column].equals("B"))
                    System.out.print("[B]");
                else if(board[row][column].equals("W"))
                    System.out.print("[W]");
                else if(legalNumbersSet.contains(Integer.parseInt(board[row][column])))
                    System.out.print("[X]"); 
                else if(!(board[row][column].equals("B") || board[row][column].equals("W")))
                    System.out.print("[ ]");
            }
            System.out.print("\n");   
        }
    }
    
    public void fillBoard()
    {
        int spaceNumb = 0;
        for(int row = 0; row < board.length; row++) //Iterates over the rows of the cost matrix
        {
            for(int column = 0; column < board.length; column++) //Iterates over the columns of the cost matrix
            {
                board[row][column] = Integer.toString(spaceNumb);//Stores every value iterated on the 2D array
                spaceNumb++;
            }    
        }
        
        board[3][3] = "W"; board[3][4] = "B"; board[4][4] = "W"; board[4][3] = "B"; //This is always the initial board configuration according to the rules of the game
    }
    
    public void playerTurn() //Function that returns true if it's player 1's turn and false if it's player 2s turn. 
    {
        if(turnCount%2 == 0)
        {
            turnCount++;
            player1 = true;
        }
        
        else
        {
            turnCount++;
            player1 = false;
        }
        
        //The legal moves for each player are different, thus need to be re-computed.
        legalMoves.clear(); 
        legalNumbers.clear();
        legalNumbersSet.clear();
    }
    
    public void getPlayerMove()//This function basically has no parameters and is called to get the human's moves or return the computer's moves.
    {
        Scanner playerInput = new Scanner(System.in);
        Minimaxboardless opponent = new Minimaxboardless(this); 
        if(player1 == true)
        {    
                System.out.println("Player 1, Please input the number of tile you wish to play");
            //else 
                //System.out.println("Player 2, Please input the number of tile you wish to play");
            int tileNumber = playerInput.nextInt();
            boolean correctMove = isLegal(tileNumber);
            while(correctMove != true)
            {
                System.out.println("Move is illegal, please re-enter the move"); //Ensure that the player enters valid moves
                tileNumber = playerInput.nextInt();
                correctMove = isLegal(tileNumber); 

            }
            playerMove(tileNumber);
        }
        
        else
        {
            playerMove(opponent.minMaxing(1));//The parameter is not used, but it could be expanded to branch further (each unit would be equivalent to two branches)
            //playerMove((int) opponent.randomPick());
            
        }
    }
    
    public void getLegalMoves() //Get legal moves takes no parameters and collects all the player's tokens to evaluate the possible legal moves.
    {
        if(player1 == true)
        {
            //System.out.println("Looking for Player 1's moves...");
            for(int row = 0; row < board.length; row++) 
            {
                for(int column = 0; column < board.length; column++)
                {
                    if(board[row][column].equals("B"))
                    {
                        //System.out.println("Checking: row/column" + row + "/" + column);
                        possibleMoves(row, column, "W"); 
                    }
                }    
            }
        }
        
        else
        {
            //System.out.println("Looking for Player 2's moves...");
            for(int row = 0; row < board.length; row++) //First checks if the play has already been made
            {
                for(int column = 0; column < board.length; column++)
                {
                    if(board[row][column].equals("W"))
                    {
                        //System.out.println("Checking: row/column" + row + "/" + column);
                        possibleMoves(row, column, "B");
                    }
                }    
            }
        }
    }
    
    public void possibleMoves(int row, int column, String op) //Possible moves might contain a bug, however it generally exhausts all possible moves in all general directions: left, right, up down, northwest, northeast, sothwest and southeast.
    {
        int temp1 = row; int temp2 = column;
        ArrayList<Object> turnables = new ArrayList(); //Contains the possible move as the first element and the turnable pieces attached to the rest of the list.
        
        if(row > 0 && board[row-1][column].equals(op)) //Checks all possible moves above
        {
            boolean spaceFound = false;
            while(row >= 0 && spaceFound == false)
            {
                if (!board[row][column].equals("B") && !board[row][column].equals("W"))
                {
                    turnables.add(0, Integer.parseInt(board[row][column]));
                    legalMoves.add((ArrayList<Object>) turnables.clone());
                    spaceFound = true;
                }
                
                if(board[row][column].equals(op))
                {
                    turnables.add(Integer.toString(row) + Integer.toString(column));
                }   
                row--;
            }    
        }
        
        row = temp1; column = temp2; turnables.clear();
        if(column > 0 && board[row][column-1].equals(op)) //Checks all possible moves to the left
        {
            boolean spaceFound = false;
            while(column >= 0 && spaceFound == false)
            {
                if (!board[row][column].equals("B") && !board[row][column].equals("W"))
                {
                    //legalMoves.add(Integer.parseInt(board[row][column]));
                    turnables.add(0, Integer.parseInt(board[row][column]));
                    legalMoves.add((ArrayList<Object>) turnables.clone());
                    spaceFound = true;
                }
                
                if(board[row][column].equals(op))
                {
                    turnables.add(Integer.toString(row) + Integer.toString(column));;
                }
                column--;
            }    
        }
        
        row = temp1; column = temp2; turnables.clear();
        if(row < 7 && board[row+1][column].equals(op)) //Checks all possible moves below
        {
            boolean spaceFound = false;
            while(row <= 7 && spaceFound == false)
            {
                if (!board[row][column].equals("B") && !board[row][column].equals("W"))
                {
                    turnables.add(0, Integer.parseInt(board[row][column]));
                    legalMoves.add((ArrayList<Object>) turnables.clone());
                    spaceFound = true;
                }
                if(board[row][column].equals(op))
                {
                    turnables.add(Integer.toString(row) + Integer.toString(column));;
                }
                row++;
            } 
        }
        
        row = temp1; column = temp2; turnables.clear();
        if(column < 7 && board[row][column+1].equals(op)) //Checks all possible moves to the right
        {
            boolean spaceFound = false;
            while(column <= 7 && spaceFound == false)
            {
                if (!board[row][column].equals("B") && !board[row][column].equals("W"))
                {
                    turnables.add(0, Integer.parseInt(board[row][column]));
                    legalMoves.add((ArrayList<Object>) turnables.clone());
                    spaceFound = true;
                }
                if(board[row][column].equals(op))
                {
                    turnables.add(Integer.toString(row) + Integer.toString(column));
                }
                column++;
            } 
        }
        
        row = temp1; column = temp2; turnables.clear(); 
        if((column > 0 && row > 0) && board[row-1][column-1].equals(op)) //Checks north-west diagonal
        {
            boolean spaceFound = false;
            while((column >= 0 && row >= 0) && spaceFound == false)
            {
                if (!board[row][column].equals("B") && !board[row][column].equals("W"))
                {
                    turnables.add(0, Integer.parseInt(board[row][column]));
                    legalMoves.add((ArrayList<Object>) turnables.clone());
                    spaceFound = true;
                }
                
                if(board[row][column].equals(op))
                {
                    turnables.add(Integer.toString(row) + Integer.toString(column));
                }
                column--;
                row--;
            } 
        }
        
        row = temp1; column = temp2; turnables.clear();
        if((column > 0 && row < 7) && board[row+1][column-1].equals(op)) //Checks south-west diagonal
        {
            boolean spaceFound = false;
            while((column >= 0 && row <= 7) && spaceFound == false)
            {
                if (!board[row][column].equals("B") && !board[row][column].equals("W"))
                {
                    turnables.add(0, Integer.parseInt(board[row][column]));
                    legalMoves.add((ArrayList<Object>) turnables.clone());
                    spaceFound = true;
                }
                
                if(board[row][column].equals(op))
                {
                    turnables.add(Integer.toString(row) + Integer.toString(column));
                }
                column--;
                row++;
            } 
        }
        
        row = temp1; column = temp2; turnables.clear();
        if((column > 7 && row < 7) && board[row+1][column+1].equals(op)) //Checks south-east diagonal
        {
            boolean spaceFound = false;
            while((column >= 7 && row <= 7) && spaceFound == false)
            {
                if (!board[row][column].equals("B") && !board[row][column].equals("W"))
                {
                    //legalMoves.add(Integer.parseInt(board[row][column]));
                    turnables.add(0, Integer.parseInt(board[row][column]));
                    legalMoves.add((ArrayList<Object>) turnables.clone());
                    spaceFound = true;
                }
                
                if(board[row][column].equals(op))
                {
                    turnables.add(Integer.toString(row) + Integer.toString(column));
                }
                column++;
                row++;
            } 
        }
        
        row = temp1; column = temp2; turnables.clear();
        if((column > 7 && row < 0) && board[row-1][column+1].equals(op)) //Checks north-east diagonal
        {
            boolean spaceFound = false;
            while((column >= 7 && row <= 0) && spaceFound == false)
            {
                if (!board[row][column].equals("B") && !board[row][column].equals("W"))
                {
                    turnables.add(0, Integer.parseInt(board[row][column]));
                    legalMoves.add((ArrayList<Object>) turnables.clone());
                    spaceFound = true;
                }
                
                if(board[row][column].equals(op))
                {
                    turnables.add(Integer.toString(row) + Integer.toString(column));
                }
                column++;
                row--;
            } 
        }
        
    }        
    
    public boolean isLegal(int Tile) //A function that takes the potential play as a parameter and verifies it is according to the games rules, returning a boolean if it is or isn't.
    {
       for (int i = 0; i < legalMoves.size(); i++)
       {
           if(legalNumbersSet.contains(Tile))
           {
               return true;
           }    
       }
       return false;
    }
    
    public void playerMove(int Tile) //This function receives an integer which can be filled with either color and looks it up in the grid linearly and changes it to the player's token.
    {   
        for(int row = 0; row < board.length; row++) //Iterates over the rows of the cost matrix
        {
            for(int column = 0; column < board.length; column++) //Iterates over the columns of the cost matrix
            {
                if(board[row][column].equals(Integer.toString(Tile)))
                {
                    if(player1 == true)
                    {
                        board[row][column] = "B"; //The first player is always black according to the rules.
                    }
                    
                    else
                    {
                        board[row][column] = "W";
                    }
                }    
            }    
        }
        shiftColors(Tile);
    }
    
    private void shiftColors(int Tile) //When tokens are sorrounded by the opposite player's colors this function is called for 'flipping' such tokens. 
    {
        
        for (int j = 0; j < legalMoves.size(); j++)//Indices are stored behind every legalMove.
        {
            if(legalMoves.get(j).get(0).equals(Tile))
            {
                for (int k = 1; k < legalMoves.get(j).size(); k++)
                {
                    String rc = (String) legalMoves.get(j).get(k);
                    oppositeColor((Character.getNumericValue(rc.charAt(0))), Character.getNumericValue(rc.charAt(1))); //It is called on the indices.
                }    
            }
        }
    }
    
    private void oppositeColor(int row, int column) //his is a helper function to 'shiftColors' which simply changes a color in the board to the opposite, taking the board's coordinates as parameters.
    {
        if (board[row][column].equals("B"))
        {
            board[row][column] = "W";
        }
        else
        {
            board[row][column] = "B";
        }    
    }
         
    public void isGameOver() //A function that periodically changes if the board is filled or legal moves are exahusted.
    {
        boolean finishCondition = true;
        for(int row = 0; row < board.length; row++) //Iterates over the rows of the cost matrix
        {
            for(int column = 0; column < board.length; column++) //Iterates over the columns of the cost matrix
            {
                if(!(board[row][column].equals("W") || board[row][column].equals("B") || legalNumbersSet.isEmpty()))
                {
                    finishCondition = false;
                }    
            }    
        }
        
        if(finishCondition == true)
        {
            System.out.println("Game is over"); 
            endGame = true;
        }
    }
    
    public void gameScore() //Function used to update the score for each player.
    {
        player1Score = 0;
        player2Score = 0;
        for(int row = 0; row < board.length; row++) //Iterates over the rows of the cost matrix
        {
            for(int column = 0; column < board.length; column++) //Iterates over the columns of the cost matrix
            {
                if(board[row][column].equals("B"))
                    player1Score++;                 
                
                if(board[row][column].equals("W"))
                    player2Score++; 
                
            }    
        }
        System.out.println("Player 1 Score: " + player1Score + " Player 2 Score: " + player2Score);
    }        
    
}
