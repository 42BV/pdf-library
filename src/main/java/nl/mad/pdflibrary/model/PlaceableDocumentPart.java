package nl.mad.pdflibrary.model;


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
}
