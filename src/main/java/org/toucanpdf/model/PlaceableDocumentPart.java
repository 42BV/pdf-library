package org.toucanpdf.model;

/**
 * Interface for placeable document parts. This extends DocumentPart and specifies methods for positioning the DocumentPart.
 * @author Dylan de Wolff
 *
 */
public interface PlaceableDocumentPart extends DocumentPart {

    /**
     * Retrieves the position of this documentPart.
     * @return Position of this object.
     */
    Position getPosition();

    /**
     * Sets the position of the document part.
     * @param x The x value of the position.
     * @param y The y value of the position.
     * @return this image instance.
     */
    PlaceableDocumentPart on(int x, int y);

    /**
     * Sets the position of the image.
     * @param position The position to use.
     * @return this image instance.
     */
    PlaceableDocumentPart on(Position position);

    /**
     * Sets the alignment of the document part.
     * @param alignment Alignment to use.
     * @return this document part instance.
     */
    PlaceableDocumentPart align(Alignment alignment);

    /**
     * Returns the alignment of the part.
     * @return Alignment used by the part.
     */
    Alignment getAlignment();

    /**
     * Creates a copy of the object.
     * @return Copy of the object.
     */
    PlaceableDocumentPart copy();

    /**
     * Returns the margin to the left of the part.
     * @return int containing margin value.
     */
    int getMarginLeft();

    /**
     * Returns the margin to the right of the part.
     * @return int containing margin value.
     */
    int getMarginRight();

    /**
     * Returns the margin above the part.
     * @return int containing margin value.
     */
    int getMarginTop();

    /**
     * Returns the margin below the part.
     * @return int containing margin value.
     */
    int getMarginBottom();

    /**
     * Sets the top margin of the document part.
     * @param marginTop the top margin to set
     * @return This document part instance.
     */
    PlaceableDocumentPart marginTop(int marginTop);

    /**
     * Sets the bottom margin of the document part.
     * @param marginBottom the bottom margin to set
     * @return This document part instance.
     */
    PlaceableDocumentPart marginBottom(int marginBottom);

    /**
     * Sets the right margin of the document part.
     * @param marginRight the right margin to set
     * @return This document part instance.
     */
    PlaceableDocumentPart marginRight(int marginRight);

    /**
     * Sets the left margin of the document part.
     * @param marginLeft the left margin to set
     * @return This document part instance.
     */
    PlaceableDocumentPart marginLeft(int marginLeft);
}
