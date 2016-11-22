package sw.assignments.TicTacToe;

import java.io.PrintStream;

/**
 * A class that continuously reads from message queue for a particular client,
 * forwarding to the client.
 */
public class ServerSender extends Thread
{
    private MessageQueue queue;
    private PrintStream client;
    private boolean isRunning;

    /**
     * Initialises the object with the queue and the stream
     * 
     * @param q
     *            The MessageQueue corresponding to the client
     * @param c
     *            The PrintStream corresponding to the client
     */
    public ServerSender(MessageQueue q, PrintStream c)
    {
	queue = q;
	client = c;

	// this variable is the condition on which this thread runs
	isRunning = true;
    }

    @Override
    public void run()
    {
	// The reading and sending is done as long as "isRunning" is true, which
	// means the quit method was not called
	while (isRunning)
	{
	    // a message is taken from the queue
	    // as long as there is no message in the queue the method will block
	    Message msg = queue.take();
	    
	    //this block is synchronised so that the two messages are sent at once 
	    synchronized (client)
	    {
		// the message is redirected to the client
		client.println(msg.getSender());
		client.println(msg.getText());
	    }
	}
    }

    /**
     * Sends a message to the client corresponding to this object
     * @param msg The message
     */
    public void send(Message msg)
    {
	//the message is simply put in the queue so the while in the run method will take care of it
	queue.offer(msg);
    }

    /**
     * Stops the sending of messages to the client (and the thread)
     */
    public void quit()
    {
	isRunning = false;

    }
}
