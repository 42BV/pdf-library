package org.toucanpdf.api;

import org.toucanpdf.model.Anchor;
import org.toucanpdf.model.AnchorLocation;
import org.toucanpdf.model.PlaceableFixedSizeDocumentPart;
import org.toucanpdf.model.Text;

/**
 * Base implementation of the anchor interface.
 * @author Dylan de Wolff
 * @see Anchor
 *
 */
public class BaseAnchor implements Anchor {
    private PlaceableFixedSizeDocumentPart part;
    private Text anchorPoint;
    private AnchorLocation location;

    /**
     * Creates a new instance of anchor.
     * @param part The document part you wish to attach to an anchor point.
     */
    public BaseAnchor(PlaceableFixedSizeDocumentPart part) {
        this.part = part;
        location = AnchorLocation.BELOW;
    }

    /**
     * Creates a copy of the given anchor object.
     * @param a The anchor to copy.
     * @param anchorPoint The new anchor point.
     */
    public BaseAnchor(Anchor a, Text anchorPoint) {
        this.part = (PlaceableFixedSizeDocumentPart) a.getPart().copy();
        this.location = a.getLocation();
        this.anchorPoint = anchorPoint;
    }

    @Override
    public PlaceableFixedSizeDocumentPart getPart() {
        return part;
    }

    @Override
    public Text getAnchorPoint() {
        return anchorPoint;
    }

    @Override
    public AnchorLocation getLocation() {
        return location;
    }

    @Override
    public Anchor above(Text newAnchorPoint) {
        this.anchorPoint = newAnchorPoint;
        this.location = AnchorLocation.ABOVE;
        return this;
    }

    @Override
    public Anchor beneath(Text newAnchorPoint) {
        this.anchorPoint = newAnchorPoint;
        this.location = AnchorLocation.BELOW;
        return this;
    }

    @Override
    public Anchor leftOf(Text newAnchorPoint) {
        this.anchorPoint = newAnchorPoint;
        this.location = AnchorLocation.LEFT;
        return this;
    }

    @Override
    public Anchor rightOf(Text newAnchorPoint) {
        this.anchorPoint = newAnchorPoint;
        this.location = AnchorLocation.RIGHT;
        return this;
    }

    @Override
    public Anchor part(PlaceableFixedSizeDocumentPart documentPart) {
        this.part = documentPart;
        return this;
    }
}
