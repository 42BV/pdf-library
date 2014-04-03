package nl.mad.pdflibrary.api;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Observable;
import nl.mad.pdflibrary.model.Observer;

/**
 * AbstractDocumentPart is the abstract class for all the different document parts that can be added to an instance of Document.
 * @author Dylan de Wolff
 * @see Document
 */
public abstract class AbstractDocumentPart extends Observable implements DocumentPart {
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

    @Override
    public void addChangeObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void removeChangeObserver(Observer o) {
        this.deleteObserver(o);
    }
}
