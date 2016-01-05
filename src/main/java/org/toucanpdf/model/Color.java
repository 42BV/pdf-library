package org.toucanpdf.model;

/**
 * This class can be used to set the color of text. You can either use a custom color by creating a new instance of this class or use one of the default colors.
 */
public class Color {
    private double red, green, blue;

    public static final Color RED = new Color(1, 0, 0);
    public static final Color GREEN = new Color(0, 1, 0);
    public static final Color BLUE = new Color(0, 0, 1);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1);

    public Color(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }

    @Override
    public String toString() {
        return red + " " + green + " " + blue;
    }
}
