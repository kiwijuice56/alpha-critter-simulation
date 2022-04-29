import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Create window for visual testing
 */
public class CritterVisualFrame extends JFrame {
	private final Timer timer;
	private static final Color BG_COLOR = new Color(55,55,68);
	private static final Color TEXT_COLOR = new Color(200, 203, 207);

	public CritterVisualFrame(SimulationGrid grid) throws IOException {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("AlphaCritter Visual Simulation");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().setBackground(BG_COLOR);

		timer = new Timer(5, e -> {
			try {
				grid.step();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			repaint();
		});
		timer.setCoalesce(true);

		JPanel holder = new JPanel();
		holder.setBackground(Color.BLACK);
		holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS));

		CritterPanel panel = new CritterPanel(grid);

		holder.add(panel);
		holder.add(initializeControlPanel(grid));

		getContentPane().add(Box.createVerticalGlue());
		getContentPane().add(holder);
		getContentPane().add(Box.createVerticalGlue());
		setMinimumSize(new Dimension(1000, 700));
		setVisible(true);
	}

	public JPanel initializeControlPanel(SimulationGrid grid) throws IOException {
		JSlider stepAmount = new JSlider(SwingConstants.HORIZONTAL, 0, 1000, 1);
		stepAmount.setMajorTickSpacing(250); stepAmount.setMinorTickSpacing(50);
		stepAmount.setPaintTicks(true); stepAmount.setPaintLabels(true);
		stepAmount.setBackground(BG_COLOR);
		stepAmount.setForeground(TEXT_COLOR);

		JSlider speed = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 15);
		speed.addChangeListener(e -> timer.setDelay((speed.getValue())));
		speed.setMajorTickSpacing(25); speed.setMinorTickSpacing(5);
		speed.setPaintTicks(true); speed.setPaintLabels(true);
		speed.setBackground(BG_COLOR);
		speed.setForeground(TEXT_COLOR);

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

		JPanel speedSliderHolder = new JPanel();
		speedSliderHolder.setBorder(new EmptyBorder(0,0,0,25));
		speedSliderHolder.setLayout(new BoxLayout(speedSliderHolder, BoxLayout.X_AXIS));
		JLabel frameDelayLabel = new JLabel("Frame Delay (ms)");
		frameDelayLabel.setForeground(TEXT_COLOR);
		speedSliderHolder.add(frameDelayLabel);
		speedSliderHolder.add(speed);
		speedSliderHolder.setBackground(BG_COLOR);

		JPanel stepAmountSliderHolder = new JPanel();
		stepAmountSliderHolder.setLayout(new BoxLayout(stepAmountSliderHolder, BoxLayout.X_AXIS));
		JLabel stepSizeLabel = new JLabel("Step size");
		stepSizeLabel.setForeground(TEXT_COLOR);
		stepAmountSliderHolder.add(stepSizeLabel);
		stepAmountSliderHolder.add(stepAmount);
		stepAmountSliderHolder.setBackground(BG_COLOR);

		JButton playButton = new DarkJButton("");
		ImageIcon playIcon = new ImageIcon(ImageIO.read(new File("resources/play.png")));
		ImageIcon pauseIcon = new ImageIcon(ImageIO.read(new File("resources/pause.png")));
		playButton.setIcon(playIcon);

		playButton.addActionListener(e -> {
			if (!timer.isRunning()) {
				playButton.setIcon(pauseIcon);
				timer.start();
			} else {
				timer.stop();
				playButton.setIcon(playIcon);
			}
		});

		JPanel sliderHolder = new JPanel();
		sliderHolder.setBorder(new EmptyBorder(0,0,10,0));
		sliderHolder.setLayout(new BoxLayout(sliderHolder, BoxLayout.X_AXIS));
		sliderHolder.add(speedSliderHolder);
		sliderHolder.add(stepAmountSliderHolder);
		sliderHolder.setBackground(BG_COLOR);

		JPanel buttonHolder = new JPanel();
		buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
		buttonHolder.add(playButton);
		buttonHolder.add(stepButton);
		buttonHolder.setBackground(BG_COLOR);

		JPanel controlHolder = new JPanel();
		controlHolder.setLayout(new BoxLayout(controlHolder, BoxLayout.Y_AXIS));
		controlHolder.add(sliderHolder);
		controlHolder.add(buttonHolder);
		controlHolder.setBorder(new EmptyBorder(5,10,5,10));
		controlHolder.setBackground(BG_COLOR);

		return controlHolder;
	}
}

class DarkJButton extends JButton {
	private static final Color BG_COLOR = new Color(60,60,72);
	private static final Color HOV_COLOR = new Color(70,70,80);
	private static final Color PRESS_COLOR = new Color(95,95,115);

	public DarkJButton(String text) {
		super(text);
		setForeground(new Color(200, 203, 207));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(40,40,55)),
				BorderFactory.createEmptyBorder(6,12,6,12)));
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
