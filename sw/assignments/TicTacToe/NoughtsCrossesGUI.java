package sw.assignments.TicTacToe;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Painter;

/**
 * Contains a TicTacToe game frame
 */
public class NoughtsCrossesGUI
{

    public NoughtsCrossesModel model;

    /**
     * Initialises the GUI, having in mind if the client plays noughts or crosses
     * @param player 0 if the player plays noughts, 1 if the player plays crosses
     */
    public NoughtsCrossesGUI(int player)
    {
	NoughtsCrosses game = new NoughtsCrosses(player);
	model = new NoughtsCrossesModel(game);

	JFrame frame = new JFrame("Noughts and Crosses");
	NoughtsCrossesComponent comp = new NoughtsCrossesComponent(model, frame);

	frame.setSize(400, 400);
	
	//I am setting what happens if the window is closed
	frame.addWindowListener(new WindowAdapter()
	{
	    @Override
	    public void windowClosing(WindowEvent e)
	    {
		//If the game is finished or the other player closed the game (or disconnected) 
		//the player can simply close the window. A confirm dialog pops up otherwise
		//If the user wants to exit a message is sent to the other player, 
		//a loss is recorded via the server and the pair is set to null
		if (!game.gameEnded() && Client.getPair() != null)
		    if (JOptionPane.showConfirmDialog(frame,
			    "Are you sure you want to close the game? You are still playing.", "Exiting...",
			    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
		    {
			Client.send(Client.getPair(), "I exited");
			Client.setPair(null);
			frame.dispose();
			Client.sendGameEnd(-1);
		    }
		    else return;
	    }
	});

	frame.add(comp);

	frame.setVisible(true);

    }

    /**
     * Gets the TicTacToe game model
     * @return the NoughtsCrossesModel object
     */
    public NoughtsCrossesModel getModel()
    {
	return model;
    }
}
