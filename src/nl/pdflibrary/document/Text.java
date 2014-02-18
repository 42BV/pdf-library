package nl.pdflibrary.document;

import java.awt.Font;

/**
 * This is the text document part, storing all the data necessary for showing text in a document.
 * 
 * @author Dylan de Wolff
 * @see AbstractDocumentPart
 */
public class Text extends AbstractDocumentPart {
    private String text;
    private Font font;

    /**
     * Creates a new text instance.
     * 
     * @param text The text that needs to be shown
     * @param font The font that's used for the text
     * @param posX The x position of the text
     * @param posY The y position of the text
     */
    public Text(String text, Font font, int posX, int posY) {
        super(DocumentPartType.TEXT);
        this.text = text;
        this.font = font;
        this.setPositionX(posX);
        this.setPositionY(posY);
    }

    /**
     * Creates a new text instance.
     * @param text The text that needs to be shown
     * @param posX The x position of the text
     * @param posY The y position of the text
     */
    public Text(String text, int posX, int posY) {
        super(DocumentPartType.TEXT);
        this.text = text;
        this.setPositionX(posX);
        this.setPositionY(posY);
        this.font = Document.DEFAULT_FONT;
    }

    /**
     * Creates a new text instance.
     */
    public Text() {
        super(DocumentPartType.TEXT);
        this.text = "";
        this.setPositionX(0);
        this.setPositionY(0);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
