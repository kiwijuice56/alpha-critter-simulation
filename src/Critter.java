import java.awt.Color;

public class Critter {
    public enum Neighbor { WALL, EMPTY, SAME, OTHER }

    public enum Action { HOP, LEFT, RIGHT, INFECT }

    public enum Direction { NORTH, EAST, SOUTH, WEST }

    // These methods should be overridden

    public Action getMove(CritterInfo info) {
        return Action.LEFT;
    }

    public Color getColor() {
        return Color.BLACK;
    }

    public String toString() {
        return "?";
    }

    public void resetClassState() {

    }
}