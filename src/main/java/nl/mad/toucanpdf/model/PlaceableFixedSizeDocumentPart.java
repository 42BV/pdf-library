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
    double getWidth();

    /**
     * Returns the height of the part.
     * @return Height of the part.
     */
    double getHeight();

    /**
     * Returns whether or not other parts are allowed to wrap around this part.
     * @return boolean containing true if wrapping is allowed, false otherwise.
     */
    boolean isWrappingAllowed();

	/**
	 * Sets whether other objects are allowed to wrap around this object.
	 * @param wrapping True if you want to allow other objects to wrap, false otherwise.
	 */
	void setWrappingAllowed(boolean wrapping);
}
