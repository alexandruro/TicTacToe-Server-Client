package sw.assignments.TicTacToe;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Represents the panel containing the tic-tac-toe board 
 */
public class BoardView extends JPanel implements Observer
{
    private NoughtsCrossesModel model;
    private JButton[][] cell;
    private JFrame frame;

    /**
     * Initialises the panel
     * @param model The model object
     * @param frame 
     */
    public BoardView(NoughtsCrossesModel model, JFrame frame)
    {
	super();
	
	this.frame = frame;

	// initialise model
	this.model = model;

	// create array of buttons
	cell = new JButton[3][3];

	// set layout of panel
	setLayout(new GridLayout(3, 3));

	// for each square in grid:create a button; place on panel
	for (int i = 0; i < 3; i++)
	{
	    for (int j = 0; j < 3; j++)
	    {
		cell[i][j] = new JButton(" ");
		final int x = i;
		final int y = j;
		cell[i][j].addActionListener(e ->
		{
		    //The move is sent to the user with whom this client is playing
		    Client.sendTurn(x * 3 + y);
		    model.turn(x, y);
		});
		add(cell[i][j]);
	    }
	}
	update(null, null);
    }

    @Override
    public void update(Observable obs, Object obj)
    {
	//computes whether the game is over or not
	boolean notOver = !model.gameEnded();
	
	//if the game ended then show a message showing the outcome and tell the server if the player has won or lost
	if (!notOver)
	{
	    Client.setPair(null);
	    if (model.playerWon())
	    {
		
		Client.sendGameEnd(1);
		JOptionPane.showMessageDialog(this, "You won! Congratulations!");
		frame.dispose();
		
	    } else if (!model.playerLost())
	    {
		Client.sendGameEnd(0);
		JOptionPane.showMessageDialog(this, "The game ended as a draw.");
		frame.dispose();
	    } else
	    {
		Client.sendGameEnd(-1);
		JOptionPane.showMessageDialog(this, "Game lost. Better luck next time!");
		frame.dispose();
	    }
	}

	// for each square do the following:
	// if it's a NOUGHT, put O on button
	// if it's a CROSS, put X on button
	// else put on button
	for (int i = 0; i < 3; i++)
	{
	    for (int j = 0; j < 3; j++)
	    {
		if (model.get(i, j) == NoughtsCrosses.CROSS)
		{
		    cell[i][j].setText("X");
		    cell[i][j].setEnabled(false);
		} else if (model.get(i, j) == NoughtsCrosses.NOUGHT)
		{
		    cell[i][j].setText("O");
		    cell[i][j].setEnabled(false);
		} else
		{
		    cell[i][j].setText(" ");
		    //only enable the cell if it is the player's turn
		    cell[i][j].setEnabled(notOver && model.isPlayerTurn());
		}
	    }
	}
	repaint();
    }
}
