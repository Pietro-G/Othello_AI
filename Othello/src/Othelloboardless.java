import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This interface of Othello has the exact same functions as the real-time othello class, however, the board has been removed
 * from all of the functions so that it can be controlled by the minMaxboardless class in order to predict moves in advance.
 * All of the functions are the same, aside from taking an extra parameter and having some changes in order to allow external
 * control of the game functions. 
 * @author Pietro
 */

public class Othelloboardless 
{
    ArrayList<ArrayList<Object>> legalMoves = new ArrayList<ArrayList<Object>>();
    //ArrayList<Object> changes = new ArrayList<Object>();
    ArrayList<Integer> legalNumbers = new ArrayList();
    Set<Integer> legalNumbersSet = new HashSet();
    
    private int turnCount = 1; // Basically because this class is solely used to mirror the computers moves
    public boolean player1;
    public boolean endGame = false;
    public int player1Score = 0;
    public int player2Score = 0;
    
    
    public Othelloboardless(String[][] board)
    {
        fillBoard(board);
    }
    
    public void displayBoard(String[][] board)       
    {
        System.out.print("Legal Moves: ");
        for(Integer i: legalNumbersSet)
        {
            System.out.print(i + " ");
        }
        System.out.print("\n");
        
        
        System.out.print("   ");
        for(int i = 0; i < 8; i++)
        {
            System.out.print(i + "  ");
        } 
        System.out.print("\n");
        for(int row = 0; row < board.length; row++) //Iterates over the rows of the cost matrix
        {
            System.out.print(row + " ");   
            for(int column = 0; column < board.length; column++) //Iterates over the columns of the cost matrix
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
        if(player1 == false)    
            System.out.println("Player 2, Please input the number of tile you wish to play");
        gameScore(board);        
    }
    
    public void fillBoard(String[][] board )
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
        
        legalMoves.clear();
        legalNumbers.clear();
        legalNumbersSet.clear();
    }
    
    public void getPlayerMove(String[][] board)
    {
        
        Scanner playerInput = new Scanner(System.in);
        if(player1 == true)
        {    
                System.out.println("Player 1, Please input the number of tile you wish to play");
            //else 
                //System.out.println("Player 2, Please input the number of tile you wish to play");
            int tileNumber = playerInput.nextInt();
            boolean correctMove = isLegal(tileNumber);
            while(correctMove != true)
            {
                System.out.println("Move is illegal, please re-enter the move");
                tileNumber = playerInput.nextInt();
                correctMove = isLegal(tileNumber);

            }
            playerMove(board, tileNumber);
        }
        
        else
        {
            //playerMove(board, (int) opponent.randomPick());
        }
    }
    
    public void getLegalMoves(String[][] board)
    {
        if(player1 == true)
        {
            //System.out.println("Looking for Player 1's moves...");
            for(int row = 0; row < board.length; row++) //First checks if the play has already been made
            {
                for(int column = 0; column < board.length; column++)
                {
                    if(board[row][column].equals("B"))
                    {
                        //System.out.println("Checking: row/column" + row + "/" + column);
                        possibleMoves(board, row, column, "W");
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
                        possibleMoves(board, row, column, "B");
                    }
                }    
            }
        }
        
        for (int j = 0; j < legalMoves.size(); j++)
        {
            legalNumbers.add((Integer) legalMoves.get(j).get(0));       
        }
        legalNumbersSet.addAll(legalNumbers);
    }
    
    public void possibleMoves(String[][] board, int row, int column, String op)
    {
        int temp1 = row; int temp2 = column;
        ArrayList<Object> turnables = new ArrayList(); //Contains the possible move as the first element and the turnable pieces attached to the rest of the list.
        
        if(row > 0 && board[row-1][column].equals(op) ) //Checks all possible moves above
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
                    //legalMoves.add(Integer.parseInt(board[row][column]));
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
                    //legalMoves.add(Integer.parseInt(board[row][column]));
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
                row--;
            } 
        }
        
    }        
    
    public boolean isLegal(int Tile) //Change if possible moves works.
    {
       for (int i = 0; i < legalMoves.size(); i++)
       {
           if(legalNumbers.contains(Tile))
           {
               return true;
           }    
       }
       return false;
    }
    
    public void playerMove(String[][] board, int Tile)
    {   
        for(int row = 0; row < board.length; row++) //Iterates over the rows of the cost matrix
        {
            for(int column = 0; column < board.length; column++) //Iterates over the columns of the cost matrix
            {
                if(board[row][column].equals(Integer.toString(Tile)))
                {
                    if(player1 == true)
                    {
                        board[row][column] = "B"; //The first player is always black
                    }
                    
                    else
                    {
                        board[row][column] = "W";
                    }
                }    
            }    
        }
        shiftColors(board, Tile);
    }
    
    private void shiftColors(String[][] board, int Tile)
    {
        //Change for co-ordinates.
        for (int j = 0; j < legalMoves.size(); j++)
        {
            if(legalMoves.get(j).get(0).equals(Tile))
            {
                for (int k = 1; k < legalMoves.get(j).size(); k++)
                {
                    String rc = (String) legalMoves.get(j).get(k);
                    oppositeColor(board, (Character.getNumericValue(rc.charAt(0))), Character.getNumericValue(rc.charAt(1)));
                }    
            }
        }
    }
    
    private void oppositeColor(String[][] board, int row, int column)
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
         
    public void isGameOver(String[][] board)
    {
        boolean finishCondition = true;
        for(int row = 0; row < board.length; row++) //Iterates over the rows of the cost matrix
        {
            for(int column = 0; column < board.length; column++) //Iterates over the columns of the cost matrix
            {
                if(!(board[row][column].equals("W") || board[row][column].equals("B") || legalNumbers.isEmpty()))
                {
                    finishCondition = false;
                }    
            }    
        }
        
        if(finishCondition == true)
        {
            System.out.println("Game is over"); //I'll implement a score function later.
            endGame = true;
        }
    }
    
    public void gameScore(String[][] board)
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
        //System.out.println("Player 1 Score: " + player1Score + " Player 2 Score: " + player2Score);
    } 
}
