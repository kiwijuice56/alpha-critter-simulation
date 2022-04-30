import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates window for visual testing
 */
public class CritterVisualFrame extends JFrame {
	private static final Font CRITTER_FONT = new Font("Monospaced", Font.BOLD, 12);
	private static final Color BG_COLOR_1 = new Color(55,55,68);
	private static final Color BG_COLOR_2 = new Color(42,42,52);
	private static final Color TEXT_COLOR = new Color(200, 203, 207);
	private static final Color WIN_TEXT_COLOR = new Color(190, 255, 180);
	private static final Color LOSE_TEXT_COLOR = new Color(100, 100, 120);

	private final Map<Class<? extends Critter>, JLabel> countLabels;
	private final SimulationGrid grid;
	private final Timer timer;

	public CritterVisualFrame(SimulationGrid grid) throws Exception {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("AlphaCritter Visual Simulation");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().setBackground(BG_COLOR_1);

		countLabels = new HashMap<>();
		this.grid = grid;

		timer = new Timer(5, e -> {
			try {
				grid.step();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			repaint();
		});
		timer.setCoalesce(true);

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(BG_COLOR_2);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

		CritterPanel critterPanel = new CritterPanel(grid);
		centerPanel.add(critterPanel);
		centerPanel.add(createCritterCounter(grid));

		mainPanel.add(centerPanel);
		mainPanel.add(initializeControlPanel(grid, critterPanel));

		getContentPane().add(Box.createVerticalGlue());
		getContentPane().add(mainPanel);
		getContentPane().add(Box.createVerticalGlue());

		setMinimumSize(new Dimension(1100, 700));
		setVisible(true);
	}

	public JPanel initializeControlPanel(SimulationGrid grid, CritterPanel critterPanel) throws IOException {
		// Initialize the step components

		JSlider stepAmount = new JSlider(SwingConstants.HORIZONTAL, 0, 1000, 1);
		stepAmount.setMajorTickSpacing(250); stepAmount.setMinorTickSpacing(50);
		stepAmount.setPaintTicks(true); stepAmount.setPaintLabels(true);
		darkenJComponent(stepAmount);

		JButton stepButton = new DarkJButton("step");
		stepButton.addActionListener(e -> {
			for (int i = 0; i < stepAmount.getValue(); i++) {
				try {
					grid.step();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			repaint();
		});

		JPanel stepAmountSliderHolder = new JPanel();
		stepAmountSliderHolder.setLayout(new BoxLayout(stepAmountSliderHolder, BoxLayout.X_AXIS));
		darkenJComponent(stepAmountSliderHolder);

		JLabel stepSizeLabel = new JLabel("Step size");
		darkenJComponent(stepSizeLabel);

		stepAmountSliderHolder.add(stepSizeLabel);
		stepAmountSliderHolder.add(stepAmount);

		// Initialize the speed components

		JSlider speed = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 15);
		speed.addChangeListener(e -> timer.setDelay(speed.getValue()));
		speed.setMajorTickSpacing(25); speed.setMinorTickSpacing(5);
		speed.setPaintTicks(true); speed.setPaintLabels(true);
		darkenJComponent(speed);

		JPanel speedSliderHolder = new JPanel();
		speedSliderHolder.setBorder(new EmptyBorder(0,0,0,25));
		speedSliderHolder.setLayout(new BoxLayout(speedSliderHolder, BoxLayout.X_AXIS));
		darkenJComponent(speedSliderHolder);

		JLabel frameDelayLabel = new JLabel("Frame Delay (ms)");
		darkenJComponent(frameDelayLabel);

		speedSliderHolder.add(frameDelayLabel);
		speedSliderHolder.add(speed);

		// Initialize play button

		JButton playButton = new DarkJButton("");
		ImageIcon playIcon = new ImageIcon(ImageIO.read(new File("resources/play.png")));
		ImageIcon pauseIcon = new ImageIcon(ImageIO.read(new File("resources/pause.png")));
		playButton.setIcon(playIcon);

		playButton.addActionListener(e -> {
			if (!timer.isRunning()) {
				timer.start();
				playButton.setIcon(pauseIcon);
			} else {
				timer.stop();
				playButton.setIcon(playIcon);
			}
		});

		// Initialize debug button

		JButton debugButton = new DarkJButton("");
		ImageIcon debugOnIcon = new ImageIcon(ImageIO.read(new File("resources/debug_on.png")));
		ImageIcon debugOffIcon = new ImageIcon(ImageIO.read(new File("resources/debug_off.png")));
		debugButton.setIcon(debugOffIcon);

		debugButton.addActionListener(e -> {
			if (critterPanel.isDebugMode()) {
				debugButton.setIcon(debugOffIcon);
				critterPanel.setDebugMode(false);
			} else {
				debugButton.setIcon(debugOnIcon);
				critterPanel.setDebugMode(true);
			}
			critterPanel.repaint();
		});

		// Combine all prepared elements

		JPanel sliderHolder = new JPanel();
		sliderHolder.setBorder(new EmptyBorder(10,0,0,0));
		sliderHolder.setLayout(new BoxLayout(sliderHolder, BoxLayout.X_AXIS));
		sliderHolder.add(speedSliderHolder);
		sliderHolder.add(stepAmountSliderHolder);
		darkenJComponent(sliderHolder);

		JPanel buttonHolder = new JPanel();
		buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
		buttonHolder.add(playButton);
		buttonHolder.add(debugButton);
		buttonHolder.add(stepButton);
		darkenJComponent(buttonHolder);

		JPanel controlHolder = new JPanel();
		controlHolder.setLayout(new BoxLayout(controlHolder, BoxLayout.Y_AXIS));
		controlHolder.add(buttonHolder);
		controlHolder.add(sliderHolder);
		controlHolder.setBorder(new EmptyBorder(5,10,5,10));
		darkenJComponent(controlHolder);

		return controlHolder;
	}

	public JPanel createCritterCounter(SimulationGrid grid) throws Exception {
		JPanel holder = new JPanel();
		holder.setBorder(new EmptyBorder(10, 10, 10, 10));
		holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS));
		holder.setBackground(BG_COLOR_2);

		for (Class<? extends Critter> species : grid.getCritterCount().keySet()) {
			JPanel critterHolder = new JPanel();
			critterHolder.setLayout(new FlowLayout(FlowLayout.LEFT));
			critterHolder.setBackground(BG_COLOR_2);
			critterHolder.setMaximumSize(new Dimension(164, 32));

			JLabel nameLabel = new JLabel(species.getName().substring(0, Math.min(species.getName().length(), 16)));
			nameLabel.setBackground(BG_COLOR_2);
			nameLabel.setForeground(TEXT_COLOR);

			JLabel countLabel = new JLabel(String.format("%04d", grid.getCritterCount().get(species)));
			countLabel.setFont(CRITTER_FONT);
			countLabel.setBackground(BG_COLOR_2);
			countLabel.setForeground(TEXT_COLOR);
			countLabels.put(species, countLabel);

			Critter ref = species.getDeclaredConstructor().newInstance();
			JLabel iconLabel = new JLabel(ref.toString().substring(0, Math.min(ref.toString().length(), 1)));
			iconLabel.setFont(CRITTER_FONT);
			iconLabel.setBackground(BG_COLOR_2);
			iconLabel.setForeground(ref.getColor());

			critterHolder.add(countLabel);
			critterHolder.add(iconLabel);
			critterHolder.add(nameLabel);
			holder.add(critterHolder);
		}
		return holder;
	}

	public static void darkenJComponent(JComponent j) {
		j.setBackground(BG_COLOR_1);
		j.setForeground(TEXT_COLOR);
	}

	// Override repaint to update count labels as the game progresses
	@Override
	public void repaint(long time, int x, int y, int width, int height) {
		super.repaint(time, x, y, width, height);
		int maxCount = Collections.max(grid.getCritterCount().values());
		for (Class<? extends Critter> species : countLabels.keySet()) {
			countLabels.get(species).setText(String.format("%04d", grid.getCritterCount().get(species)));
			if (grid.getCritterCount().get(species) == 0)
				countLabels.get(species).setForeground(LOSE_TEXT_COLOR);
			else if (grid.getCritterCount().get(species) == maxCount)
				countLabels.get(species).setForeground(WIN_TEXT_COLOR);
			else
				countLabels.get(species).setForeground(TEXT_COLOR);
		}
	}
}

/**
 * Custom JButton with darker color scheme
 */
class DarkJButton extends JButton {
	private static final Color BG_COLOR = new Color(68,68,81);
	private static final Color HOV_COLOR = new Color(75,75,86);
	private static final Color PRESS_COLOR = new Color(95,95,115);

	public DarkJButton(String text) {
		super(text);
		setForeground(new Color(200, 203, 207));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(40,40,55)),
				BorderFactory.createEmptyBorder(6,32,6,32)));
		setContentAreaFilled(false); setFocusPainted(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isPressed())
			g.setColor(PRESS_COLOR);
		else if (getModel().isRollover())
			g.setColor(HOV_COLOR);
		else
			g.setColor(BG_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
}
