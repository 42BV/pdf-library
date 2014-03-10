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
     */
    void addText(Text text);

    /**
     * Returns list of all the text objects in this paragraph.
     * @return List containing all text objects in this paragraph.
     */
    List<Text> getTextCollection();

}
