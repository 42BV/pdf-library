package org.toucanpdf.font;

/**
 * Class representing the metrics of a single character. 
 * @author Dylan de Wolff
 */
public class Type1CharacterMetric {
    private int c;
    private int wx;
    private String name;
    private int[] boundingBox;

    /**
     * Default value for the character code.
     */
    public static final int DEFAULT_C_VALUE = -1;
    /**
     * Default value for the width.
     */
    public static final int DEFAULT_WX_VALUE = 250;

    /**
     * Creates a new instance of CharacterMetric.
     * @param c Character code.
     * @param wx Character width.
     * @param name Name of the character.
     * @param boundingBox Bounding box of the character.
     */
    public Type1CharacterMetric(int c, int wx, String name, int[] boundingBox) {
        this.c = c;
        this.wx = wx;
        this.name = name;
        this.boundingBox = boundingBox.clone();
    }

    public int getC() {
        return c;
    }

    public int getWx() {
        return wx;
    }

    public String getName() {
        return name;
    }

    public int[] getBoundingBox() {
        return boundingBox.clone();
    }
}
