package nl.mad.pdflibrary.api;

import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Position;

/**
 * AbstractPlaceableDocumentPart is an extension of AbstractDocumentPart that allows for positioning of the object.
 * 
 * @author Dylan de Wolff
 * @see AbstractDocumentPart
 */
public abstract class AbstractPlaceableDocumentPart extends AbstractDocumentPart implements PlaceableDocumentPart {
    private Position position;

    /**
     * Creates a new instance of AbstractPlaceableDocumentPart.
     * @param type Type of document part.
     */
    public AbstractPlaceableDocumentPart(DocumentPartType type) {
        super(type);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}
