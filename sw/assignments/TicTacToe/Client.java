//Usage: java Client
package sw.assignments.TicTacToe;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * The main class of the client, the one that starts everything
 */
class Client extends Observable
{
    private static String pair;
    private static NoughtsCrossesGUI game;
    private static PrintStream toServer;
    private static String[] playerList;
    private static String[] scoreList;
    private static ArrayList<String> inviteList;
    private static JFrame mainGUI;
    private JFrame initialGUI;

    private BufferedReader fromServer;
    private ClientReceiver receiver;
    private Socket server;

    private String nickname;
    private String hostname;
    private int port;

    /**
     * Initialises the client and the GUI (the GUI is not visible until the user
     * presses connect)
     */
    public Client()
    {
	// Initialises the window in which the server details and the nickname
	// are to be inputed
	makeInitialGUI();
    }

    /**
     * Return the frame of the GUI
     * 
     * @return The JFrame object
     */
    public static JFrame getFrame()
    {
	return mainGUI;
    }

    /**
     * Initialises the TicTacToe game
     * 
     * @param player
     *            0 if the user plays noughts and 1 if he/she plays crosses
     */
    public static void initGame(int player)
    {
	game = new NoughtsCrossesGUI(player);
    }

    /**
     * Method that must be called when the other player made a move to show it
     * on the board
     * 
     * @param move
     *            A number between 0 and 8 representing the position on the
     *            board1
     */
    public static void turn(int move)
    {
	game.getModel().turn(move / 3, move % 3);
    }

    /**
     * Notifies the observers of this object
     */
    public void notifyObs()
    {
	setChanged();
	notifyObservers();
    }

    /**
     * Gets the person with whom the player is connected (the player invited him
     * or they are playing)
     * 
     * @return The nickname of that person
     */
    public static String getPair()
    {
	return pair;
    }

    /**
     * Sets the person with whom the player is connected.
     * 
     * @param p
     *            The nickname of that person
     */
    public static void setPair(String nickname)
    {
	pair = nickname;
    }

    /**
     * Sends a move to the other player through the server
     * 
     * @param move
     *            A number between 0 and 8 representing the position on the
     *            board1
     */
    public static void sendTurn(int move)
    {
	send(pair, "move " + move);
    }

    /**
     * Sends a message through the server
     * 
     * @param recipient
     *            The name of the recipient
     * @param message
     *            The message
     */
    public synchronized static void send(String recipient, String text)
    {
	if (text.equals("play"))
	{
	    //setPair(recipient);
	    // System.out.println(recipient + " added as a pair");
	} else if (text.equals("accept"))
	{
	    initGame(0);
	    setPair(recipient);
	}

	toServer.println(recipient);
	toServer.println(text);
    }

    /**
     * Tells the server that the game has ended.
     * 
     * @param outcome
     */
    public static void sendGameEnd(int outcome)
    {
	send("server", "end " + outcome);
    }

    /**
     * Stores the online players list in the class field
     * 
     * @param list
     *            The list of players (as received from the server, with
     *            everything on a single line and players separated by ";")
     */
    public void setPlayersList(String list)
    {
	playerList = list.split(";");
	setChanged();
	notifyObservers();
    }

    /**
     * Gets the list of connected players
     * 
     * @return An array of nicknames
     */
    public String[] getPlayersList()
    {
	return playerList;
    }

    /**
     * Stores the scores list in the class field
     * 
     * @param list
     *            The scores list (as received from the server, with everything
     *            on a single line and players separated by ";")
     */
    public void setScoresList(String list)
    {
	scoreList = list.split(";");

	// notifies the observers so that the list in the GUI can be refreshed
	setChanged();
	notifyObservers();
    }

    /**
     * Gets the scores list
     * 
     * @return An array of the following form: <nickname> -> (<wins>,<draws>,
     *         <loses>)
     */
    public static String[] getScoresList()
    {
	return scoreList;
    }

    /**
     * Adds a received invitation to the list
     * 
     * @param nick
     *            The nickname of the player who sent the invitation
     */
    public void addInvite(String nick)
    {
	inviteList.add(nick);
	setChanged();
	notifyObservers();
    }

    /**
     * Removes an invitation from the list (should be called when the player
     * accepts/denies it)
     * 
     * @param nick
     *            The nickname of the player who sent the invitation
     */
    public void removeInvite(String nick)
    {
	inviteList.remove(nick);
	setChanged();
	notifyObservers();
    }

    /**
     * Gets the list of invitations
     * 
     * @return An ArrayList of nicknames
     */
    public ArrayList<String> getInvite()
    {
	return inviteList;
    }

    
    //PRIVATES
    
    
    // Initialises the window in which the server details and the nickname are
    // to be inputed
    private void makeInitialGUI()
    {

	initialGUI = new JFrame("TicTacToe");

	initialGUI.setLocationRelativeTo(null);
	initialGUI.setSize(300, 300);
	initialGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	initialGUI.setLayout(new FlowLayout());

	JLabel nicknameLabel = new JLabel("Nickname");
	initialGUI.add(nicknameLabel);

	JTextField nicknameText = new JTextField("alex", 20);
	initialGUI.add(nicknameText);

	JLabel hostnameLabel = new JLabel("Hostname");
	initialGUI.add(hostnameLabel);

	JTextField hostnameText = new JTextField("localhost", 20);
	initialGUI.add(hostnameText);

	JLabel portLabel = new JLabel("Port number");
	initialGUI.add(portLabel);

	JTextField portText = new JTextField("4444", 20);
	initialGUI.add(portText);

	JButton connectButton = new JButton("Connect");
	initialGUI.add(connectButton);

	// Hitting the connect button will make the program try to establish a
	// connection to the server
	connectButton.addActionListener(new ActionListener()
	{

	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		// Checks if every requested information was typed in the fields
		if (portText.getText().equals("") || nicknameText.getText().equals("")
			|| hostnameText.getText().equals(""))
		    JOptionPane.showMessageDialog(null, "Please enter all the required information.",
			    "Something went wrong", JOptionPane.WARNING_MESSAGE);
		else
		{
		    // If everything is fine then the information is extracted
		    // from the GUI

		    // Check if the port is appropriate.
		    try
		    {
			port = Integer.parseInt(portText.getText());
			if (port < 0 || port > 65535)
			    throw new NumberFormatException();
		    } catch (NumberFormatException exception)
		    {
			JOptionPane.showMessageDialog(null, "The port must be a number between 0 and 65535.");
			return;
		    }

		    nickname = nicknameText.getText();
		    hostname = hostnameText.getText();

		    // tries to initialise the connection to the server and to initialise
		    // the input and output streams
		    if (!initialiseServerConnection())
			return;

		    // Tell the server what the client's name is
		    // (or try to come up with a new one until it is accepted)
		    setUsername();

		    // if successful, the initialGUI is disposed (inside the
		    // setUsername method)

		    // initialise the lists and the ClientReceiver object
		    completeInitialisation();

		    // Initialising the main GUI of the program
		    makeMainGUI();

		    // Wait for the player to exit and close sockets.
		    waitForFinish();

		}

	    }
	});

	initialGUI.setVisible(true);
    }

    // Initialising the main GUI of the program
    private void makeMainGUI()
    {
	
	mainGUI = new JFrame();
	mainGUI.addWindowListener(new WindowAdapter()
	{

	    @Override
	    public void windowClosing(WindowEvent e)
	    {
		super.windowClosing(e);
		Client.send("quit", "quit");
	    }
	});
	mainGUI.setSize(800, 600);
	mainGUI.setLayout(new BorderLayout());
	mainGUI.setLocationRelativeTo(null);
	
	// request the server to send the list of players, so that they are
	// visible when opening the GUI without having to refresh
	send("server", "players");

	// adds the panel containing "exit" and "show scores"
	ButtonPanel buttonPanel = new ButtonPanel();
	mainGUI.add(buttonPanel, BorderLayout.EAST);

	// adds the list of players and invitations
	PlayersInvitesList list = new PlayersInvitesList(this);
	mainGUI.add(list, BorderLayout.CENTER);

	// The PlayersInvitesList object is added as an observer so the lists
	// can be refreshed when something changes
	addObserver(list);

	mainGUI.setTitle(nickname + " - TicTacToe");
	mainGUI.setVisible(true);
	
	
	send("server", "players");
	
    }

    // tries to initialise the connection to the server and to initialise the
    // input and output streams
    // return true is everything went to plan
    private boolean initialiseServerConnection()
    {
	try
	{
	    server = new Socket(hostname, port);
	    toServer = new PrintStream(server.getOutputStream());
	    fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
	    return true;
	} catch (UnknownHostException e)
	{
	    System.err.println("Unknown host: " + hostname);
	    // System.exit(1); // Give up.
	    JOptionPane.showMessageDialog(initialGUI, "Unknown host: " + hostname);
	    return false;
	} catch (IOException e)
	{
	    System.err
		    .println("The server doesn't seem to be running " + e.getMessage() + "\n" + hostname + "\n" + port);
	    // System.exit(1); // Give up.
	    JOptionPane.showMessageDialog(initialGUI, "The server doesn't seem to be running " + e.getMessage());
	    return false;
	}
    }

    // Tell the server what the client's name is
    // (or try to come up with a new one until it is accepted)
    private void setUsername()
    {
	try
	{
	    while (true)
	    {
		toServer.println(nickname);
		if (fromServer.readLine().contains("accepted"))
		{
		    System.out.println("Nickname accepted");
		    initialGUI.dispose();
		    break;
		}
		System.out.println("That name is already taken. Please choose another one.");

		nickname = (String) JOptionPane.showInputDialog(mainGUI,
			"That name is already taken. Please choose another one.", "Duplicate nickname",
			JOptionPane.ERROR_MESSAGE);

		// If the user selected cancel
		if (nickname == null)
		    System.exit(0);
	    }
	} catch (IOException IOE)
	{
	    System.err.println("Communication broke in Client trying to send username");
	    System.exit(1);
	}
    }

    private void completeInitialisation()
    {
	playerList = new String[0];
	inviteList = new ArrayList<String>();
	scoreList = new String[0];

	// Create the receiver thread and run it in parallel with this one
	receiver = new ClientReceiver(fromServer, this);
	receiver.start();
    }

    // Wait for them to end and close sockets.
    private void waitForFinish()
    {
	new Thread(new Runnable()
	{
	    @Override
	    public void run()
	    {
		try
		{
		    receiver.join();
		    fromServer.close();
		    //System.out.println("receiver has ended");

		    server.close();
		    //System.out.println("I quited");

		    System.exit(0);

		} catch (IOException e)
		{
		    System.err.println("Something wrong " + e.getMessage());
		    System.exit(1); // Give up.
		} catch (InterruptedException e)
		{
		    System.err.println("Unexpected interruption " + e.getMessage());
		    System.exit(1); // Give up.
		}
	    }
	}).start();
	
    }

    public static void main(String[] args)
    {
	Client client = new Client();
    }

}