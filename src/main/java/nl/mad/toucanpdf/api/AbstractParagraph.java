package nl.mad.toucanpdf.api;

import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.PlaceableFixedSizeDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;

/**
 * @author Dylan de Wolff
 *
 */
public abstract class AbstractParagraph extends AbstractPlaceableDocumentPart implements Paragraph {
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
        Anchor a = new Anchor(part);
        return addAnchor(a);
    }

    @Override
    public Anchor addAnchor(Anchor a) {
        this.anchors.add(a);
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
