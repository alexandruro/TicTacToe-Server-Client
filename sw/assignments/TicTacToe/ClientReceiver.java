package sw.assignments.TicTacToe;

import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JOptionPane;

// Gets messages from other clients via the server (by the
// ServerSender thread).

/**
 * A class that Gets messages from other clients via the server or the server itself 
 * (by the ServerSender thread).
 */
public class ClientReceiver extends Thread
{

    private BufferedReader server;
    private Client client;

    /**
     * Initialises the receiver
     * @param server The stream from the server
     * @param client The client
     */
    ClientReceiver(BufferedReader server, Client client)
    {
	this.server = server;
	this.client = client;
    }

    @Override
    public void run()
    {
	try
	{
	    //The receiver tries to get messages forever (or until the client wants to quit)
	    while (true)
	    {
		//A message is made of two lines
		String sender = server.readLine();
		String message = server.readLine();
		
		//Check if the message is not null
		if (sender != null && message != null)
		    
		    //If the message is sent from the server
		    if (sender.equals("server"))
		    {
			//The user was trying to send a message to a client that does not exist
			if (message.equals("Unexistent_client"))
			{
			    JOptionPane.showMessageDialog(Client.getFrame(), "User disconnected.");
			    client.notifyObs();
			    
			    //the client was trying to communicate to its pair, so it is set to null
			    Client.setPair(null);
			}
		    }
		
		//This means the server has sent the players list
		    else if (sender.equals("players"))
		    {
			client.setPlayersList(message);
		    }
		//this is redirected from the client via the server in order to break the loop and stop the thread
		    else if (sender.equals("quit"))
		    {
			break;
		    }
		//This means the server has sent the scores list
		    else if (sender.equals("scores"))
		    {
			client.setScoresList(message);
		    }
		//This means an invite that the player has sent was received by the other player
		    else if(message.equals("INVITE_RECEIVED"))
			Client.setPair(sender);	
		//This means another player has sent an invitation
		    else if (message.equals("play"))
			//The invitation is only valid if the player is not paired with someone else
			if (Client.getPair() == null)
			{
			    client.addInvite(sender);
			    //confirm to the other player that the invitation is valid
			    Client.send(sender, "INVITE_RECEIVED");
			}
			//or not
			else
			    Client.send(sender, "already_playing");
		//If a sent invitation is not valid
		    else if (message.equals("already_playing"))
		    {
			JOptionPane.showMessageDialog(Client.getFrame(),
				"That person is already in a game or has sent an invitation to someone.",
				"Try again later", JOptionPane.ERROR_MESSAGE);
		    } 
		//if the other player accepted the invitation
		    else if (message.equals("accept"))
		    {
			if (Client.getPair().equals(sender))
			{
			    //the game starts
			    Client.initGame(1);
			} 

		    }
		//if the other player rejected the invitation a message dialog pops up and the pair is reset to null
		    else if (message.equals("deny"))
		    {
			if (Client.getPair().equals(sender))
			{
			    JOptionPane.showMessageDialog(Client.getFrame(), sender + " denied your request.");
			    Client.setPair(null);
			} 
		    }
		//if the other player quit
		    else if (message.equals("I exited"))
		    {
			JOptionPane.showMessageDialog(Client.getFrame(), "The other person closed the game", "You won!",
				JOptionPane.ERROR_MESSAGE);
			Client.sendGameEnd(1);
			Client.setPair(null);
		    }
		//if the other player has made a move I show it in the game GUI
		    else if (message.contains("move"))
		    {
			//make sure the player didn't close the window without telling the other player
			if (Client.getPair() == null)
			{
			    Client.send(sender, "I exited");
			} else
			{
			    int move = Integer.parseInt((message.split(" "))[1]);
			    Client.turn(move);
			}
		    }
		// This would make the users be able to chat, but this functionality is not needed.
//		    else
//		    {
//			System.out.println(sender + ": " + message);
//		    }
		else
		{
		    server.close(); // Probably no point.
		    throw new IOException("Got null from server"); // Caught
								   // below.
		}
	    }
	} catch (IOException e)
	{
	    JOptionPane.showMessageDialog(Client.getFrame(), "Server seems to have died " + e.getMessage());
	    System.exit(1); // Give up.
	}
    }
}
