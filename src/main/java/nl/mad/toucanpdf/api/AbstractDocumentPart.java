package nl.mad.toucanpdf.api;

import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.DocumentPartType;

/**
 * AbstractDocumentPart is the abstract class for all the different document parts that can be added to an instance of Document.
 * @author Dylan de Wolff
 * @see Document
 */
public abstract class AbstractDocumentPart implements DocumentPart {
    /**
     * Type of this part.
     */
    private DocumentPartType type;

    /**
     * Creates a new instance of AbstractDocumentPart.
     * @param type Type of document part.
     */
    public AbstractDocumentPart(DocumentPartType type) {
        this.type = type;
    }

    @Override
    public DocumentPartType getType() {
        return type;
    }
}
