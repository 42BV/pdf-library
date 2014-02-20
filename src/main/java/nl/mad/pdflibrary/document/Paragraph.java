package nl.mad.pdflibrary.document;

import java.util.ArrayList;

/**
 * Paragraph contains a collection of text objects. This ensures that the text objects are placed together in the mad.
 * This also makes it unnecessary to specify positions for the text objects.
 * @author Dylan de Wolff
 */
public class Paragraph extends AbstractDocumentPart {
    private ArrayList<Text> textCollection;
    private boolean customPositioning = true;

    /**
     * Creates a new instance of Paragraph.
     */
    public Paragraph() {
        this(0, 0);
        customPositioning = false;
    }

    /**
     * Creates a new instance of Paragraph.
     * @param positionX x-position from the lower left corner
     * @param positionY y-position from the lower left corner
     */
    public Paragraph(int positionX, int positionY) {
        super(DocumentPartType.PARAGRAPH);
        textCollection = new ArrayList<Text>();
        this.setPositionX(positionX);
        this.setPositionY(positionY);
    }

    public void addText(Text text) {
        this.textCollection.add(text);
    }

    public ArrayList<Text> getTextCollection() {
        return this.textCollection;
    }

    public boolean getCustomPositioning() {
        return this.customPositioning;
    }
}