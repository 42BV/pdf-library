package org.toucanpdf.api;

import org.toucanpdf.model.*;

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
    public PlaceableDocumentPart on(Position position) {
        this.position = position;
        return this;
    }

    @Override
    public PlaceableDocumentPart on(int x, int y) {
        return this.on(new Position(x, y));
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public PlaceableDocumentPart align(Alignment alignment) {
        this.alignment = alignment;
        return this;
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
    public PlaceableDocumentPart marginLeft(int marginLeft) {
        if (marginLeft >= 0) {
            this.marginLeft = marginLeft;
        }
        return this;
    }

    @Override
    public PlaceableDocumentPart marginRight(int marginRight) {
        if (marginRight >= 0) {
            this.marginRight = marginRight;
        }
        return this;
    }

    @Override
    public PlaceableDocumentPart marginTop(int marginTop) {
        if (marginTop >= 0) {
            this.marginTop = marginTop;
        }
        return this;
    }

    @Override
    public PlaceableDocumentPart marginBottom(int marginBottom) {
        if (marginBottom >= 0) {
            this.marginBottom = marginBottom;
        }
        return this;
    }
}
