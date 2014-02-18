package nl.pdflibrary.document;

import java.util.ArrayList;

/**
 * Paragraph contains a collection of text objects. This ensures that the text objects are placed together in the document.
 * This also makes it unnecessary to specify positions for the text objects.
 * @author Dylan de Wolff
 */
public class Paragraph extends AbstractDocumentPart {
    private ArrayList<Text> textCollection;

    /**
     * Creates a new instance of Paragraph
     * @param positionX
     * @param positionY
     */
    public Paragraph(int positionX, int positionY) {
        super(DocumentPartType.PARAGRAPH);
        textCollection = new ArrayList<Text>();
        this.setPositionX(positionX);
        this.setPositionY(positionY);
    }

    public void addText(Text text) {
        text.setPositionX(this.getPositionX());
        text.setPositionY(this.getPositionY());
        this.textCollection.add(text);
    }

    public ArrayList<Text> getTextCollection() {
        return this.textCollection;
    }

}