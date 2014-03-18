package nl.mad.pdflibrary.model;

public class Position {
    private int x;
    private int y;
    public static final int UNUSED_VALUE = -1;

    public Position() {
        this(UNUSED_VALUE, UNUSED_VALUE);
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean hasCustomPosition() {
        return (x != UNUSED_VALUE || y != UNUSED_VALUE);
    }
}
