package nl.mad.toucanpdf.model;

/**
 * Anchor represents the attachment of a document part, such as an image or table, to text within a paragraph. You can only attach
 * document parts that have a predefined height and width (parts that extend PlaceableFixedSizeDocumentPart).
 * @author Dylan de Wolff
 *
 */
public interface Anchor {
    /**
     * @return the part this anchor is attaching.
     */
    PlaceableFixedSizeDocumentPart getPart();

    /**
     * @return the object this anchor is attached to.
     */
    Text getAnchorPoint();

    /**
     * @return the location
     */
    AnchorLocation getLocation();

    /**
     * Positions the part above the given anchor point.
     * @param newAnchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    Anchor above(Text newAnchorPoint);

    /**
     * Positions the part beneath the given anchor point.
     * @param newAnchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    Anchor beneath(Text newAnchorPoint);

    /**
     * Positions the part to the left of the given anchor point.
     * @param newAnchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    Anchor leftOf(Text newAnchorPoint);

    /**
     * Positions the part to the right of the given anchor point.
     * @param newAnchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    Anchor rightOf(Text newAnchorPoint);

    /**
     * Sets the part the anchor will attach.
     * @param documentPart the part the anchor will attach.
     * @return the anchor.
     */
    Anchor part(PlaceableFixedSizeDocumentPart documentPart);
}
