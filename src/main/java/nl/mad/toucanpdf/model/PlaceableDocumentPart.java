package nl.mad.toucanpdf.model;

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
     * Sets the position to the given position.
     * @param position Position to use.
     */
    void setPosition(Position position);

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
     * Sets the left margin for this object.
     * @param marginLeft Margin to use.
     */
    void setMarginLeft(int marginLeft);

    /**
     * Sets the right margin for this object.
     * @param marginRight Margin to use.
     */
    void setMarginRight(int marginRight);

    /**
     * Sets the top margin for this object.
     * @param marginTop Margin to use.
     */
    void setMarginTop(int marginTop);

    /**
     * Sets the bottom margin for this object.
     * @param marginBottom Margin to use.
     */
    void setMarginBottom(int marginBottom);
}
