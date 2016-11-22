package sw.assignments.TicTacToe;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Represents a panel containing the player list, the invite list and the controls corresponding to them
 */
public class PlayersInvitesList extends JPanel implements Observer
{
    JList<String> players;
    JList<String> invites;
    Client client;

    /**
     * Initialise every component of the panel
     * @param client The Client object
     */
    public PlayersInvitesList(Client client)
    {
	super();
	
	this.client = client;
	setLayout(new GridLayout(1, 2));
	setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

	//The player list (represented by a JList) 
	JPanel playerPanel = new JPanel();
	playerPanel.setLayout(new BorderLayout());
	players = new JList<>();
	playerPanel.add(players, BorderLayout.CENTER);
	playerPanel.add(new JLabel("Connected players:"), BorderLayout.NORTH);
	playerPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

	//The controls of the player list (refresh & invite)
	JPanel playerButtons = new JPanel();
	JButton refresh = new JButton("Refresh players list");
	refresh.addActionListener(e -> Client.send("server", "players"));
	JButton invite = new JButton("Invite");
	invite.addActionListener(e ->
	{
	    //Clicking the invite button will send a message to the selected player
	    //This only happens if the client has no pair 
	    //(a pair is a player to whom an invitation was sent by the client or with whom a game is being played)
	    if (players.getSelectedValue() != null && players.getSelectedValue() != "")
		if(Client.getPair() == null)
		    Client.send(players.getSelectedValue().toString(), "play");
		else JOptionPane.showMessageDialog(Client.getFrame(), "You already invited a player or you accepted an invitation!",
			"You can't invite any players right now.", JOptionPane.WARNING_MESSAGE);
	    else  JOptionPane.showMessageDialog(Client.getFrame(), "Please select a player.");
	});
	playerButtons.add(invite);
	playerButtons.add(refresh);
	playerPanel.add(playerButtons, BorderLayout.SOUTH);

	add(playerPanel);

	//The invitations list (also represented using a JList)
	JPanel invitePanel = new JPanel();
	invitePanel.setLayout(new BorderLayout());
	invitePanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	invites = new JList<>();
	invitePanel.add(invites, BorderLayout.CENTER);
	invitePanel.add(new JLabel("Invites received:"), BorderLayout.NORTH);
	
	//the controls of the list
	JButton accept = new JButton("Accept invite");
	accept.addActionListener(e ->
	{
	    //The invitation can only be accepted if the pair of the user is null
	    if(Client.getPair() !=null)
	    {
		JOptionPane.showMessageDialog(Client.getFrame(), "You are already in a game or you sent an invitation.");
	    }
	    else if (invites.getSelectedValue() != null && invites.getSelectedValue() != "")
	    {
		//a message is sent to the other player and the invite is removed from the list
		Client.send(invites.getSelectedValue(), "accept");
		client.removeInvite(invites.getSelectedValue());
	    }
	    else  
		JOptionPane.showMessageDialog(Client.getFrame(), "Please select an invitation.");
	});
	JButton deny = new JButton("Deny invite");
	deny.addActionListener(e ->
	{
	    if (invites.getSelectedValue() != null && invites.getSelectedValue() != "")
	    {
		//a message is sent to the other player and the invite is removed from the list
		Client.send(invites.getSelectedValue(), "deny");
		client.removeInvite(invites.getSelectedValue());
	    }
	    else  JOptionPane.showMessageDialog(Client.getFrame(), "Please select an invitation.");
	});

	JPanel inviteButtons = new JPanel();
	inviteButtons.add(accept);
	inviteButtons.add(deny);

	invitePanel.add(inviteButtons, BorderLayout.SOUTH);

	add(invitePanel);
    }

    @Override
    public void update(Observable o, Object arg)
    {
	//refreshes the lists
	
	players.setListData(client.getPlayersList());
	if (!client.getInvite().isEmpty() && client.getInvite() != null)
	    invites.setListData(client.getInvite().toArray(new String[client.getInvite().size()]));
	else
	    invites.setListData(new String[1]);
	repaint();
    }

}
