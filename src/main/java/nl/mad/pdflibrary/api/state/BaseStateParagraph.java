package nl.mad.pdflibrary.api.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.api.AbstractParagraph;
import nl.mad.pdflibrary.api.Anchor;
import nl.mad.pdflibrary.api.AnchorLocation;
import nl.mad.pdflibrary.api.BaseParagraph;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.StatePage;
import nl.mad.pdflibrary.model.StateParagraph;
import nl.mad.pdflibrary.model.StatePlaceableDocumentPart;
import nl.mad.pdflibrary.model.StateText;
import nl.mad.pdflibrary.model.Text;

/**
 * Base implementation of the StateParagraph class. This class offers the same functionality as the BaseParagraph class. 
 * The BaseStateParagraph class also offers the functionality to calculate the position of the both the paragraph and it's content.
 * 
 * @see AbstractParagraph
 * @see StateParagraph
 * @author Dylan de Wolff
 *
 */
public class BaseStateParagraph extends AbstractParagraph implements StateParagraph {
    private List<StateText> textCollection;

    /**
     * Creates a copy of the given paragraph object. 
     * @param p The paragraph to copy.
     * @param copyCollection Whether or not you wish to copy the text collection and anchors. Use true if you want to do so, false otherwise.
     */
    public BaseStateParagraph(Paragraph p, boolean copyCollection) {
        super();
        this.textCollection = new LinkedList<StateText>();
        this.align(p.getAlignment());
        if (copyCollection) {
            for (Text t : p.getTextCollection()) {
                StateText newText = new BaseStateText(t);
                textCollection.add(newText);
                for (Anchor a : p.getAnchorsOn(t)) {
                    this.addAnchor(new Anchor(a, newText));
                }
            }
        }
        this.setPosition(p.getPosition());
    }

    /**
     * Creates a new instance of BaseStateParagraph.
     */
    public BaseStateParagraph() {
        this.textCollection = new LinkedList<StateText>();
    }

    @Override
    public Paragraph processContentSize(StatePage page, boolean fixedPosition) {
        Paragraph overflowParagraph = null;
        for (int i = 0; i < textCollection.size(); ++i) {
            StateText t = (StateText) textCollection.get(i);
            t.align(this.getAlignment());
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

    /**
     * Processes the positioning of the given anchors.
     * @param position Position to place the anchors.
     * @param page Page to add the anchors to.
     * @param anchorList List of anchors to process.
     * @return an instance of Position that has been adjusted for the anchor additions.
     */
    private Position processAnchorPositions(Position position, Page page, List<Anchor> anchorList) {
        Position newPos = new Position(position);
        for (Anchor a : anchorList) {
            if (a.getPart() instanceof StatePlaceableDocumentPart) {
                StatePlaceableDocumentPart anchorPart = (StatePlaceableDocumentPart) a.getPart();
                anchorPart.setPosition(newPos);
                page.add(anchorPart);
                newPos = new Position(anchorPart.getContentWidth(page, newPos) + newPos.getX(), newPos.getY());
            }
        }
        return newPos;
    }

    /**
     * Retrieves the anchors corresponding to the given text and processes their positioning.
     * @param text Text that is being processed.
     * @param page Page the text and anchors will be on.
     * @param fixedPosition Whether the text has a fixed position.
     */
    private void anchorProcessing(StateText text, StatePage page, boolean fixedPosition) {
        List<Anchor> anchorList = this.getAnchorsOn(text);
        if (anchorList.size() > 0) {
            int index = textCollection.indexOf(text);
            StateText previous = null;
            if (index != 0) {
                previous = textCollection.get(index - 1);
            }
            double posX = this.getPosition().getX();
            if (!fixedPosition) {
                posX -= page.getMarginLeft();
            }

            double requiredWidth = Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING;
            double requiredHeight = text.getRequiredSpaceBelow();
            for (Anchor a : anchorList) {
                if (a.getPart() instanceof StatePlaceableDocumentPart) {
                    StatePlaceableDocumentPart statePlaceableDocumentPart = (StatePlaceableDocumentPart) a.getPart();
                    if (AnchorLocation.LEFT.equals(a.getLocation()) || AnchorLocation.RIGHT.equals(a.getLocation())) {
                        requiredWidth += statePlaceableDocumentPart.getContentWidth(page, new Position());
                        requiredHeight = Math.max(statePlaceableDocumentPart.getContentHeight(page), requiredHeight);
                    }
                }
            }
            System.out.println("Getting open pos for the anchor!");
            Position position = page.getOpenPosition(text.getRequiredSpaceAbove(), requiredHeight, requiredWidth);
            //TODO: overflow handling?
            position = this.processAnchorPositions(position, page, this.getAnchorsOn(text, AnchorLocation.LEFT));
            position.setX(position.getX() + Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING);
            this.processAnchorPositions(position, page, this.getAnchorsOn(text, AnchorLocation.RIGHT));
        }
    }

    /**
     * Returns anchors that have been attached to the given text on the given location.
     * @param t Text to check.
     * @param location Location to check.
     * @return List of anchors that have been attached on the given text object and location.
     */
    private List<Anchor> getAnchorsOn(Text t, AnchorLocation location) {
        List<Anchor> anchorsOnText = new LinkedList<Anchor>();
        for (Anchor a : this.getAnchorsOn(t)) {
            if (location.equals(a.getLocation())) {
                anchorsOnText.add(a);
            }
        }
        return anchorsOnText;
    }

    /**
     * Processes the positioning of the text.
     * @param text Text to position.
     * @param page Page to add the text to.
     * @param fixedPosition Whether the text has a fixed position.
     * @return
     */
    private double processTextPosition(StateText text, StatePage page, boolean fixedPosition) {
        int index = textCollection.indexOf(text);
        double posX = this.getPosition().getX();
        if (!fixedPosition) {
            posX -= page.getMarginLeft();
        }

        if (index == 0 && this.getPosition().hasCustomPosition()) {
            text.on(this.getPosition());
        } else {
            StateText previous = textCollection.get(index - 1);
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

    /**
     * Processes overflow based on the given index and text.
     * @param index Index of the text object causing overflow.
     * @param text Text object that contains the overflow.
     * @return
     */
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
        for (StateText t : textCollection) {
            height += t.getContentHeight(page);
        }
        return height;
    }

    @Override
    public int getContentWidth(Page page, Position position) {
        int longestWidth = 0;
        for (StateText t : textCollection) {
            int width = t.getContentWidth(page, position);
            longestWidth = Math.max(width, longestWidth);
        }
        return longestWidth;
    }

    @Override
    public int[] getPositionAt(double height) {
        ArrayList<Integer> positionsTemp = new ArrayList<Integer>();
        for (StateText t : textCollection) {
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
        for (StateText t : textCollection) {
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
    public PlaceableDocumentPart copy() {
        return new BaseStateParagraph(this, false);
    }

    @Override
    public Paragraph addText(Text text) {
        this.textCollection.add(new BaseStateText(text));
        return this;
    }

    @Override
    public List<Text> getTextCollection() {
        List<Text> text = new LinkedList<Text>();
        text.addAll(textCollection);
        return text;
    }

    @Override
    public Paragraph addText(List<Text> textCollection) {
        for (Text t : textCollection) {
            this.addText(t);
        }
        return this;
    }

    @Override
    public List<StateText> getStateTextCollection() {
        return this.textCollection;
    }
}
