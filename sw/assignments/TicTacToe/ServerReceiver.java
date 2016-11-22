package sw.assignments.TicTacToe;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A class that gets messages from the corresponding client and interprets them
 */
public class ServerReceiver extends Thread
{
    private String myClientsName;
    private BufferedReader myClient;
    private ClientTable clientTable;
    private ServerSender sender;
    
    /**
     * Initialises the object
     * @param n The name of the client corresponding to the thread
     * @param c The BufferedReader corresponding to the stream of data from the client
     * @param t The ClientTable shared by the server
     * @param sender The ServerSender object corresponding to the client
     */
    public ServerReceiver(String n, BufferedReader c, ClientTable t, ServerSender sender)
    {
	myClientsName = n;
	myClient = c;
	clientTable = t;
	this.sender = sender;
    }

    @Override
    public void run()
    {
	try
	{
	    //The thread reads messages from clients as long as there is a connection (and/or the client didn't quit)
	    while (true)
	    {
		//Every message exchanged with the client is composed of 2 lines
		String recipient = myClient.readLine();
		String text = myClient.readLine();
		
		// I check that I am not working with null Strings to avoid any exception
		if (recipient != null && text != null)
		{
		    //Otherwise, the message is analysed. There are a few different cases
		    
		    //The client wants to quit
		    if (recipient.equals("quit"))
		    {
			//Closing the sender and the receiver: 
			
			//Because of the way the sender is made, the call "quit()" will not stop it if it is stuck trying to read from an empty queue 
			//This is why I am sending a message, which will unblock it and allow it to stop
			synchronized (sender)
			{
			    sender.quit();
			    sender.send(new Message("quit", "quit"));
			}
			
			//the client is removed from the table of connected people
			clientTable.remove(myClientsName);
			
			//this breaks the loop so that the receiver can stop
			break;

		    }
		    
		    //The client has a request for the server or wants to send information to it
		    else if (recipient.equals("server"))
		    {
			
			//The client finished a game and wants to send the outcome 
			if (text.contains("end"))
			    
			    // -1 means the player lost, so I add a loss to its score
			    if (text.contains("-1"))
			    {
				clientTable.getScore(myClientsName).addLoss();
			    } 
			    else
				// 0 means a draw
				if (text.contains("0"))
				    clientTable.getScore(myClientsName).addDraw();
			    else
				// otherwise, it means that the player won
				clientTable.getScore(myClientsName).addWin();
			
			//The client wants to know the scores of the players
			else if (text.equals("scores"))
			{
			    sender.send(new Message("scores", clientTable.scoresToString()));
			} 
			
			//The client wants to know which players are online, in which case I chose to send the scores list as well
			else if (text.equals("players"))
			{
			    sender.send(new Message("players", clientTable.usersToString(myClientsName)));
			    sender.send(new Message("scores", clientTable.scoresToString()));
			}
		    }
		    
		    //If the recipient is not the server it means that the player want to communicate with someone else,
		    //so the message is redirected to the recipient's queue
		    else
		    {

			Message msg = new Message(myClientsName, text);
			MessageQueue recipientsQueue = clientTable.getQueue(recipient);
			if (recipientsQueue != null)
			    recipientsQueue.offer(msg);
			else
			{
			    //If that player does not exist, an alert message is sent back to the client
			    //This usually means that a player exited or lost connection
			    
			    //System.err.println("Message for unexistent client " + recipient + ": " + text);
			    sender.send(new Message("server", "Unexistent_client"));
			}
		    }
		} 
		else
		{
		    //this will probably never happen, as the Client is working with a GUI rather than a console
		    myClient.close();
		    return;
		}
	    }
	} 
	catch (IOException e)
	{
	    //If the connection is lost the player is removed from the client list
	    clientTable.remove(myClientsName);
	    System.err.println("Something went wrong with the client " + myClientsName + " " + e.getMessage());
	    // No point in trying to close sockets. Just give up.
	}
    }
}
