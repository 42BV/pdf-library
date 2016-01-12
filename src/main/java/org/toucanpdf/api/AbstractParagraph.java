package org.toucanpdf.api;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.toucanpdf.model.Alignment;
import org.toucanpdf.model.Anchor;
import org.toucanpdf.model.AnchorLocation;
import org.toucanpdf.model.DocumentPartType;
import org.toucanpdf.model.Paragraph;
import org.toucanpdf.model.PlaceableFixedSizeDocumentPart;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dylan de Wolff
 *
 */
public abstract class AbstractParagraph extends AbstractPlaceableDocumentPart implements Paragraph {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParagraph.class);
    private List<Anchor> anchors;

    /**
     * 
     */
    public AbstractParagraph() {
        super(DocumentPartType.PARAGRAPH);
        this.anchors = new LinkedList<>();
    }

    @Override
    public Paragraph on(int x, int y) {
        return this.on(new Position(x, y));
    }

    @Override
    public Paragraph on(Position position) {
        super.on(position);
        return this;
    }

    @Override
    public Anchor addAnchor(PlaceableFixedSizeDocumentPart part) {
        Anchor a = new BaseAnchor(part);
        return addAnchor(a);
    }

    @Override
    public Anchor addAnchor(Anchor a) {
        if (this.getAnchorOn(a.getAnchorPoint(), a.getLocation()) == null) {
            this.anchors.add(a);
        } else {
            LOGGER.info("The given anchor could not be added on the given text and location. Only a single anchor is allowed per location.", a);
        }
        return a;
    }

    @Override
    public List<Anchor> getAnchorsOn(Text t) {
        List<Anchor> anchorsOnText = new LinkedList<>();
        if (t != null) {
            anchorsOnText.addAll(anchors.stream().filter(a -> t.equals(a.getAnchorPoint())).collect(Collectors.toList()));
        }
        return anchorsOnText;
    }

    /**
     * Returns the anchor that has been attached to the given text on the given location.
     * @param t Text to check.
     * @param location Location to check.
     * @return Anchor that has been attached on the given text object and location.
     */
    protected Anchor getAnchorOn(Text t, AnchorLocation location) {
        for (Anchor a : this.getAnchorsOn(t)) {
            if (location.equals(a.getLocation())) {
                return a;
            }
        }
        return null;
    }

    @Override
    public List<Anchor> getAnchors() {
        return this.anchors;
    }

    @Override
    public Paragraph align(Alignment alignment) {
        super.align(alignment);
        return this;
    }

    @Override
    public Paragraph marginTop(int marginTop) {
        super.marginTop(marginTop);
        return this;
    }

    @Override
    public Paragraph marginBottom(int marginBottom) {
        super.marginBottom(marginBottom);
        return this;
    }

    @Override
    public Paragraph marginRight(int marginRight) {
        super.marginRight(marginRight);
        return this;
    }

    @Override
    public Paragraph marginLeft(int marginLeft) {
        super.marginLeft(marginLeft);
        return this;
    }
}
