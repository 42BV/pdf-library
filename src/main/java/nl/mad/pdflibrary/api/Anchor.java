package nl.mad.pdflibrary.api;

import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Text;

/**
 * Anchor represents the attachment of a document part, such as an image or table, to text within a paragraph.
 * @author Dylan de Wolff
 *
 */
public class Anchor {
    private PlaceableDocumentPart part;
    private Text anchorPoint;
    private AnchorLocation location;

    /**
     * Creates a new instance of anchor.
     * @param part The document part you wish to attach to an anchor point.
     */
    public Anchor(PlaceableDocumentPart part) {
        this.part = part;
        location = AnchorLocation.BELOW;
    }

    /**
     * Creates a copy of the given anchor object.
     * @param a The anchor to copy.
     * @param anchorPoint The new anchor point.
     */
    public Anchor(Anchor a, Text anchorPoint) {
        //TODO: fix copying of part
        this.part = a.getPart().copy();
        this.location = a.getLocation();
        this.anchorPoint = anchorPoint;
    }

    /**
     * @return the part this anchor is attaching.
     */
    public PlaceableDocumentPart getPart() {
        return part;
    }

    /**
     * @return the object this anchor is attached to.
     */
    public PlaceableDocumentPart getAnchorPoint() {
        return anchorPoint;
    }

    /**
     * @return the location
     */
    public AnchorLocation getLocation() {
        return location;
    }

    /**
     * Positions the part above the given anchor point.
     * @param anchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    public Anchor above(Text anchorPoint) {
        this.anchorPoint = anchorPoint;
        this.location = AnchorLocation.ABOVE;
        return this;
    }

    /**
     * Positions the part beneath the given anchor point.
     * @param anchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    public Anchor beneath(Text anchorPoint) {
        this.anchorPoint = anchorPoint;
        this.location = AnchorLocation.BELOW;
        return this;
    }

    /**
     * Positions the part to the left of the given anchor point.
     * @param anchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    public Anchor leftOf(Text anchorPoint) {
        this.anchorPoint = anchorPoint;
        this.location = AnchorLocation.LEFT;
        return this;
    }

    /**
     * Positions the part to the right of the given anchor point.
     * @param anchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    public Anchor rightOf(Text anchorPoint) {
        this.anchorPoint = anchorPoint;
        this.location = AnchorLocation.RIGHT;
        return this;
    }
}
