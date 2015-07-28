package nl.mad.toucanpdf.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import nl.mad.toucanpdf.api.AbstractParagraph;
import nl.mad.toucanpdf.api.BaseAnchor;
import nl.mad.toucanpdf.model.Anchor;
import nl.mad.toucanpdf.model.AnchorLocation;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.PlaceableFixedSizeDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Space;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StateParagraph;
import nl.mad.toucanpdf.model.state.StatePlaceableDocumentPart;
import nl.mad.toucanpdf.model.state.StatePlaceableFixedSizeDocumentPart;
import nl.mad.toucanpdf.model.state.StateText;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseStateParagraph.class);
    private List<StateText> textCollection;
    private DocumentPart originalObject;

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
                    Anchor newAnchor = this.addAnchor(new BaseAnchor(a, newText));
                    newAnchor.part(convertAnchorPart(newAnchor.getPart()));
                }
            }
        }
        this.marginBottom = p.getMarginBottom();
        this.marginLeft = p.getMarginLeft();
        this.marginRight = p.getMarginRight();
        this.marginTop = p.getMarginTop();
        this.setPosition(p.getPosition());
    }

    private PlaceableFixedSizeDocumentPart convertAnchorPart(PlaceableDocumentPart part) {
        switch (part.getType()) {
        case IMAGE:
            return new BaseStateImage((Image) part);
        case TABLE:
            return new BaseStateTable((Table) part);
        default:
            return null;
        }
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
            t.marginLeft(t.getMarginLeft() + this.marginLeft);
            t.marginRight(t.getMarginRight() + this.marginRight);
            if (i == 0) {
                t.marginTop(t.getMarginTop() + this.marginTop);
            }
            if (i == (textCollection.size() - 1)) {
                t.marginBottom(t.getMarginBottom() + this.marginBottom);
            }
            t.align(this.getAlignment());
            double posX = 0;
            if (this.getAnchorsOn(t).size() > 0) {
                overflowParagraph = processAnchors(textCollection.get(i), page, fixedPosition);
            } else {
                posX = processTextPosition(textCollection.get(i), page, fixedPosition);
            }

            Text overflow = t.processContentSize(page, posX, fixedPosition);
            if (overflow != null) {
                overflowParagraph = this.handleOverflow(i + 1, overflow);
            } else {
                Position pos = new Position(t.getPosition());
                pos.adjustY(-t.getContentHeight(page));
                pos.setX(page.getMarginLeft());
                this.processAnchorPositions(new Position(pos), page, this.getAnchorOn(t, AnchorLocation.BELOW), AnchorLocation.BELOW);

            }
        }
        return overflowParagraph;
    }

    /**
     * Retrieves the anchors corresponding to the given text and processes their positioning.
     * @param text Text that is being processed.
     * @param page Page the text and anchors will be on.
     * @param fixedPosition Whether the text has a fixed position.
     * @return Paragraph containing overflow. Null if there is no overflow.
     */
    private Paragraph processAnchors(StateText text, StatePage page, boolean fixedPosition) {
        List<Anchor> anchorList = this.getAnchorsOn(text);
        if (anchorList.size() > 0) {
            double[] requiredSize = removeOverflowAnchors(text, anchorList, page);
            double requiredWidth = requiredSize[0];
            double requiredHeight = requiredSize[1];
            double requiredSpaceAbove = text.getRequiredSpaceAbove();
            if (this.textCollection.indexOf(text) == 0) {
                requiredSpaceAbove = 0;
            }
            Position position = page.getOpenPosition(requiredSpaceAbove, requiredHeight, text, requiredWidth);
            if (position == null) {
                Paragraph overflow = handleAnchorOverflow(requiredWidth, requiredHeight, page, text);
                if (overflow == null) {
                    return this.processAnchors(text, page, fixedPosition);
                } else {
                    return overflow;
                }
            }
            position = this.processAnchorPositions(position, page, this.getAnchorOn(text, AnchorLocation.ABOVE), AnchorLocation.ABOVE);
            position.adjustY(-text.getMarginTop());
            position = this.processAnchorPositions(position, page, this.getAnchorOn(text, AnchorLocation.LEFT), AnchorLocation.LEFT);
            Position startingPositionForText = new Position(position);
            startingPositionForText.adjustY(-text.getRequiredSpaceAboveLine());
            position = getMinimalStartingPositionForRightAnchor(position,
                    page.getOpenSpacesOn(position, true, text.getRequiredSpaceAbove(), requiredHeight, text));
            position = getStartingPositionRightAnchor(this.getAnchorOn(text, AnchorLocation.RIGHT), position,
                    page.getOpenSpacesOn(position, true, text.getRequiredSpaceAbove(), requiredHeight, text), page);
            text.on(startingPositionForText);
            this.processAnchorPositions(position, page, this.getAnchorOn(text, AnchorLocation.RIGHT), AnchorLocation.RIGHT);
        }
        return null;
    }

    /**
     * Removes anchors that cause overflow from the given list.
     * @param text Text the anchors are on.
     * @param anchorList List of anchors.
     * @param page Page the anchors will be on.
     * @return double array containing two values, respectively the width required to fit the anchors and the height required to fit the anchors
     */
    private double[] removeOverflowAnchors(StateText text, List<Anchor> anchorList, Page page) {
        double requiredWidthLeft = 0;
        double requiredWidthRight = 0;
        double requiredWidthAboveOrUnder = 0;
        double requiredWidthTotal = 0;
        double requiredHeight = text.getRequiredSpaceBelow();

        //process requiredWidth and requiredHeight
        for (Anchor a : anchorList) {
            PlaceableFixedSizeDocumentPart part = a.getPart();
            double totalPartWidth = part.getWidth() + part.getMarginLeft() + part.getMarginRight();
            double totalPartHeight = part.getHeight() + part.getMarginTop() + part.getMarginBottom();
            if (AnchorLocation.LEFT.equals(a.getLocation())) {
                requiredWidthLeft += totalPartWidth;
                requiredHeight = Math.max(totalPartHeight, requiredHeight);
            } else if (AnchorLocation.RIGHT.equals(a.getLocation())) {
                requiredWidthRight += totalPartWidth;
                requiredHeight = Math.max(totalPartHeight, requiredHeight);
            } else if (AnchorLocation.ABOVE.equals(a.getLocation()) || AnchorLocation.BELOW.equals(a.getLocation())) {
                requiredWidthAboveOrUnder = Math.max(totalPartWidth, requiredWidthAboveOrUnder);
                requiredHeight += totalPartHeight;
            }
            double requiredWidthTotalOld = requiredWidthTotal;
            requiredWidthTotal = Math.max(requiredWidthLeft + requiredWidthRight + Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING, requiredWidthAboveOrUnder);
            //if the current anchor doesn't fit we'll remove it
            if (requiredWidthTotal > page.getWidthWithoutMargins() || requiredHeight > page.getHeightWithoutMargins()) {
                this.removeAnchor(a);
                LOGGER.warn("The given anchor did not fit on the page. The anchor has been removed.", a);
                requiredWidthTotal = requiredWidthTotalOld;
            }
        }
        return new double[] { requiredWidthTotal, requiredHeight };
    }

    private void removeAnchor(Anchor a) {
        this.getAnchors().remove(a);
    }

    private Position getMinimalStartingPositionForRightAnchor(Position pos, List<Space> openSpaces) {
        //this method calculates the minimal starting point for the anchor to the right of the text.
        Position position = new Position(pos);
        for (Space openSpace : openSpaces) {
            if (openSpace.getEndPoint() - openSpace.getStartPoint() > Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING) {
                position.setX(openSpace.getStartPoint() + Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING);
                return position;
            }
        }
        return position;
    }

    private Position getStartingPositionRightAnchor(Anchor anchor, Position pos, List<Space> openSpaces, StatePage page) {
        Position position = new Position(pos);
        if (anchor != null) {
            /*this method determines the starting position of the anchor to the right of a text object.
            this is achieved by checking how much space the right anchor requires.
            The starting position has to be as accurate as possible as to not waste space and to get the correct placement on the text.*/

            int a = openSpaces.size() - 1;
            boolean anchorPlaced = false;
            //Go through the open spaces and anchors list from back to front
            while (a >= 0 && !anchorPlaced) {
                Space openSpace = openSpaces.get(a);
                int availableWidth = openSpace.getEndPoint() - openSpace.getStartPoint();
                PlaceableFixedSizeDocumentPart part = anchor.getPart();
                double partWidth = part.getWidth() + part.getMarginLeft() + part.getMarginRight();
                //if the current anchor fits in this open space we'll add it to the processed anchors.
                if (FloatEqualityTester.greaterThan(availableWidth, partWidth)) {
                    position.setX(openSpace.getEndPoint() - partWidth);
                    anchorPlaced = true;
                }
                --a;
            }
        } else {
            position = new Position(pos.getY(), page.getWidth());
        }
        return position;
    }

    /**
     * Processes the positioning of the given anchors.
     * @param position Position to place the anchors.
     * @param page StatePage to add the anchors to.
     * @param anchor anchor to process.
     * @param location location of the anchors to process.
     * @return an instance of Position that has been adjusted for the anchor additions.
     */
    private Position processAnchorPositions(Position position, StatePage page, Anchor anchor, AnchorLocation location) {
        if (anchor != null) {
            //This method takes care of the actual positioning of the anchors.
            Position newPos = new Position(position);
            PlaceableFixedSizeDocumentPart anchorPart = anchor.getPart();
            newPos.adjustX(anchorPart.getMarginLeft());
            newPos.adjustY(-anchorPart.getMarginTop());
            anchorPart.setPosition(newPos);
            boolean wrapping = true;
            boolean alignment = false;
            //Text should not wrap around images that are above or below a text.
            if (AnchorLocation.ABOVE.equals(location) || AnchorLocation.BELOW.equals(location)) {
                wrapping = false;
                alignment = true;
            }
            StatePlaceableFixedSizeDocumentPart stateAnchorPart = (StatePlaceableFixedSizeDocumentPart) anchorPart;
            stateAnchorPart.setWrappingAllowed(wrapping);
            stateAnchorPart.processContentSize(page, wrapping, alignment, false);
            page.add(anchorPart);
            newPos = new Position(anchorPart.getWidth() + anchorPart.getMarginRight() + anchorPart.getPosition().getX(), newPos.getY());
            double newPosX = newPos.getX();
            double newPosY = newPos.getY();

            //To ensure that text will not wrap around the images we have to increase the position values.
            if ((AnchorLocation.ABOVE.equals(location) || AnchorLocation.BELOW.equals(location))) {
                newPosY = newPos.getY() - anchorPart.getHeight() - anchorPart.getMarginBottom() - (Page.DEFAULT_NEW_LINE_SIZE * 3);
                newPosX = page.getMarginLeft();
            }
            return new Position(newPosX, newPosY);
        }
        return position;
    }

    private Paragraph handleAnchorOverflow(double requiredWidth, double requiredHeight, StatePage page, StateText text) {
        Paragraph overflow = null;
        //if the anchors and text can fit on the page, it simply means there is not enough space on this page and we should move on to the next one.
        if (requiredWidth < page.getWidthWithoutMargins() && requiredHeight < page.getHeightWithoutMargins()) {
            overflow = this.handleOverflow(textCollection.indexOf(text), text);
        }
        return overflow;
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

        if (index == 0) {
            text.on(page.getOpenPosition(text.getRequiredSpaceAbove(), text.getRequiredSpaceBelow(), text, 0));
        } else {
            StateText previous = textCollection.get(index - 1);
            if (fixedPosition) {
                double newPositionY = previous.getPosition().getY() - previous.getContentHeightUnderBaseLine(page) - previous.getRequiredSpaceBelow()
                        - page.getLeading() - text.getRequiredSpaceAbove();
                Position position = new Position(posX, newPositionY);
                text.on(position);
            } else {
                Anchor a = this.getAnchorOn(previous, AnchorLocation.BELOW);
                if (a != null) {
                    page.setFilledHeight(page.getFilledHeight() + text.getRequiredSpaceAbove());
                }
                text.on(page.getOpenPosition(text.getRequiredSpaceAbove(), text.getRequiredSpaceBelow(), text, 0));
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
        BaseStateParagraph overflowParagraph = new BaseStateParagraph(this, false);
        overflowParagraph.addText(newTextList);
        //TODO: add anchors on overflow! Including beneath anchor from the object causing the overflow
        for (Text t : overflowParagraph.getTextCollection()) {
            for (Anchor a : this.getAnchorsOn(t)) {
                overflowParagraph.addAnchor(a);
            }
        }
        overflowParagraph.setOriginalObject(this.getOriginalObject());
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
    public double getContentWidth(Page page, Position position) {
        double longestWidth = 0;
        for (StateText t : textCollection) {
            double width = t.getContentWidth(page, position);
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
    public List<Space> getUsedSpaces(double height, int pageWidth) {
        List<Space> spaces = new LinkedList<>();
        for (StateText t : textCollection) {
            spaces.addAll(t.getUsedSpaces(height, pageWidth));
        }
        return spaces;
    }

    @Override
    public double getRequiredSpaceAbove() {
        if (!textCollection.isEmpty()) {
            Text t = textCollection.get(0);
            Anchor a = this.getAnchorOn(t, AnchorLocation.ABOVE);
            if (a != null) {
                PlaceableFixedSizeDocumentPart part = a.getPart();
                if (part instanceof StatePlaceableDocumentPart) {
                    return ((StatePlaceableDocumentPart) part).getRequiredSpaceAbove();
                }
            } else {
                return textCollection.get(0).getRequiredSpaceAbove();
            }
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
        List<Text> text = textCollection.stream().collect(Collectors.toCollection(LinkedList::new));
        return text;
    }

    @Override
    public Paragraph addText(List<Text> text) {
        for (Text t : text) {
            this.addText(t);
        }
        return this;
    }

    @Override
    public List<StateText> getStateTextCollection() {
        return this.textCollection;
    }

    @Override
    public void setOriginalObject(DocumentPart originalObject) {
        if (this.originalObject == null) {
            this.originalObject = originalObject;
        }
    }

    @Override
    public DocumentPart getOriginalObject() {
        return this.originalObject;
    }

    @Override
    public double getRequiredSpaceLeft() {
        return this.marginLeft;
    }

    @Override
    public double getRequiredSpaceRight() {
        return this.marginRight;
    }
}
