package sw.assignments.TicTacToe;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Represents the panel on the right side of the GUI, containing the exit button and the score viewing one.
 */
public class ButtonPanel extends JPanel
{

    	/**
    	 * Initialises the panel and the buttons in it
    	 */
	public ButtonPanel()
	{
		super();
		setLayout(new GridLayout(4, 1));
		
		//The button that will show the scores
		JButton scores = new JButton("View scores");
		scores.addActionListener(e -> 
		{
		    //shows a window that has a JList with the scores
		    JPanel panel = new JPanel();
		    JList<String> list = new JList<>(Client.getScoresList());
		    panel.add(list);
		    JOptionPane.showMessageDialog(null, panel);
		});
		
		//the button that closes the client
		JButton exit = new JButton("Exit");
		exit.addActionListener(e -> Client.send("quit", "quit"));
		
		//adding them to the panel
		add(scores);
		add(exit);
	}
}
