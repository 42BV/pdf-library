package org.toucanpdf.api;

import org.toucanpdf.model.DocumentPart;
import org.toucanpdf.model.DocumentPartType;

/**
 * AbstractDocumentPart is the abstract class for all the different document parts that can be added to an instance of DocumentBuilder.
 * @author Dylan de Wolff
 * @see org.toucanpdf.DocumentBuilder
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
