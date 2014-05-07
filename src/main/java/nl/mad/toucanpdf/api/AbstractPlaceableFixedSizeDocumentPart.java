package nl.mad.toucanpdf.api;

import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.PlaceableFixedSizeDocumentPart;

/**
 * AbstractPlaceableFixedSizeDocumentPart is the parent class for document parts that can be positioned freely
 * and have a preset height and width such as images. 
 * 
 * @author Dylan de Wolff
 */
public abstract class AbstractPlaceableFixedSizeDocumentPart extends AbstractPlaceableDocumentPart implements PlaceableFixedSizeDocumentPart {
    /**
     * Height of the object.
     */
    protected int height;
    /**
     * Width of the object.
     */
    protected int width;

    /**
     * Creates a new instance with the given type.
     * @param type Object type.
     */
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
