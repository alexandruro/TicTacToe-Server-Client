package sw.assignments.TicTacToe;

import java.util.Observable;

/**
 * A model of a TicTacToe game
 * @author John Rowe
 */
public class NoughtsCrossesModel extends Observable
{
    private NoughtsCrosses oxo;

    /**
     * Initialises the model
     * @param oxo The NoughtsCrosses object
     */
    public NoughtsCrossesModel(NoughtsCrosses oxo)
    {
	super();
	this.oxo = oxo;
    }

    /**
     * Tells whether the game ended or not
     * @return True if the game ended and false if not
     */
    public boolean gameEnded()
    {
	return oxo.gameEnded();
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
	return oxo.get(i, j);
    }

    /**
     * Is it cross's turn?
     * 
     * @return true if it is cross's turn, false for nought's turn
     */
    public boolean isCrossTurn()
    {
	return oxo.isCrossTurn();
    }

    /**
     * Is it the player's turn? 
     * @return true if it is the player's turn, false if it is the opponent's turn
     */
    public boolean isPlayerTurn()
    {
	return oxo.isPlayerTurn();
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
	oxo.turn(i, j);
	setChanged();
	notifyObservers();
    }

    /**
     * Determine who (if anyone) has won
     * 
     * @return CROSS if cross has won, NOUGHT if nought has won, oetherwise
     *         BLANK
     */
    public int whoWon()
    {
	return oxo.whoWon();
    }
    
    /**
     * Did the player win?
     * @return true if the player won, false if the game is not over or the player lost
     */
    public boolean playerWon()
    {
	return oxo.playerWon();
    }

    /**
     * Did the player lose?
     * @return true if the player lost, false if the game is not over or the player won
     */
    public boolean playerLost()
    {
	return oxo.playerLost();
    }
    
    /**
     * Start a new game
     */
    public void newGame()
    {
	oxo.newGame();
	setChanged();
	notifyObservers();
    }
}