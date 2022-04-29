import javax.swing.*;
import java.awt.*;

/**
 * JPanel to paint critters onto the screen
 */
public class CritterPanel extends JPanel {
	private final Font font;
	private final SimulationGrid grid;

	public static final int FONT_SIZE = 16;

	public CritterPanel(SimulationGrid grid) {
		this.grid = grid;
		setMinimumSize(new Dimension(grid.getWidth() * FONT_SIZE,grid.getHeight() * FONT_SIZE));
		setMaximumSize(getMinimumSize());
		setPreferredSize(getMinimumSize());
		font = new Font("Monospaced", Font.BOLD, FONT_SIZE);
		setBackground(new Color(5,5,20));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(30,30,40));
		g.fillRect(0, 0, FONT_SIZE * grid.getWidth(), FONT_SIZE * grid.getHeight());

		g.setFont(font);
		for (Critter c : grid.getStatus().keySet()) {
			g.setColor(c.getColor());
			g.drawString(c.toString(), grid.getStatus().get(c).col * FONT_SIZE, grid.getStatus().get(c).row * FONT_SIZE);
		}
	}
}