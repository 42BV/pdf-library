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
    protected int marginLeft = 0;
    protected int marginRight = 0;
    protected int marginTop = 0;
    protected int marginBottom = 0;

    /**
     * Creates a new instance of AbstractPlaceableDocumentPart.
     * @param type Type of document part.
     */
    public AbstractPlaceableDocumentPart(DocumentPartType type) {
        super(type);
        this.position = new Position();
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

    @Override
    public int getMarginLeft() {
        return this.marginLeft;
    }

    @Override
    public int getMarginRight() {
        return this.marginRight;
    }

    @Override
    public int getMarginTop() {
        return this.marginTop;
    }

    @Override
    public int getMarginBottom() {
        return this.marginBottom;
    }

    @Override
    public void setMarginLeft(int marginLeft) {
        if (marginLeft >= 0) {
            this.marginLeft = marginLeft;
        }
    }

    @Override
    public void setMarginRight(int marginRight) {
        if (marginRight >= 0) {
            this.marginRight = marginRight;
        }
    }

    @Override
    public void setMarginTop(int marginTop) {
        if (marginTop >= 0) {
            this.marginTop = marginTop;
        }
    }

    @Override
    public void setMarginBottom(int marginBottom) {
        if (marginBottom >= 0) {
            this.marginBottom = marginBottom;
        }
    }
}
