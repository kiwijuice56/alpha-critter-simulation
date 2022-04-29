import java.util.*;

public class SimulationGrid {
	private Critter[][] grid;
	private Map<Critter, GridStatus> status;
	private final int width, height;

	public SimulationGrid(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new Critter[height][width];
		status = new HashMap<>();
	}

	/**
	 * Simulates one step of this grid
	 * @throws Exception Can throw a multitude of exceptions if critter does not have appropriate constructor
	 */
	public void step() throws Exception {
		// Keep track of statuses and critters to remove (must be isolated to prevent concurrent modification)
		Set<GridStatus> toAdd = new HashSet<>();
		Set<Critter> toRemove = new HashSet<>();
		Set<Critter> lock = new HashSet<>();

		for (Critter c : status.keySet()) {
			// Prevent dead critters from interacting
			if (toRemove.contains(c))
				continue;

			// Initialize directions of this critter
			Critter.Direction dir = status.get(c).direction;
			int row = status.get(c).row, col = status.get(c).col;
			int frontRow = row, frontCol = col;
			switch (dir) {
				case NORTH -> frontRow--; case EAST -> frontCol++;
				case SOUTH -> frontRow++; case WEST -> frontCol--;
			}

			Critter.Action action = c.getMove(new CritterInfo(this, status.get(c).direction, row, col));

			switch (action) {
				case RIGHT -> status.get(c).direction = Critter.Direction.values()[(dir.ordinal() + 1) % 4];
				case LEFT -> status.get(c).direction = Critter.Direction.values()[(dir.ordinal() + 3) % 4];
				case HOP -> {
					if (!inBounds(frontRow, frontCol) || grid[frontRow][frontCol] != null)
						break;
					// Swap the critter in the grid and update the status
					grid[frontRow][frontCol] = c;
					grid[row][col] = null;
					status.get(c).row = frontRow;
					status.get(c).col = frontCol;
					lock.add(c);
				}
				case INFECT -> {
					// Check that there is an available critter to infect
					if (!inBounds(frontRow, frontCol) || grid[frontRow][frontCol] == null ||
							grid[frontRow][frontCol].getClass() == c.getClass() ||
							lock.contains(grid[frontRow][frontCol]))
						break;
					// Replace critter with new instance, and lock the new and old critters form interacting
					toRemove.add(grid[frontRow][frontCol]);

					grid[frontRow][frontCol] = c.getClass().getDeclaredConstructor().newInstance();
					toAdd.add(new GridStatus(frontRow, frontCol, Critter.Direction.values()[(int) (Math.random() * 4)]));
					lock.add(grid[frontRow][frontCol]);
				}
			}
		}
		// Update status map with new and removed critters
		for (GridStatus newStatus : toAdd)
			status.put(grid[newStatus.row][newStatus.col], newStatus);
		for (Critter eaten : toRemove)
			status.remove(eaten);
	}

	/**
	 * Initializes the grid with a critter class : count mapping
	 * @param communityCount The species to add and the corresponding counts
	 * @throws Exception Can throw a multitude of exceptions if critter does not have appropriate constructor
	 */
	public void populate(Map<Class<? extends Critter>, Integer> communityCount) throws Exception {
		for (Class<? extends Critter> species : communityCount.keySet())
			species.getDeclaredConstructor().newInstance().resetClassState();

		int totalCritters = 0;
		for (int speciesCount : communityCount.values())
			totalCritters += speciesCount;

		if (totalCritters > width * height)
			throw new Exception("Attempted to add %d critters while there was only %d available spaces".formatted(totalCritters, width * height));

		// Shuffle all critter and empty cells then add them to the grid
		List<Class<? extends Critter>> toAdd = new ArrayList<>(totalCritters);
		for (Class<? extends Critter> species : communityCount.keySet())
			for (int i = 0; i < communityCount.get(species); i++)
				toAdd.add(species);
		for (int i = 0; i < width * height - totalCritters; i++)
			toAdd.add(null);
		Collections.shuffle(toAdd);

		grid = new Critter[height][width];
		status = new HashMap<>();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				Class<? extends Critter> species = toAdd.remove(toAdd.size()-1);
				if (species != null) {
					grid[row][col] = species.getDeclaredConstructor().newInstance();
					status.put(grid[row][col], new GridStatus(row, col, Critter.Direction.values()[(int) (Math.random() * 4)]));
				}
			}
		}
	}

	public boolean inBounds(int row, int col) {
		return row >= 0 && row < grid.length && col >= 0 && col < grid[row].length;
	}

	// Accessor methods

	public Critter[][] getGrid() {
		return grid;
	}

	public Map<Critter, GridStatus> getStatus() {
		return status;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}

class GridStatus {
	public int row, col;
	public Critter.Direction direction;

	public GridStatus(int row, int col, Critter.Direction direction) {
		this.row = row;
		this.col = col;
		this.direction = direction;
	}
}
