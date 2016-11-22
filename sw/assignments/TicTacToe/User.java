package sw.assignments.TicTacToe;

/**
 * Represents a user connected to the server
 * Has a MessageQueue and a Score object
 */
public class User
{
    
    private MessageQueue queue;
    private Score score;
    
    /**
     * Initialises the user
     */
    public User()
    {
	queue = new MessageQueue();
	score = new Score();
    }
    
    /**
     * Gets the score of the player
     * @return the Score object corresponding to that user
     */
    public Score getScore()
    {
	return score;
    }
    
    /**
     * Gets the queue of the player
     * @return the MessageQueue object corresponding to that user
     */
    public MessageQueue getQueue()
    {
	return queue;
    }

}
