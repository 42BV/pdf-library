package nl.mad.toucanpdf.api;

import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Anchor;
import nl.mad.toucanpdf.model.AnchorLocation;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.PlaceableFixedSizeDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;

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
        this.anchors = new LinkedList<Anchor>();
    }

    @Override
    public Paragraph on(int x, int y) {
        return on(new Position(x, y));
    }

    @Override
    public Paragraph on(Position position) {
        this.setPosition(position);
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
        List<Anchor> anchorsOnText = new LinkedList<Anchor>();
        if (t != null) {
            for (Anchor a : anchors) {
                if (t.equals(a.getAnchorPoint())) {
                    anchorsOnText.add(a);
                }
            }
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
        this.setAlignment(alignment);
        return this;
    }
}
