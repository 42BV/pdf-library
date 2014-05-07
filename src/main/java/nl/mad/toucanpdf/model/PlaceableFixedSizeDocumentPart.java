package nl.mad.toucanpdf.model;

/**
 * This interface specifies methods that should be implemented by document parts that can have a custom position and have a preset size.
 * @author Dylan de Wolff
 *
 */
public interface PlaceableFixedSizeDocumentPart extends PlaceableDocumentPart {

    /**
     * Returns the width of the part.
     * @return Width of the part.
     */
    int getWidth();

    /**
     * Returns the height of the part.
     * @return Height of the part.
     */
    int getHeight();

    /**
     * Returns whether or not other parts are allowed to wrap around this part.
     * @return boolean containing true if wrapping is allowed, false otherwise.
     */
    boolean wrappingAllowed();
}
