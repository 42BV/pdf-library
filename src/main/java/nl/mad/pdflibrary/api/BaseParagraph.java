package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;

/**
 * Paragraph contains a collection of text objects. This ensures that the text objects are placed together in the api.
 * This also makes it unnecessary to specify positions for the text objects.
 * @author Dylan de Wolff
 */
public class BaseParagraph extends AbstractPlaceableDocumentPart implements Paragraph {

    private List<Text> textCollection;

    /**
     * Creates a new instance of Paragraph with automatic positioning.
     */
    public BaseParagraph() {
        this(0, 0);
        this.setCustomPositioning(false);
    }

    /**
     * Creates a new instance of Paragraph.
     * @param positionX x-position from the lower left corner.
     * @param positionY y-position from the lower left corner.
     */
    public BaseParagraph(int positionX, int positionY) {
        super(DocumentPartType.PARAGRAPH);
        textCollection = new ArrayList<Text>();
        this.setPositionX(positionX);
        this.setPositionY(positionY);
    }

    /**
     * Creates a Text object using the given values and adds it to the newly made paragraph afterwards.
     * @param text Text to be added to the text object. The paragraph will be positioned automatically.
     * @param textSize Size of the text.
     * @param font Font to be used.
     */
    public BaseParagraph(String text, int textSize, Font font) {
        this(0, 0);
        textCollection.add(new BaseText(text, textSize, font));
        this.setCustomPositioning(false);
    }

    /**
     * Creates a Text object using the given values and adds it to the newly made paragraph afterwards.
     * The paragraph is positioned at the given values.
     * @param text Text to be added to the text object. The paragraph will be positioned automatically.
     * @param textSize Size of the text.
     * @param font Font to be used.
     * @param positionX x-position from the lower left corner.
     * @param positionY y-position from the lower left corner.
     */
    public BaseParagraph(String text, int textSize, Font font, int positionX, int positionY) {
        this(0, 0);
        textCollection.add(new BaseText(text, textSize, font));
        this.setCustomPositioning(false);
    }

    public void addText(Text text) {
        this.textCollection.add(text);
    }

    public List<Text> getTextCollection() {
        return this.textCollection;
    }
}