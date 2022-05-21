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
            case NORTHWEST -> getNorth().getWest();
            case NORTH -> getNorth();
            case NORTHEAST -> getNorth().getEast();
            case EAST -> getEast();
            case WEST -> getWest();
            case SOUTHWEST -> getSouth().getWest();
            case SOUTH -> getSouth();
            case SOUTHEAST -> getSouth().getEast();
            default -> this;
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
            case 1 -> getDirection(Direction.NORTHWEST);
            case 2 -> getDirection(Direction.NORTH);
            case 3 -> getDirection(Direction.NORTHEAST);
            case 4 -> getDirection(Direction.WEST);
            case 6 -> getDirection(Direction.EAST);
            case 7 -> getDirection(Direction.SOUTHWEST);
            case 8 -> getDirection(Direction.SOUTH);
            case 9 -> getDirection(Direction.SOUTHEAST);
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
