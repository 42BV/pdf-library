package nl.mad.pdflibrary.model;

public class Position {
    private int x;
    private int y;
    /**
     * Default value used when no value was specified.
     */
    public static final int UNUSED_VALUE = -1;

    /**
     * Creates a new instance of position without specifying the x or y values.
     */
    public Position() {
        this(UNUSED_VALUE, UNUSED_VALUE);
    }

    /**
     * Creates a new instance of position.
     * @param x The x value.
     * @param y The y value.
     */
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

    /**
     * Checks if this Position has custom positioning.
     * @return True if either x or y are not equal to the default unused value, false otherwise.
     */
    public boolean hasCustomPosition() {
        return (x != UNUSED_VALUE || y != UNUSED_VALUE);
    }
}
