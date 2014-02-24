package nl.mad.pdflibrary.api;

/**
 * DocumentPart is the abstract class for all the different api parts that can be added to an instance of Document.
 * @author Dylan de Wolff
 * @see Document
 */
public abstract class AbstractDocumentPart {
    /**
     * Type of this part.
     */
    private DocumentPartType type;

    /**
     * Creates a new instance of AbstractDocumentPart.
     * @param type Type of api part.
     */
    public AbstractDocumentPart(DocumentPartType type) {
        this.type = type;
    }

    public DocumentPartType getType() {
        return type;
    }
}
