package sw.assignments.TicTacToe;

/**
 * Tracks the score of a player
 */
public class Score
{
    private int played;
    private int wins;
    private int loses;
    private int draws;

    /**
     * Initialises the object
     */
    public Score()
    {
	played = 0;
	wins = 0;
	loses = 0;
	draws = 0;
    }
    
    /**
     * Adds a win to the player's record
     */
    public void addWin()
    {
	played ++;
	wins ++;
    }
    
    /**
     * Adds a loss to the player's record
     */
    public void addLoss()
    {
	played++;
	loses ++;
    }
    
    /**
     * Adds a draw to the player's record
     */
    public void addDraw()
    {
	played++;
	draws++;
    }

    /**
     * Gets the number of played games
     * @return The number of played games
     */
    public int getPlayed()
    {
	return played;
    }
    
    /**
     * Gets the number of won games
     * @return The number of wins
     */
    public int getWon()
    {
	return wins;
    }
    
    /**
     * Gets the number of lost games
     * @return The number of loses
     */
    public int getLost()
    {
	return loses;
    }
    
    /**
     * Gets the number of draws
     * @return The number of draws
     */
    public int getDraws()
    {
	return draws;
    }
    
    /**
     * Returns a string as follows: (<wins>,<draws>,<loses>)
     */
    @Override
    public String toString()
    {
	return "("+wins+","+draws+","+loses+")";
    }
    
}
