package nl.mad.toucanpdf.model;

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
}
