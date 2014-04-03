package nl.mad.pdflibrary.model;

import java.util.List;

/**
 * Interface for paragraph classes. Paragraph objects store a collection of other document parts that should be kept together.
 * @author Dylan de Wolff
 */
public interface Paragraph extends PlaceableDocumentPart {

    /**
     * Adds text to the paragraph.
     * @param text Text to be added.
     * @return the paragraph.
     */
    Paragraph addText(Text text);

    /**
     * @param x The x position to place the paragraph.
     * @param y The y position to place the paragraph.
     * @return the paragraph.
     */
    Paragraph on(int x, int y);

    /**
     * @param position The position to place the paragraph.
     * @return the paragraph.
     */
    Paragraph on(Position position);

    /**
     * Returns list of all the text objects in this paragraph.
     * @return List containing all text objects in this paragraph.
     */
    List<Text> getTextCollection();

    /**
     * Calculates the size of the content within the paragraph.
     * @param page Page the paragraph is on.
     */
    void processContentSize(Page page);

}
