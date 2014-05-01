package nl.mad.toucanpdf.api;

import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.PlaceableFixedSizeDocumentPart;

public abstract class AbstractPlaceableFixedSizeDocumentPart extends AbstractPlaceableDocumentPart implements PlaceableFixedSizeDocumentPart {
    protected int height;
    protected int width;

    public AbstractPlaceableFixedSizeDocumentPart(DocumentPartType type) {
        super(type);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

}
