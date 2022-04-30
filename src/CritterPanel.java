import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * JPanel to paint critters onto the screen
 */
public class CritterPanel extends JPanel {
	public static final int FONT_SIZE = 16;
	private static final int BOTTOM_PAD = 4;
	private static final Font FONT = new Font("Monospaced", Font.BOLD, FONT_SIZE);
	private static final Color BG_COLOR = new Color(22,22,32);
	private final SimulationGrid grid;
	private boolean debugMode;

	public CritterPanel(SimulationGrid grid) {
		this.grid = grid;

		setMinimumSize(new Dimension(grid.getWidth() * FONT_SIZE, BOTTOM_PAD + grid.getHeight() * FONT_SIZE));
		setMaximumSize(getMinimumSize());
		setPreferredSize(getMinimumSize());

		setBorder(new LineBorder(new Color(140,140,155), 1));
		setBackground(new Color(5,5,20));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, FONT_SIZE * grid.getWidth(), BOTTOM_PAD + FONT_SIZE * grid.getHeight());
		g.setFont(FONT);
		// Draw each critter according to their toString and getColor methods
		for (Critter c : grid.getStatus().keySet()) {
			g.setColor(c.getColor());
			String critterString = !debugMode ? c.toString() :
					switch (grid.getStatus().get(c).direction) {
						case NORTH -> "🡅"; case EAST -> "🡆";
						case SOUTH -> "🡇"; case WEST -> "🡄";
					};
			g.drawString(critterString, grid.getStatus().get(c).col * FONT_SIZE, (1 + grid.getStatus().get(c).row) * FONT_SIZE);
		}
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
}