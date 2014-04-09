package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Observable;
import nl.mad.pdflibrary.model.ObserverEvent;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;

/**
 * Paragraph contains a collection of text objects. This ensures that the text objects are placed together in the document.
 * This also makes it unnecessary to specify positions for the text objects. The paragraph should be filled up before being
 * added to the document. Changes made to the paragraph after adding it to the document will not be processed.
 * @author Dylan de Wolff
 */
public class BaseParagraph extends AbstractPlaceableDocumentPart implements Paragraph {

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
        super(DocumentPartType.PARAGRAPH);
        textCollection = new ArrayList<Text>();
        this.setPosition(position);
    }

    /**
     * Creates a new instance of Paragraph and fills the textCollection with the given list
     * @param text List of text to add to the paragraph.
     */
    private BaseParagraph(List<Text> text) {
        this(new Position());
        this.textCollection = text;
    }

    public BaseParagraph(Paragraph p) {
        super(DocumentPartType.PARAGRAPH);
        this.textCollection = p.getTextCollection();
        this.setPosition(p.getPosition());
    }

    @Override
    public Paragraph addText(Text text) {
        this.textCollection.add(text);
        return this;
    }

    public List<Text> getTextCollection() {
        return this.textCollection;
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
    public void processContentSize(Page page) {
        for (int i = 0; i < textCollection.size(); ++i) {
            Text t = textCollection.get(i);
            System.out.println(t.getText());
            if (i == 0) {
                processParagraphPosition(textCollection.get(i), page);
            } else {
                t.setPosition(page.getOpenPosition(t.getLeading()));
            }
            t.processContentSize(page, true, this.getPosition().getX(), false);
        }
    }

    private void processParagraphPosition(Text text, Page page) {
        if (this.getPosition().hasCustomPosition()) {
            text.setPosition(this.getPosition());
        }
    }

    private void handleOverflow(Observable sender, ObserverEvent event, DocumentPart arg) {
        List<Text> newTextList = new ArrayList<Text>();
        newTextList.add((Text) arg);
        newTextList.addAll(textCollection.subList(textCollection.indexOf(sender), textCollection.size()));
    }

    @Override
    public int getContentHeight(Page page) {
        int height = 0;
        for (Text t : textCollection) {
            height += t.getContentHeight(page);
        }
        return height;
    }

    @Override
    public int getContentWidth(Page page, Position position) {
        int longestWidth = 0;
        for (Text t : textCollection) {
            int width = t.getContentWidth(page, position);
            longestWidth = Math.max(width, longestWidth);
        }
        return longestWidth;
    }

    @Override
    public int getLeading() {
        int leading = 0;
        for (Text text : textCollection) {
            int textLeading = text.getLeading();
            leading = Math.max(leading, textLeading);
        }
        return leading;
    }

    @Override
    public int[] getPositionAt(int height) {
        ArrayList<Integer> positionsTemp = new ArrayList<>();
        for (Text t : textCollection) {
            int[] xPositions = t.getPositionAt(height);
            for (int pos : xPositions) {
                if (pos != -1) {
                    positionsTemp.add(pos);
                }
            }
        }
        int[] positions = new int[positionsTemp.size()];
        for (int i = 0; i < positionsTemp.size(); ++i) {
            positions[i] = positionsTemp.get(i);
        }
        return positions;
    }

    @Override
    public List<int[]> getUsedSpaces(int height) {
        List<int[]> spaces = new LinkedList<int[]>();
        for (Text t : textCollection) {
            spaces.addAll(t.getUsedSpaces(height));
        }
        return spaces;
    }
}