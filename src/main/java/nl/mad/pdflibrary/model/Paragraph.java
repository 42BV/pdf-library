package nl.mad.pdflibrary.model;

import java.util.List;

/**
 * Interface for paragraph classes.
 * @author Dylan de Wolff
 *
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
