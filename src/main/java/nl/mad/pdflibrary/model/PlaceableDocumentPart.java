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
     * Returns the height of this part's content. This value will not always be representative of the actual height of the part if the position 
     * was not specified by the user.
     * @param page page the content will be on.
     * @return int containing height.
     */
    int getContentHeight(Page page);

    /**
     * Returns the width of this part's content. This value will not always be representative of the actual width of the part if the position 
     * was not specified by the user.
     * @param page Page the content will be on.
     * @param position Position to check the width of.
     * @return int containing width.
     */
    int getContentWidth(Page page, Position position);

    //    /**
    //     * Checks if this document part intersects with the given document part.
    //     * @param part Part to check for possible intersections.
    //     * @return True if the two parts intersect, false otherwise.
    //     */
    //    boolean intersectsWith(PlaceableDocumentPart part);
}
