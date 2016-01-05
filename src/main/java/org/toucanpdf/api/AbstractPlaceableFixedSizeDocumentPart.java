package org.toucanpdf.api;

import org.toucanpdf.model.DocumentPartType;
import org.toucanpdf.model.PlaceableFixedSizeDocumentPart;

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
    protected boolean wrappingAllowed = false;

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

    @Override
    public void setWrappingAllowed(boolean wrapping) {
        this.wrappingAllowed = wrapping;
    }

    @Override
    public boolean isWrappingAllowed() {
        return this.wrappingAllowed;
    }
}
