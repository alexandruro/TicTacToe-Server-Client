// Usage:
//java Server <port number>
package sw.assignments.TicTacToe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Represents a server of TicTacToe
 */
public class Server
{

    @SuppressWarnings("resource")
    private static ServerSocket openServerSocket(int port)
    {
 	try
 	{
 	    return new ServerSocket(port);
 	} catch (IOException e)
 	{
 	    System.out.println("Couldn't listen on port " + port + ". Do you want to retry? Type \"Y\" if you do.");
 	    if(new Scanner(System.in).nextLine().equals("Y"))
 		return openServerSocket(port); 
 	    else 
 		System.exit(1);
 	}
 	
 	return null;
    }
    
    
    /**
     * Starts the server
     * @param args the port number
     */
    public static void main(String[] args)
    {
	//initialising
	int port = 0;

	// Check if the program has been run correctly
	if (args.length != 1)
	{
	    System.err.println("Usage: java Server <port number>");
	    System.exit(1);
	}
	
	//Check if the port is indeed a relevant number
	try
	{
	    port = Integer.parseInt(args[0]);
	    if (port < 0 || port > 65535)
		throw new NumberFormatException(); //this is just for convenience
	} catch (NumberFormatException e)
	{
	    System.out.println("The port must be a number between 0 and 65535.");
	    System.exit(1);
	}

	// The client table shared by the server threads:
	ClientTable clientTable = new ClientTable();

	// Open a server socket:
	ServerSocket serverSocket = openServerSocket(port);
	
	// Good. We succeeded. But we must try again for the same reason:
	try
	{
	    // We loop for ever, as servers usually do:

	    while (true)
	    {
		// Listen to the socket, accepting connections from new clients:
		Socket socket = serverSocket.accept();

		//a new thread is created here so that other clients can connect while one is trying to find a username that is not taken
		new Thread(new Runnable()
		{

		    @Override
		    public void run()
		    {
			try
			{
			    // Creating input/output ways to communicate with the client
			    BufferedReader fromClient = new BufferedReader(
				    new InputStreamReader(socket.getInputStream()));
			    PrintStream toClient = new PrintStream(socket.getOutputStream());

			    // We ask the client what its name is:
			    String clientName = fromClient.readLine();

			    // We check if a client with that name is already connected and
			    // prompt the user to choose another name if that is the case,
			    // then we add it to the client table
			    while (!clientTable.add(clientName)) // this method returns false if the name is already in the table
			    {
				toClient.println("That name is already taken. Please choose another one.");
				clientName = fromClient.readLine();
			    }
			    
			    toClient.println("Nickname accepted");

			    // For debugging:
			    System.out.println(clientName + " connected");

			    // We create and start a new thread to write to the client:
			    ServerSender sender = new ServerSender(clientTable.getQueue(clientName), toClient);
			    sender.start();

			    // We create and start a new thread to read from the client:
			    ServerReceiver receiver = new ServerReceiver(clientName, fromClient, clientTable, sender);
			    receiver.start();

			    //this will make this inner class (the thread) exit when the client exits
			    try
			    {
				sender.join();
				receiver.join();
			    } catch (InterruptedException e)
			    {
				System.err.println("Unexpected interruption " + e.getMessage());
			    }
			    	
			    System.out.println(clientName + " disconnected");

			} 
			catch (IOException e)
			{
			    System.err.println("IO error " + e.getMessage());
			}
		    }
		}).start();

	    }
	} 
	catch (IOException e)
	{
	    System.err.println("IO error " + e.getMessage());
	}
    }
}
