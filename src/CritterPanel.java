import javax.swing.*;
import java.awt.*;

/**
 * JPanel to paint critters onto the screen
 */
public class CritterPanel extends JPanel {
	private final SimulationGrid grid;
	public static final int FONT_SIZE = 16;
	private static final Font FONT = new Font("Monospaced", Font.BOLD, FONT_SIZE);
	private static final Color BG_COLOR = new Color(30,30,40);

	public CritterPanel(SimulationGrid grid) {
		this.grid = grid;

		setMinimumSize(new Dimension(grid.getWidth() * FONT_SIZE,grid.getHeight() * FONT_SIZE));
		setMaximumSize(getMinimumSize());
		setPreferredSize(getMinimumSize());

		setBackground(new Color(5,5,20));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, FONT_SIZE * grid.getWidth(), FONT_SIZE * grid.getHeight());
		g.setFont(FONT);
		// Draw each critter according to their toString and getColor methods
		for (Critter c : grid.getStatus().keySet()) {
			g.setColor(c.getColor());
			g.drawString(c.toString(), grid.getStatus().get(c).col * FONT_SIZE, (1 + grid.getStatus().get(c).row) * FONT_SIZE);
		}
	}
}