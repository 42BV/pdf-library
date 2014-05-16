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
    protected double height;
    /**
     * Width of the object.
     */
    protected double width;

    /**
     * Creates a new instance with the given type.
     * @param type Object type.
     */
    public AbstractPlaceableFixedSizeDocumentPart(DocumentPartType type) {
        super(type);
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

}
