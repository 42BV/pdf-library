package nl.mad.pdflibrary.model;

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
    FontFamily getFamily();

    /**
     * Sets the family of this font.
     * @param family Family of the font.
     */
    void setFamily(FontFamily family);

    /**
     * Returns the font style.
     * @return FontStyle specifying the style of this font.
     */
    FontStyle getStyle();

    /**
     * Sets the font style.
     * @param style Style of the font.
     */
    void setStyle(FontStyle style);

    /**
     * Returns the base font family of this font.
     * @return BaseFontFamily for this font.
     */
    BaseFontFamily getBaseFontFamily();

}
