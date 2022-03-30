package world.fantasy.world;

public class Land implements Comparable<Land> {
    private int row;
    private int column;

    public Object occupant;

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

    public Land getNorth() { return new Land(row + 1, column); }
    public Land getEast() { return new Land(row, column + 1); }
    public Land getSouth() { return new Land(row - 1, column); }
    public Land getWest() { return new Land(row, column - 1); }

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
        return Character.toString('\u2395');
        // alternate land: '\u26cb'  '\u26f6'    '\u2337'    '\u2395'
    }
    // Allow land to hold an object
}
