package org.toucanpdf.model;

/**
 * Interface for font classes.
 * @author Dylan de Wolff
 *
 */
public interface Font extends DocumentPart {

    /**
     * Returns the family of this font.
     * @return FontFamily specifying the family of this font.
     */
    FontFamilyType getFamily();

    /**
     * Sets the family of this font.
     * @param family Family of the font.
     * @return the font.
     */
    Font family(FontFamilyType family);

    /**
     * Returns the font style.
     * @return FontStyle specifying the style of this font.
     */
    FontStyle getStyle();

    /**
     * Sets the font style.
     * @param style Style of the font.
     * @return the font.
     */
    Font style(FontStyle style);

    /**
     * Sets the font style to bold.
     * @return the font.
     */
    Font bold();

    /**
     * Sets the font style to italic.
     * @return the font.
     */
    Font italic();

    /**
     * Sets the font style to bold-italic.
     * @return the font.
     */
    Font boldItalic();

    /**
     * Returns the base font family of this font.
     * @return BaseFontFamily for this font.
     */
    FontFamily getFontFamily();

    /**
     * Returns the height of a line using this font for the given text size.
     * @param textSize size of the text to get the height for.
     * @return double containing height value.
     */
    double getLineHeight(int textSize);

    /**
     * Returns the metrics corresponding to this font.
     * @return FontMetrics corresponding to this font.
     */
    FontMetrics getMetrics();
}
