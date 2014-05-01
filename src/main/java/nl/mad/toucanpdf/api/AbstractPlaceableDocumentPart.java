package nl.mad.toucanpdf.api;

import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;

/**
 * AbstractPlaceableDocumentPart is an extension of AbstractDocumentPart that allows for positioning of the object.
 * 
 * @author Dylan de Wolff
 * @see AbstractDocumentPart
 */
public abstract class AbstractPlaceableDocumentPart extends AbstractDocumentPart implements PlaceableDocumentPart {
    private Position position;
    private Alignment alignment = Alignment.LEFT;

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

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    protected void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }
}
