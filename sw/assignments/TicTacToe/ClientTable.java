package sw.assignments.TicTacToe;

import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * The table of users connected to the server
 */
public class ClientTable
{

    private ConcurrentMap<String, User> clientTable = new ConcurrentHashMap<String, User>();

    /**
     * Tries to add a player to the table, return true if it could, false if the username is taken
     * @param nickname The nickname of the player
     * @return Whether the operation was successful
     */
    public boolean add(String nickname)
    {
	if (clientTable.containsKey(nickname))
	    return false;
	else
	{
	    clientTable.put(nickname, new User());
	    return true;
	}
    }

    /**
     * Gets the Queue of a user or null if the user is not in the table
     * @param nickname The nickname of the user
     * @return The queue
     */
    public MessageQueue getQueue(String nickname)
    {
	if(clientTable.get(nickname)==null)
	    return null;
	return clientTable.get(nickname).getQueue();
    }

    /**
     * Gets the Score of a user or null if the user is not in the table
     * @param nickname The nickname of the user
     * @return The Score object
     */
    public Score getScore(String nickname)
    {
	if(clientTable.get(nickname)==null)
	    return null;
	return clientTable.get(nickname).getScore();
    }

    /**
     * Gets a user in the table or null if the user is not connected
     * @param nickname The nickname of the user
     * @return The User object
     */
    public User getUser(String nickname)
    {
	return clientTable.get(nickname);
    }

    /**
     * Removes a player from the table
     * @param nickname The nickname of the player
     */
    public void remove(String nickname)
    {
	clientTable.remove(nickname);
    }

    /**
     * Gets a list of the players and their scores
     * @return A string with players separated by ";"
     */
    public String scoresToString()
    {
	String string = "";
	synchronized (clientTable)
	{
	    for (Entry<String, User> entry : clientTable.entrySet())
		string += entry.getKey() + " -> " + entry.getValue().getScore().toString() + ";";
	}
	return string;
    }

    /**
     * Gets a list of the players 
     * @return A string with players separated by ";"
     */
    public String usersToString(String excluded)
    {
	String string = "";
	synchronized (clientTable)
	{
	    for (Entry<String, User> entry : clientTable.entrySet())
	    {
		if (entry.getKey() != excluded)
		    string += entry.getKey() + ";";
	    }
	}
	return string;
    }

}
