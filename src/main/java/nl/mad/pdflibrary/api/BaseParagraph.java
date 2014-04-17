package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPartType;
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
        textCollection = new LinkedList<Text>();
        this.setPosition(position);
    }

    /**
     * Creates a new instance of paragraph based on the given paragraph.
     * @param p paragraph to copy.
     * @param copyCollection Whether or not this paragraph should copy the contents of the given paragraph.
     */
    public BaseParagraph(Paragraph p, boolean copyCollection) {
        super(DocumentPartType.PARAGRAPH);
        this.textCollection = new LinkedList<Text>();
        if (copyCollection) {
            for (Text t : p.getTextCollection()) {
                textCollection.add(new BaseText(t));
            }
        }
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
    public Paragraph processContentSize(Page page, boolean fixedPosition) {
        Paragraph overflowParagraph = null;
        for (int i = 0; i < textCollection.size(); ++i) {
            Text t = textCollection.get(i);
            System.out.println("Handling ze: " + t.getText());
            System.out.println(t.getPosition().getY());
            double posX = processTextPosition(textCollection.get(i), page, fixedPosition);
            Text overflow = t.processContentSize(page, posX, fixedPosition);
            if (overflow != null) {
                overflowParagraph = this.handleOverflow(i + 1, overflow);
            }
        }
        return overflowParagraph;
    }

    private double processTextPosition(Text text, Page page, boolean fixedPosition) {
        int index = textCollection.indexOf(text);
        double posX = this.getPosition().getX();
        if (!fixedPosition) {
            posX -= page.getMarginLeft();
        }
        if (index == 0) {
            if (this.getPosition().hasCustomPosition()) {
                text.setPosition(this.getPosition());
            }
        } else {
            Text previous = textCollection.get(index - 1);
            System.out.println("Previous: " + previous.getPosition().getY());
            System.out.println("Previous contentheight: " + previous.getContentHeight(page));
            System.out.println("New pos: "
                    + (previous.getPosition().getY() - previous.getContentHeightUnderBaseLine(page) - page.getLeading() - text.getRequiredSpaceAbove()));
            double newPositionY = previous.getPosition().getY() - previous.getContentHeightUnderBaseLine(page) - page.getLeading()
                    - text.getRequiredSpaceAbove();
            if (fixedPosition) {
                Position pos = new Position(posX, newPositionY);
                text.setPosition(pos);
            } else {
                text.setPosition(page.getOpenPosition(posX, newPositionY, text.getRequiredSpaceAbove(), text.getRequiredSpaceBelow()));
            }
        }
        return posX;
    }

    private Paragraph handleOverflow(int index, Text text) {
        List<Text> newTextList = new ArrayList<Text>();
        newTextList.add(text);
        newTextList.addAll(textCollection.subList(index, textCollection.size()));
        this.textCollection.removeAll(newTextList);
        Paragraph overflowParagraph = new BaseParagraph(this, false);
        overflowParagraph.addText(newTextList);
        return overflowParagraph;
    }

    @Override
    public double getContentHeight(Page page) {
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
    public int[] getPositionAt(double height) {
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
    public List<int[]> getUsedSpaces(double height) {
        List<int[]> spaces = new LinkedList<int[]>();
        for (Text t : textCollection) {
            spaces.addAll(t.getUsedSpaces(height));
        }
        return spaces;
    }

    @Override
    public double getRequiredSpaceAbove() {
        if (!textCollection.isEmpty()) {
            return textCollection.get(0).getRequiredSpaceAbove();
        }
        return 0;
    }

    @Override
    public double getRequiredSpaceBelow() {
        if (!textCollection.isEmpty()) {
            return textCollection.get(textCollection.size() - 1).getRequiredSpaceBelow();
        }
        return 0;
    }

    @Override
    public Paragraph addText(List<Text> textCollection) {
        this.textCollection.addAll(textCollection);
        return this;
    }
}