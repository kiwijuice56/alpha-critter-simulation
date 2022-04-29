import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		// Initialize the grid
		SimulationGrid grid = new SimulationGrid(55, 35);

		// Add your critters to the battle
		Map<Class<? extends Critter>, Integer> communityCount = new HashMap<>();
		communityCount.put(EricA.class, 100);
		communityCount.put(Lion.class, 900);
		communityCount.put(Wu_ll.class, 159);

		// Populate the grid
		grid.populate(communityCount);

		new CritterVisualFrame(grid);
	}
}
