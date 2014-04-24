package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.model.Alignment;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
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
    private List<Anchor> anchors;

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
        anchors = new LinkedList<Anchor>();
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
        this.anchors = new LinkedList<Anchor>();
        this.align(p.getAlignment());
        if (copyCollection) {
            for (Text t : p.getTextCollection()) {
                Text newText = new BaseText(t);
                textCollection.add(newText);
                for (Anchor a : p.getAnchorsOn(t)) {
                    anchors.add(new Anchor(a, newText));
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
        System.out.println("Text col: " + textCollection.size());
        for (int i = 0; i < textCollection.size(); ++i) {
            Text t = textCollection.get(i);
            t.align(this.getAlignment());
            System.out.println("The text alignment is: " + t.getAlignment());
            System.out.println("Handling ze: " + t.getText());
            System.out.println(t.getPosition().getY());
            //            double requiredHeight = 0;
            //            double requiredWidth = 0;
            //            for (Anchor a : this.getAnchorsOn(t)) {
            //                requiredHeight = Math.max(requiredHeight, a.getPart().getContentHeight(page));
            //                requiredWidth += a.getP
            //            }
            //processAnchorPosition(t, page, fixedPosition, AnchorLocation.ABOVE);
            anchorProcessing(textCollection.get(i), page, fixedPosition);
            double posX = processTextPosition(textCollection.get(i), page, fixedPosition);
            //processAnchorPosition(t, page, fixedPosition, AnchorLocation.BELOW);
            Text overflow = t.processContentSize(page, posX, fixedPosition);
            if (overflow != null) {
                overflowParagraph = this.handleOverflow(i + 1, overflow);
            }
        }
        return overflowParagraph;
    }

    private Position processAnchorPositions(Position position, Text text, Page page, boolean fixedPosition, List<Anchor> anchorList) {
        Position newPos = new Position(position);
        for (Anchor a : anchorList) {
            PlaceableDocumentPart anchorPart = a.getPart();
            anchorPart.setPosition(newPos);
            page.add(anchorPart);
            newPos = new Position(anchorPart.getContentWidth(page, newPos) + newPos.getX(), newPos.getY());
        }
        return newPos;
    }

    private void anchorProcessing(Text text, Page page, boolean fixedPosition) {
        List<Anchor> anchorList = this.getAnchorsOn(text);
        System.out.println("Text = " + text.getText() + ", anchors = " + anchorList.size());
        if (anchorList.size() > 0) {
            int index = textCollection.indexOf(text);
            Text previous = null;
            if (index != 0) {
                previous = textCollection.get(index - 1);
            }
            double posX = this.getPosition().getX();
            if (!fixedPosition) {
                posX -= page.getMarginLeft();
            }
            //double posY = 0;//page.getHeight() - page.getFilledHeight() - page.getLeading();
            if (previous != null) {
                //posY = page.getFilledHeight() ;
            }
            double requiredWidth = Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING;
            double requiredHeight = text.getRequiredSpaceBelow();
            for (Anchor a : anchorList) {
                if (AnchorLocation.LEFT.equals(a.getLocation()) || AnchorLocation.RIGHT.equals(a.getLocation())) {
                    requiredWidth += a.getPart().getContentWidth(page, new Position());
                    requiredHeight = Math.max(a.getPart().getContentHeight(page), requiredHeight);
                }
            }
            System.out.println("Getting open pos for the anchor!");
            Position position = page.getOpenPosition(text.getRequiredSpaceAbove(), requiredHeight, requiredWidth);
            //TODO: overflow handling?
            position = this.processAnchorPositions(position, text, page, fixedPosition, this.getAnchorsOn(text, AnchorLocation.LEFT));
            position.setX(position.getX() + Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING);
            this.processAnchorPositions(position, text, page, fixedPosition, this.getAnchorsOn(text, AnchorLocation.RIGHT));
        }
    }

    private double processTextPosition(Text text, Page page, boolean fixedPosition) {
        int index = textCollection.indexOf(text);
        double posX = this.getPosition().getX();
        if (!fixedPosition) {
            posX -= page.getMarginLeft();
        }

        if (index == 0 && this.getPosition().hasCustomPosition()) {
            text.on(this.getPosition());
        } else {
            Text previous = textCollection.get(index - 1);
            double newPositionY = previous.getPosition().getY() - previous.getContentHeightUnderBaseLine(page) - page.getLeading()
                    - text.getRequiredSpaceAbove();
            if (fixedPosition) {
                Position pos = new Position(posX, newPositionY);
                text.on(pos);
            } else {
                text.on(page.getOpenPosition(posX, newPositionY, text.getRequiredSpaceAbove(), text.getRequiredSpaceBelow(), 0));
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

    @Override
    public Anchor addAnchor(PlaceableDocumentPart part) {
        Anchor a = new Anchor(part);
        this.anchors.add(a);
        return a;
    }

    @Override
    public List<Anchor> getAnchorsOn(Text t) {
        List<Anchor> anchorsOnText = new LinkedList<Anchor>();
        if (t != null) {
            System.out.println("Comparing anchor points: ");
            System.out.println("    given: " + t);
            for (Anchor a : anchors) {
                System.out.println("   " + a.getAnchorPoint());
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

    private List<Anchor> getAnchorsOn(Text t, AnchorLocation location) {
        List<Anchor> anchorsOnText = new LinkedList<Anchor>();
        for (Anchor a : this.getAnchorsOn(t)) {
            if (location.equals(a.getLocation())) {
                anchorsOnText.add(a);
            }
        }
        return anchorsOnText;
    }

    @Override
    public Paragraph align(Alignment alignment) {
        this.setAlignment(alignment);
        return this;
    }

    @Override
    public PlaceableDocumentPart copy() {
        return new BaseParagraph(this, false);
    }
}