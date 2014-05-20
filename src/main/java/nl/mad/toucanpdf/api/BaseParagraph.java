package nl.mad.toucanpdf.api;

import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.model.Anchor;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;

/**
 * Paragraph contains a collection of text objects. This ensures that the text objects are placed together in the document.
 * This also makes it unnecessary to specify positions for the text objects. The paragraph should be filled up before being
 * added to the document. Changes made to the paragraph after adding it to the document will not be processed.
 * @author Dylan de Wolff
 */
public class BaseParagraph extends AbstractParagraph implements Paragraph {

    private List<Text> textCollection;

    /**
     * Creates a new instance of Paragraph with automatic positioning.
     */
    public BaseParagraph() {
        this(new Position());
    }

    /**
     * Creates a new instance of Paragraph.
     * @param position Position of the paragraph.
     */
    public BaseParagraph(Position position) {
        super();
        textCollection = new LinkedList<Text>();
        this.setPosition(position);
    }

    /**
     * Creates a new instance of paragraph based on the given paragraph.
     * @param p paragraph to copy.
     * @param copyCollection Whether or not this paragraph should copy the contents of the given paragraph.
     */
    public BaseParagraph(Paragraph p, boolean copyCollection) {
        super();
        this.textCollection = new LinkedList<Text>();
        this.align(p.getAlignment());
        if (copyCollection) {
            for (Text t : p.getTextCollection()) {
                Text newText = new BaseText(t);
                textCollection.add(newText);
                for (Anchor a : p.getAnchorsOn(t)) {
                    this.addAnchor(new BaseAnchor(a, newText));
                }
            }
        }
        this.setPosition(p.getPosition());
    }

    @Override
    public Paragraph addText(Text text) {
        this.textCollection.add(text);
        return this;
    }

    @Override
    public List<Text> getTextCollection() {
        return this.textCollection;
    }

    @Override
    public Paragraph addText(List<Text> text) {
        this.textCollection.addAll(text);
        return this;
    }

    @Override
    public PlaceableDocumentPart copy() {
        return new BaseParagraph(this, false);
    }
}