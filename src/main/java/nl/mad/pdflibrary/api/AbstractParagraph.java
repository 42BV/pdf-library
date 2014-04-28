package nl.mad.pdflibrary.api;

import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.model.Alignment;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;

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
    public Anchor addAnchor(PlaceableDocumentPart part) {
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
