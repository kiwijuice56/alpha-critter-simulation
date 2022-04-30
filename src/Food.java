import java.awt.*;

public class Food extends Critter {

	@Override
	public Action getMove(CritterInfo info) {
		return Action.LEFT;
	}

	@Override
	public String toString() {
		return "☀";
	}

	@Override
	public Color getColor() {
		return new Color(128,255,35);
	}
}
