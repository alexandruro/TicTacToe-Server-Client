package sw.assignments.TicTacToe;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Represents a TicTacToe panel
 */
public class NoughtsCrossesComponent extends JPanel
{
	public NoughtsCrossesComponent(NoughtsCrossesModel model, JFrame frame)
	{
		super();
		
		BoardView board = new BoardView(model, frame);
		
		model.addObserver(board);
		
		setLayout(new BorderLayout());
		
		add(board, BorderLayout.CENTER);
	}
}
