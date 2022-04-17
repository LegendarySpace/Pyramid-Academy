package world.fantasy.world;

import world.fantasy.Actor;

import java.util.concurrent.ThreadLocalRandom;

public class Land implements Comparable<Land> {
    private int row;
    private int column;

    public Land() {
        row = 0;
        column = 0;
    }

    public Land(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }

    public Land getNorth() { return new Land(row - 1, column); }
    public Land getEast() { return new Land(row, column + 1); }
    public Land getSouth() { return new Land(row + 1, column); }
    public Land getWest() { return new Land(row, column - 1); }

    public Land getDirection(Direction dir) {
        return switch (dir) {
            case NORTH -> getNorth();
            case EAST -> getEast();
            case SOUTH -> getSouth();
            case WEST -> getWest();
        };
    }

    public Land moveTowards(Actor actor) {
        if (actor == null || actor.getPosition() == null) return posFromInt(ThreadLocalRandom.current().nextInt(1,10));

        Land land;
        int vert = Math.min(Math.max(-1, actor.getPosition().getRow() - this.getRow()), 1)+2;
        int hori = Math.min(Math.max(-1, actor.getPosition().getColumn() - this.getColumn()), 1)+2;

        return posFromInt(((vert - 1) * 3) + hori);
    }

    private Land posFromInt(int i) {
        return switch (i) {
            case 1 -> getNorth().getWest();
            case 2 -> getNorth();
            case 3 -> getNorth().getEast();
            case 4 -> getWest();
            case 6 -> getEast();
            case 7 -> getSouth().getWest();
            case 8 -> getSouth();
            case 9 -> getSouth().getEast();
            default -> this;
        };
    }

    @Override
    public int hashCode() {
        return row + column;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Land l = (Land) obj;
        return row == l.getRow() && column == l.getColumn();
    }

    @Override
    public int compareTo(Land o) {
        if (o == null) return 0;
        // sort: if rows != then sort by row, else sort by column
        if (row == o.getRow()) return Integer.compare(column, o.getColumn());
        return Integer.compare(o.getRow(), row);
    }

    @Override
    public String toString() {
        return "Ground";
        // alternate land: '\u26cb'  '\u26f6'    '\u2337'    '\u2395'
    }
    // Allow land to hold an object
}
