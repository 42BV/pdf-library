package nl.mad.pdflibrary.api;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Paragraph contains a collection of text objects. This ensures that the text objects are placed together in the api.
 * This also makes it unnecessary to specify positions for the text objects.
 * @author Dylan de Wolff
 */
public class AbstractParagraph extends AbstractPlaceableDocumentPart implements Paragraph {

    private List<Text> textCollection;

    /**
     * Creates a new instance of Paragraph.
     */
    public AbstractParagraph() {
        this(0, 0);
        this.setCustomPositioning(false);
    }

    /**
     * Creates a new instance of Paragraph.
     * @param positionX x-position from the lower left corner.
     * @param positionY y-position from the lower left corner.
     */
    public AbstractParagraph(int positionX, int positionY) {
        super(DocumentPartType.PARAGRAPH);
        textCollection = new ArrayList<>();
        this.setPositionX(positionX);
        this.setPositionY(positionY);
    }

    public void addText(Text text) {
        this.textCollection.add(text);
    }

    public List<Text> getTextCollection() {
        return this.textCollection;
    }
}