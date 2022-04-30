import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		// Initialize the grid
		SimulationGrid grid = new SimulationGrid(60, 35);

		// Add your critters to the battle
		Map<Class<? extends Critter>, Integer> communityCount = new HashMap<>();
		communityCount.put(EricA.class, 30);
		communityCount.put(Stevie.class, 30);
		communityCount.put(Food.class, 30);

		grid.populate(communityCount);
		new CritterVisualFrame(grid);
	}
}
