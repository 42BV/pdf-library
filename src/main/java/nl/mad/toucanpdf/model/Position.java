package nl.mad.toucanpdf.model;

/**
 * This class stores the position for document parts.
 * @author Dylan de Wolff
 *
 */
public class Position {
    private double x;
    private double y;
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
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a copy of the given position object.
     * @param position Position to copy.
     */
    public Position(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Adjusts the Y value by the given amount.
     * @param adjustment Adjustment to apply.
     */
    public void adjustY(double adjustment) {
        this.y += adjustment;
    }

    /**
     * Adjusts the X value by the given amount.
     * @param adjustment Adjustment to apply.
     */
    public void adjustX(double adjustment) {
        this.x += adjustment;
    }

    /**
     * Checks if this Position has custom positioning.
     * @return True if either x or y are not equal to the default unused value, false otherwise.
     */
    public boolean hasCustomPosition() {
        return (x != UNUSED_VALUE || y != UNUSED_VALUE);
    }

    /**
     * Checks if the y value has been set to a custom value.
     * @return True if y is not equal to the default unused value, false otherwise.
     */
    public boolean hasCustomYValue() {
        return y != UNUSED_VALUE;
    }

    /**
     * Checks if the x value has been set to a custom value.
     * @return True if x is not equal to the default unused value, false otherwise.
     */
    public boolean hasCustomXValue() {
        return x != UNUSED_VALUE;
    }

    @Override
    public String toString() {
        return x + ":" + y;
    }
}
