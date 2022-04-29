import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		// Initialize the grid
		SimulationGrid grid = new SimulationGrid(55, 35);

		// Add your critters to the battle
		Map<Class<? extends Critter>, Integer> communityCount = new HashMap<>();
		communityCount.put(EricA.class, 30);
		communityCount.put(Stevie.class, 30);
		communityCount.put(Wu_ll.class, 30);
		communityCount.put(michaelT.class, 30);
		communityCount.put(AlexRomans.class, 30);
		communityCount.put(Keegan.class, 30);
		communityCount.put(StarPlatinum.class, 30);
		communityCount.put(BrandonH.class, 30);

		// Populate the grid
		grid.populate(communityCount);

		new CritterVisualFrame(grid);
	}
}
