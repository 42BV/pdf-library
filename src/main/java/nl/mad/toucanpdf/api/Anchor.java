package nl.mad.toucanpdf.api;

import nl.mad.toucanpdf.model.PlaceableFixedSizeDocumentPart;
import nl.mad.toucanpdf.model.Text;

/**
 * Anchor represents the attachment of a document part, such as an image or table, to text within a paragraph. You can only attach
 * document parts that have a predefined height and width (parts that extend PlaceableFixedSizeDocumentPart).
 * @author Dylan de Wolff
 *
 */
public class Anchor {
    private PlaceableFixedSizeDocumentPart part;
    private Text anchorPoint;
    private AnchorLocation location;

    /**
     * Creates a new instance of anchor.
     * @param part The document part you wish to attach to an anchor point.
     */
    public Anchor(PlaceableFixedSizeDocumentPart part) {
        this.part = part;
        location = AnchorLocation.BELOW;
    }

    /**
     * Creates a copy of the given anchor object.
     * @param a The anchor to copy.
     * @param anchorPoint The new anchor point.
     */
    public Anchor(Anchor a, Text anchorPoint) {
        this.part = (PlaceableFixedSizeDocumentPart) a.getPart().copy();
        this.location = a.getLocation();
        this.anchorPoint = anchorPoint;
    }

    /**
     * @return the part this anchor is attaching.
     */
    public PlaceableFixedSizeDocumentPart getPart() {
        return part;
    }

    /**
     * @return the object this anchor is attached to.
     */
    public Text getAnchorPoint() {
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
     * @param newAnchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    public Anchor above(Text newAnchorPoint) {
        this.anchorPoint = newAnchorPoint;
        this.location = AnchorLocation.ABOVE;
        return this;
    }

    /**
     * Positions the part beneath the given anchor point.
     * @param newAnchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    public Anchor beneath(Text newAnchorPoint) {
        this.anchorPoint = newAnchorPoint;
        this.location = AnchorLocation.BELOW;
        return this;
    }

    /**
     * Positions the part to the left of the given anchor point.
     * @param newAnchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    public Anchor leftOf(Text newAnchorPoint) {
        this.anchorPoint = newAnchorPoint;
        this.location = AnchorLocation.LEFT;
        return this;
    }

    /**
     * Positions the part to the right of the given anchor point.
     * @param newAnchorPoint the text to attach the anchor to.
     * @return the anchor.
     */
    public Anchor rightOf(Text newAnchorPoint) {
        this.anchorPoint = newAnchorPoint;
        this.location = AnchorLocation.RIGHT;
        return this;
    }

    /**
     * Sets the part the anchor will attach.
     * @param documentPart the part the anchor will attach.
     * @return the anchor.
     */
    public Anchor part(PlaceableFixedSizeDocumentPart documentPart) {
        this.part = documentPart;
        return this;
    }
}
