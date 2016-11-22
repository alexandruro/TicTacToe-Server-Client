package sw.assignments.TicTacToe;

/**
 * Represents a game of TicTacToe
 */
public class NoughtsCrosses
{
    public static final int BLANK = 0;
    public static final int CROSS = 1;
    public static final int NOUGHT = 2;

    private boolean crossTurn;
    private int[][] board;

    private int player;
    
    private int turns;

    /**
     * Create a new game with empty board
     * @param player If 0, the player plays Noughts. If 1, the player plays Crosses
     */
    public NoughtsCrosses(int player)
    {
	turns = 0;
	this.player = player;
	board = new int[3][3];
	for (int i = 0; i < 3; i++)
	{
	    for (int j = 0; j < 3; j++)
	    {
		board[i][j] = BLANK;
	    }
	}
	crossTurn = true;
    }
    
    /**
     * Tells whether the game ended or not
     * @return True if the game ended and false if not
     */
    public boolean gameEnded()
    {
	return (turns == 9 || winner(CROSS) || winner(NOUGHT));
    }
    
 

    /**
     * Get symbol at given location
     * 
     * @param i
     *            the row
     * @param j
     *            the column
     * @return the symbol at that location
     */
    public int get(int i, int j)
    {
	return board[i][j];
    }

    /**
     * Is it cross's turn?
     * 
     * @return true if it is cross's turn, false for nought's turn
     */
    public boolean isCrossTurn()
    {
	return crossTurn;
    }
    
    /**
     * Is it the player's turn? 
     * @return true if it is the player's turn, false if it is the opponent's turn
     */
    public boolean isPlayerTurn()
    {
	if(player == 1)
	    return isCrossTurn();
	else return (!isCrossTurn());
    }
    
    /**
     * Did the player win?
     * @return true if the player won, false if the game is not over or the player lost
     */
    public boolean playerWon()
    {
	return (player == 1 && winner(CROSS)) || (player==0 && winner(NOUGHT));
    }
    
    /**
     * Did the player lose?
     * @return true if the player lost, false if the game is not over or the player won
     */
    public boolean playerLost()
    {
	return (player == 0 && winner(CROSS)) || (player==1 && winner(NOUGHT));
    }

    /**
     * Let the player whose turn it is play at a particular location
     * 
     * @param i
     *            the row
     * @param j
     *            the column
     */
    public void turn(int i, int j)
    {
	turns++;
	if (board[i][j] == BLANK)
	{
	    if (crossTurn)
	    {
		board[i][j] = CROSS;
	    } else
	    {
		board[i][j] = NOUGHT;
	    }
	    crossTurn = !crossTurn;
	} else
	{
	    throw new IllegalArgumentException("Board not empty at (" + i + ", " + j + ")");
	}
    }

    private boolean winner(int player)
    {
	return (board[0][0] == player && board[0][1] == player && board[0][2] == player)
		|| (board[1][0] == player && board[1][1] == player && board[1][2] == player)
		|| (board[2][0] == player && board[2][1] == player && board[2][2] == player)
		|| (board[0][0] == player && board[1][0] == player && board[2][0] == player)
		|| (board[0][1] == player && board[1][1] == player && board[2][1] == player)
		|| (board[0][2] == player && board[1][2] == player && board[2][2] == player)
		|| (board[0][0] == player && board[1][1] == player && board[2][2] == player)
		|| (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    /**
     * Determine who (if anyone) has won
     * 
     * @return CROSS if cross has won, NOUGHT if nought has won, otherwise BLANK
     */
    public int whoWon()
    {
	if (winner(CROSS))
	{
	    return CROSS;
	} else if (winner(NOUGHT))
	{
	    return NOUGHT;
	} else
	{
	    return BLANK;
	}
    }

    /**
     * Start a new game
     */
    public void newGame()
    {
	for (int i = 0; i < 3; i++)
	{
	    for (int j = 0; j < 3; j++)
	    {
		board[i][j] = BLANK;
	    }
	}
	crossTurn = true;
    }

}