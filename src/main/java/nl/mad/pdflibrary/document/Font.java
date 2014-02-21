package nl.mad.pdflibrary.document;

/**
 * Font class containing the font family and style. 
 * @author Dylan de Wolff
 *
 */
public class Font extends AbstractDocumentPart {
    private FontFamily family;
    private FontStyle style;
    //temp attribute!
    private String filename;

    /**
     * Create a new instance of Font.
     * @param family Font family.
     * @param style Style of font (bold, italic).
     */
    public Font(FontFamily family, FontStyle style) {
        super(DocumentPartType.FONT);
        this.setFamily(family);
        this.setStyle(style);
    }

    //for testing purposes
    public Font(String filename) {
        super(DocumentPartType.FONT);
        this.filename = filename;
    }

    public FontFamily getFamily() {
        return family;
    }

    public void setFamily(FontFamily family) {
        this.family = family;
    }

    public FontStyle getStyle() {
        return style;
    }

    public void setStyle(FontStyle style) {
        this.style = style;
    }
}
