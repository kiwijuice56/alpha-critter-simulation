/**
 * Encapsulated class to limit information given to critters
 *
 * Allows critters to see:
 *  - the type of their neighbors (wall, empty, other, same)
 *  - the direction of their neighbors
 *  - their own direction
 */
public final class CritterInfo {
	private final Critter.Neighbor[] neighbors;
	private final Critter.Direction[] neighborsDirs;
	private final Critter.Direction direction;

	public CritterInfo(SimulationGrid grid, Critter.Direction direction, int row, int col) {
		this.direction = direction;
		neighbors = new Critter.Neighbor[4];
		neighborsDirs = new Critter.Direction[4];

		// Store the surrounding neighbors and their directions
		Critter c = grid.getGrid()[row][col];
		for (int i = 0; i < 4; i++) {
			neighborsDirs[i] = Critter.Direction.NORTH;

			// Get the row/col for north, east, south, and west from the critter
			int dirRow = row, dirCol = col;
			switch (i) {
				case 0 -> dirRow--; case 1 -> dirCol++;
				case 2 -> dirRow++; case 3 -> dirCol--;
			}

			if (!grid.inBounds(dirRow, dirCol))
				neighbors[i] = Critter.Neighbor.WALL;
			else {
				Critter o = grid.getGrid()[dirRow][dirCol];
				if (o == null)
					neighbors[i] = Critter.Neighbor.EMPTY;
				else {
					if (!grid.getStatus().containsKey(o))
						continue;
					neighbors[i] = c.getClass() == o.getClass() ? Critter.Neighbor.SAME : Critter.Neighbor.OTHER;
					neighborsDirs[i] = grid.getStatus().get(o).direction;
				}
			}
		}
	}

	public Critter.Neighbor getFront() {
		return neighbors[direction.ordinal()];
	}
	public Critter.Neighbor getRight() {
		return neighbors[(direction.ordinal() + 1) % 4];
	}
	public Critter.Neighbor getBack() {
		return neighbors[(direction.ordinal() + 2) % 4];
	}
	public Critter.Neighbor getLeft() {
		return neighbors[(direction.ordinal() + 3) % 4];
	}

	public Critter.Direction getDirection() {
		return direction;
	}

	public Critter.Direction getFrontDirection() {
		return neighborsDirs[direction.ordinal()];
	}
	public Critter.Direction getRightDirection() {
		return neighborsDirs[(direction.ordinal() + 1) % 4];
	}
	public Critter.Direction getBackDirection() {
		return neighborsDirs[(direction.ordinal() + 2) % 4];
	}
	public Critter.Direction getLeftDirection() {
		return neighborsDirs[(direction.ordinal() + 3) % 4];
	}
}
