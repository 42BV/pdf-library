package nl.mad.toucanpdf.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.api.AbstractParagraph;
import nl.mad.toucanpdf.api.Anchor;
import nl.mad.toucanpdf.api.AnchorLocation;
import nl.mad.toucanpdf.api.BaseParagraph;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.PlaceableFixedSizeDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.StateImage;
import nl.mad.toucanpdf.model.StatePage;
import nl.mad.toucanpdf.model.StateParagraph;
import nl.mad.toucanpdf.model.StateText;
import nl.mad.toucanpdf.model.Text;

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
                    Anchor newAnchor = this.addAnchor(new Anchor(a, newText));
                    newAnchor.part(convertAnchorPart(newAnchor.getPart()));
                }
            }
        }
        this.setPosition(p.getPosition());
    }

    private PlaceableFixedSizeDocumentPart convertAnchorPart(PlaceableDocumentPart part) {
        switch (part.getType()) {
        case IMAGE:
            return new BaseStateImage((Image) part);
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
            Position position = page.getOpenPosition(text.getRequiredSpaceAbove(), requiredHeight, requiredWidth);
            if (position == null) {
                Paragraph overflow = handleAnchorOverflow(anchorList, requiredWidth, requiredHeight, page, text);
                if (overflow == null) {
                    this.processAnchors(text, page, fixedPosition);
                    return null;
                } else {
                    return overflow;
                }
            }
            position = this.processAnchorPositions(position, page, this.getAnchorsOn(text, AnchorLocation.ABOVE), AnchorLocation.ABOVE);
            position = this.processAnchorPositions(position, page, this.getAnchorsOn(text, AnchorLocation.LEFT), AnchorLocation.LEFT);
            Position startingPositionForText = new Position(position);
            startingPositionForText.setY(startingPositionForText.getY() - text.getRequiredSpaceAbove());
            position = getMinimalStartingPosition(position, page.getOpenSpacesOn(position, true, text.getRequiredSpaceAbove(), requiredHeight));
            position = getStartingPositionRightAnchors(this.getAnchorsOn(text, AnchorLocation.RIGHT), position,
                    page.getOpenSpacesOn(position, true, text.getRequiredSpaceAbove(), requiredHeight), page);
            text.on(startingPositionForText);
            position = this.processAnchorPositions(position, page, this.getAnchorsOn(text, AnchorLocation.RIGHT), AnchorLocation.RIGHT);
            this.processAnchorPositions(position, page, this.getAnchorsOn(text, AnchorLocation.BELOW), AnchorLocation.BELOW);
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
            if (AnchorLocation.LEFT.equals(a.getLocation())) {
                requiredWidthLeft += part.getWidth();
                requiredHeight = Math.max(part.getHeight(), requiredHeight);
            } else if (AnchorLocation.RIGHT.equals(a.getLocation())) {
                requiredWidthLeft += part.getWidth();
                requiredHeight = Math.max(part.getHeight(), requiredHeight);
            } else if (AnchorLocation.ABOVE.equals(a.getLocation()) || AnchorLocation.BELOW.equals(a.getLocation())) {
                requiredWidthAboveOrUnder = Math.max(part.getWidth(), requiredWidthAboveOrUnder);
                requiredHeight += part.getHeight();
            }
            double requiredWidthTotalOld = requiredWidthTotal;
            requiredWidthTotal = Math.max(requiredWidthLeft + requiredWidthRight + Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING, requiredWidthAboveOrUnder);
            //if the current anchor doesn't fit we'll remove it
            if (requiredWidthTotal > page.getWidthWithoutMargins() || requiredHeight > page.getHeightWithoutMargins()) {
                this.removeAnchor(a);
                //TODO: Log removal
                requiredWidthTotal = requiredWidthTotalOld;
            }
        }
        return new double[] { requiredWidthTotal, requiredHeight };
    }

    private void removeAnchor(Anchor a) {
        this.getAnchors().remove(a);
    }

    private Position getMinimalStartingPosition(Position pos, List<int[]> openSpaces) {
        //this method calculates the minimal starting point for images to the right of the text.
        Position position = new Position(pos);
        for (int[] openSpace : openSpaces) {
            if (openSpace[1] - openSpace[0] > Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING) {
                position.setX(openSpace[0] + Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING);
            }
        }
        return position;
    }

    private Position getStartingPositionRightAnchors(List<Anchor> anchors, Position pos, List<int[]> openSpaces, StatePage page) {
        /*this method determines the starting position of the anchors to the right of a text object.
        this is achieved by checking how many of the anchors will fit in the given open spaces.
        If there are any anchors that do not fit they will be removed from the list and the method will be repeated.
        The starting position has to be as accurate as possible as to not waste space and to get the correct placement on the text.*/
        Position position = new Position(pos);
        List<Anchor> processedAnchors = new LinkedList<Anchor>();
        int a = openSpaces.size() - 1;
        int b = anchors.size() - 1;
        //Go through the open spaces and anchors list from back to front
        while (a >= 0 && b >= 0) {
            int[] openSpace = openSpaces.get(a);
            int availableWidth = openSpace[1] - openSpace[0];
            boolean spaceFilled = false;
            //as long as there are anchors left and the open space hasn't been filled yet
            while (b >= 0 && !spaceFilled) {
                Anchor anchor = anchors.get(b);
                PlaceableFixedSizeDocumentPart part = anchor.getPart();
                int partWidth = part.getWidth();
                //if the current anchor fits in this open space we'll add it to the processed anchors.
                if (availableWidth > partWidth) {
                    availableWidth -= partWidth;
                    processedAnchors.add(anchor);
                } else {
                    spaceFilled = true;
                }
                --b;
            }
            //if we've processed all anchors (if there wasn't any overflow)
            if (processedAnchors.size() == anchors.size()) {
                position.setX(openSpace[0] + availableWidth);
            }
            --a;
        }
        //if there was overflow
        if (processedAnchors.size() != anchors.size()) {
            //remove the overflowing anchors and repeat the process
            List<Anchor> anchorsToProcess = new LinkedList<Anchor>(anchors.subList(0, (anchors.size() - processedAnchors.size()) - 1));
            position = getStartingPositionRightAnchors(anchorsToProcess, position, openSpaces, page);
        }
        return position;
    }

    /**
     * Processes the positioning of the given anchors.
     * @param position Position to place the anchors.
     * @param page StatePage to add the anchors to.
     * @param anchorList List of anchors to process.
     * @param location location of the anchors to process.
     * @return an instance of Position that has been adjusted for the anchor additions.
     */
    private Position processAnchorPositions(Position position, StatePage page, List<Anchor> anchorList, AnchorLocation location) {
        //This method takes care of the actual positioning of the anchors.
        Position newPos = new Position(position);
        PlaceableFixedSizeDocumentPart anchorPart = null;
        for (Anchor a : anchorList) {
            anchorPart = a.getPart();
            anchorPart.setPosition(newPos);
            switch (anchorPart.getType()) {
            case IMAGE:
                boolean wrapping = true;
                //Text should not wrap around images that are above or below a text.
                if (AnchorLocation.ABOVE.equals(location) || AnchorLocation.BELOW.equals(location)) {
                    wrapping = false;
                }
                ((StateImage) anchorPart).processContentSize(page, wrapping);
                break;
            default:
                break;
            }
            page.add(anchorPart);
            newPos = new Position(anchorPart.getWidth() + anchorPart.getPosition().getX(), newPos.getY());
        }
        double newPosX = newPos.getX();
        double newPosY = newPos.getY();

        //To ensure that text will not wrap around the images we have to increase the filledHeight value of the page.
        if ((AnchorLocation.ABOVE.equals(location) || AnchorLocation.BELOW.equals(location)) && anchorPart != null) {
            newPosY = newPos.getY() - anchorPart.getHeight() - Page.DEFAULT_NEW_LINE_SIZE;
            newPosX = page.getMarginLeft();
        }
        return new Position(newPosX, newPosY);
    }

    private Paragraph handleAnchorOverflow(List<Anchor> anchorList, double requiredWidth, double requiredHeight, StatePage page, StateText text) {
        Paragraph overflow = null;
        //if the anchors and text can fit on the page, it simply means there is not enough space on this page and we should move on to the next one.
        if (requiredWidth < page.getWidthWithoutMargins() && requiredHeight < page.getHeightWithoutMargins()) {
            overflow = this.handleOverflow(textCollection.indexOf(text), text);
        }
        return overflow;
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
                Position position = new Position(posX, newPositionY);
                text.on(position);
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
