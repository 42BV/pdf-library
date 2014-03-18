package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;

/**
 * Paragraph contains a collection of text objects. This ensures that the text objects are placed together in the document.
 * This also makes it unnecessary to specify positions for the text objects. The paragraph should be filled up before being
 * added to the document. Changes made to the paragraph after adding it to the document will not be processed.
 * @author Dylan de Wolff
 */
public class BaseParagraph extends AbstractPlaceableDocumentPart implements Paragraph {

    private List<Text> textCollection;

    /**
     * Creates a new instance of Paragraph with automatic positioning.
     */
    public BaseParagraph() {
        this(new Position());
    }

    /**
     * Creates a new instance of Paragraph.
     * @param position Position of the paragraph.
     */
    public BaseParagraph(Position position) {
        super(DocumentPartType.PARAGRAPH);
        textCollection = new ArrayList<Text>();
        this.setPosition(position);
    }

    /**
     * Creates a Text object using the given values and adds it to the newly made paragraph afterwards.
     * @param text Text to be added to the text object. The paragraph will be positioned automatically.
     * @param textSize Size of the text.
     * @param font Font to be used.
     */
    public BaseParagraph(String text, int textSize, Font font) {
        this(text, textSize, font, new Position());
    }

    /**
     * Creates a Text object using the given values and adds it to the newly made paragraph afterwards.
     * The paragraph is positioned at the given values.
     * @param text Text to be added to the text object. The paragraph will be positioned automatically.
     * @param textSize Size of the text.
     * @param font Font to be used.
     * @param position position of the paragraph.
     */
    public BaseParagraph(String text, int textSize, Font font, Position position) {
        this(position);
        textCollection.add(new BaseText(text, textSize, font));
    }

    public void addText(Text text) {
        this.textCollection.add(text);
    }

    public List<Text> getTextCollection() {
        return this.textCollection;
    }
}