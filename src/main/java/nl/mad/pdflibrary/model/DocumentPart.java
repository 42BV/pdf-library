package nl.mad.pdflibrary.model;

/**
 * Interface for DocumentParts. These parts can be added to an instance of Document.
 * @author Dylan de Wolff
 *
 */
public interface DocumentPart {

    /**
     * Returns the type of this DocumentPart.
     * @return DocumentPartType specifying the type of this part.
     */
    DocumentPartType getType();

    /**
     * Adds observer to the document part.
     * @param o Observer to add.
     */
    void addChangeObserver(Observer o);

    /**
     * Removes observer from the document part.
     * @param o Observer to remove.
     */
    void removeChangeObserver(Observer o);
}
