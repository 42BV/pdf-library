package nl.mad.pdflibrary.document;

import java.awt.Font;

/**
 * This is the text document part, storing all the data necessary for showing text in a document.
 * 
 * @author Dylan de Wolff
 * @see AbstractDocumentPart
 */
public class Text extends AbstractPlaceableDocumentPart {
    private String textString;
    private Font font;

    /**
     * Creates a new text instance.
     */
    public Text() {
        this("", Document.DEFAULT_FONT, 0, 0);
        this.setCustomPositioning(false);
    }

    /**
     * Creates a new text instance. 
     * 
     * @param text The text that needs to be shown.
     */
    public Text(String text) {
        this(text, Document.DEFAULT_FONT, 0, 0);
        this.setCustomPositioning(false);
    }

    /**
     * Creates a new text instance.
     * 
     * @param text The text that needs to be shown.
     * @param font The font that's used for the text.
     */
    public Text(String text, Font font) {
        this(text, font, 0, 0);
        this.setCustomPositioning(false);
    }

    /**
     * Creates a new text instance.
     * @param text The text that needs to be shown.
     * @param posX The x position of the text from the lower left corner.
     * @param posY The y position of the text from the lower left corner.
     */
    public Text(String text, int posX, int posY) {
        this(text, Document.DEFAULT_FONT, posX, posY);
    }

    /**
     * Creates a new text instance.
     * 
     * @param text The text that needs to be shown.
     * @param font The font that's used for the text.
     * @param posX The x position of the text from the lower left corner.
     * @param posY The y position of the text from the lower left corner.
     */
    public Text(String text, Font font, int posX, int posY) {
        super(DocumentPartType.TEXT);
        this.textString = text;
        this.font = font;
        this.setPositionX(posX);
        this.setPositionY(posY);
    }

    public String getText() {
        return textString;
    }

    public void setText(String text) {
        this.textString = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
