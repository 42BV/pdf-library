package nl.mad.toucanpdf.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.api.BasePage;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StatePlaceableDocumentPart;
import nl.mad.toucanpdf.model.state.StateSpacing;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

/**
 * Base implementation for the StatePage interface. This class offers the same functionality as BasePage but also offers
 * functionality that allows for the positioning of objects on the page. 
 * 
 * @see StatePage
 * @see BasePage 
 * @author Dylan de Wolff
 */
public class BaseStatePage extends BasePage implements StatePage {
    private double filledWidth = 0;
    private double filledHeight = 0;
    private DocumentPart originalObject;

    /**
     * Creates a new instance of BaseStatePage with the given width and height.
     * @param width Width of the page.
     * @param height Height of the page.
     */
    public BaseStatePage(int width, int height) {
        super(width, height);
    }

    /**
     * Creates a copy of the given page object.
     * @param page Page to copy.
     */
    public BaseStatePage(Page page) {
        super(page);
        marginTop(page.getMarginTop());
    }

    @Override
    public Page marginTop(int marginTop) {
        if (filledHeight == this.getMarginTop() || filledHeight == 0) {
            filledHeight = marginTop;
        }
        super.marginTop(marginTop);
        return this;
    }

    @Override
    public double getFilledWidth() {
        return this.filledWidth;
    }

    @Override
    public double getFilledHeight() {
        return this.filledHeight;
    }

    @Override
    public void setFilledWidth(double filledWidth) {
        this.filledWidth = filledWidth;
    }

    @Override
    public void setFilledHeight(double filledHeight) {
        this.filledHeight = filledHeight;
    }

    @Override
    public double getRemainingHeight() {
        return this.getHeightWithoutMargins() - filledHeight;
    }

    @Override
    public double getRemainingWidth() {
        return this.getWidthWithoutMargins() - filledWidth;
    }

    @Override
    public Position getOpenPosition(double requiredSpaceAbove, double requiredSpaceBelow, StateSpacing spacing) {
    	        return this.getOpenPosition(requiredSpaceAbove, requiredSpaceBelow, spacing, this.getMinimalWidthForWrapping(spacing));
    }

    private double getMinimalWidthForWrapping(StateSpacing spacing) {
		if(this.getWidthWithoutMargins() > this.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING) {
			return MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING;
		} else {
			return this.getWidthWithoutMargins() - spacing.getRequiredSpaceLeft() - spacing.getRequiredSpaceRight();
		}
	}

	@Override
    public Position getOpenPosition(double requiredSpaceAbove, double requiredSpaceBelow, StateSpacing spacing, double requiredWidth) {
        double posHeight = getHeight() - filledHeight - requiredSpaceAbove;
        return this.getOpenPosition(0, posHeight, requiredSpaceAbove, requiredSpaceBelow, spacing, requiredWidth);
    }

    @Override
    public Position getOpenPosition(double positionWidth, double positionHeight, double requiredSpaceAbove, double requiredSpaceBelow, StateSpacing spacing,
            double requiredWidth) {
        boolean openPositionFound = false;
        int marginBottom = getMarginBottom();
        double potentialHeight = positionHeight;
        if (requiredWidth == 0) {
            requiredWidth = this.getMinimalWidthForWrapping(spacing);
        }
        if (positionHeight > marginBottom) {
            double potentialWidth = positionWidth + getMarginLeft();
            Position position = new Position(potentialWidth, potentialHeight);
            while (!openPositionFound) {
            	System.out.println(getWidestOpenSpaceOn(position, requiredSpaceAbove, requiredSpaceBelow, spacing) >= (requiredWidth));
                if (getWidestOpenSpaceOn(position, requiredSpaceAbove, requiredSpaceBelow, spacing) >= (requiredWidth)) {
                    return position;
                }
                System.out.println("potential height: " + potentialHeight);
                System.out.println("Required space below: " + requiredSpaceBelow);
                System.out.println(potentialHeight - requiredSpaceBelow <= marginBottom);
                if (potentialHeight - requiredSpaceBelow <= marginBottom) {
                    return null;
                }
                potentialHeight -= getLeading();
                position = new Position(potentialWidth, potentialHeight);
            }
        }
        return null;
    }

    /**
     * Returns the width of the largest open space on the given position.
     * @param position Position to check.
     * @param requiredSpaceAbove Free space required above the given position.
     * @param requiredSpaceBelow Free space required below the given position.
     * @return double containing the width.
     */
    private double getWidestOpenSpaceOn(Position position, double requiredSpaceAbove, double requiredSpaceBelow, StateSpacing spacing) {
        int maxWidth = 0;
        for (int[] openSpace : this.getOpenSpacesOn(position, true, requiredSpaceAbove, requiredSpaceBelow, spacing)) {
            maxWidth = Math.max(maxWidth, openSpace[1] - openSpace[0]);
        }
        return maxWidth;
    }

    @Override
    public int getTotalAvailableWidth(Position position, double requiredSpaceAbove, double requiredSpaceBelow, StateSpacing spacing) {
        int availableWidth = 0;
        for (int[] openSpace : this.getOpenSpacesOn(position, true, requiredSpaceAbove, requiredSpaceBelow, spacing)) {
            availableWidth += openSpace[1] - openSpace[0];
        }
        return availableWidth;
    }

    @Override
    public int getAvailableHeight(Position position, double requiredSpaceAbove, double requiredSpaceBelow) {
        int availableHeight = -1;
        for (DocumentPart p : getContent()) {
            if (p instanceof PlaceableDocumentPart) {
                Position pos = ((PlaceableDocumentPart) p).getPosition();
                if (pos.getY() < position.getY()) {
                    availableHeight = (int) Math.max(availableHeight, position.getY() - pos.getY());
                }
            }
        }
        if (availableHeight == -1) {
            availableHeight = (int) (position.getY() - this.getMarginBottom());
        }
        return availableHeight;
    }

    /**
     * Returns all document parts that overlap with the given position.
     * @param pos Position to check.
     * @param requiredSpaceAbove Required free space above the given position.
     * @param requiredSpaceBelow Required free space below the given position.
     * @return List of document parts that overlap with the given position.
     */
    private List<StatePlaceableDocumentPart> getPartsOnLine(Position pos, double requiredSpaceAbove, double requiredSpaceBelow, StateSpacing spacing) {
        List<StatePlaceableDocumentPart> contentOnSameLine = new ArrayList<StatePlaceableDocumentPart>();
        List<DocumentPart> pageContent = new LinkedList<DocumentPart>(getContent());
        for (int i = 0; i < pageContent.size(); ++i) {
            DocumentPart p = pageContent.get(i);
            if (p instanceof StatePlaceableDocumentPart && !p.equals(spacing)) {
                if (DocumentPartType.PARAGRAPH.equals(p.getType())) {
                    Paragraph paragraph = (Paragraph) p;
                    pageContent.addAll(i + 1, paragraph.getTextCollection());
                } else {
                    StatePlaceableDocumentPart part = (StatePlaceableDocumentPart) p;
                    if (onSameLine(pos, requiredSpaceAbove, requiredSpaceBelow, spacing, part)) {
                        contentOnSameLine.add(part);
                    }
                }
            }
        }
        return contentOnSameLine;
    }

    /**
     * Checks if the given position overlaps with the position of the given part.
     * @param position Position to check.
     * @param requiredSpaceAbove The free space required above the position.
     * @param requiredSpaceBelow The free space required below the position.
     * @param part Part to compare the position with.
     * @return true if the given position overlaps with the given part, false otherwise.
     */
    private boolean onSameLine(Position position, double requiredSpaceAbove, double requiredSpaceBelow, StateSpacing ignoreObj, StatePlaceableDocumentPart part) {
        if (part.getPosition().hasCustomPosition()) {
            double topLimit = part.getPosition().getY() + part.getRequiredSpaceAbove();
            double bottomLimit = part.getPosition().getY() - part.getContentHeight(this) - part.getMarginBottom();// + part.getRequiredSpaceAbove();
            double y = position.getY();
            double positionTopLimit = y + requiredSpaceAbove;
            double positionBottomLimit = y - requiredSpaceBelow;
            System.out.println("    Top Limit: " + topLimit);
            System.out.println("    Bottom Limit: " + bottomLimit);
            System.out.println("    Y: " + y);
            System.out.println("    positionTopLimit: " + positionTopLimit);
            System.out.println("    positionBottomLimit: " + positionBottomLimit);

            if ((FloatEqualityTester.lessThanOrEqualTo(y, topLimit) && FloatEqualityTester.greaterThanOrEqualTo(y, bottomLimit))
                    || (FloatEqualityTester.greaterThanOrEqualTo(positionTopLimit, bottomLimit) && FloatEqualityTester.lessThanOrEqualTo(positionTopLimit,
                            topLimit))
                    || (FloatEqualityTester.lessThanOrEqualTo(positionBottomLimit, topLimit) && FloatEqualityTester.greaterThanOrEqualTo(positionBottomLimit,
                            bottomLimit))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<int[]> getOpenSpacesOn(Position pos, boolean ignoreSpacesBeforePositionWidth, double requiredSpaceAbove, double requiredSpaceBelow,
            StateSpacing spacing) {
        List<StatePlaceableDocumentPart> parts = this.getPartsOnLine(pos, requiredSpaceAbove, requiredSpaceBelow, spacing);
        List<int[]> openSpaces = new ArrayList<int[]>();

        //this might cause trouble with fixed position stuff beyond the margins
        int startingPoint = getMarginLeft();
        if (ignoreSpacesBeforePositionWidth) {
            startingPoint = (int) (pos.getX() + spacing.getRequiredSpaceLeft());
        }
        openSpaces.add(new int[] { startingPoint, (int) ((getWidth() - getMarginRight()) - spacing.getRequiredSpaceRight()) });
        for (StatePlaceableDocumentPart part : parts) {
            for (int[] usedSpace : getUsedSpacesFrom(part, pos, requiredSpaceAbove, requiredSpaceBelow)) {
                openSpaces = adjustOpenSpaces(openSpaces, usedSpace);
            }
        }
        return openSpaces;
    }

    @Override
    public List<int[]> getOpenSpacesIncludingHeight(Position pos, boolean ignoreSpacesBeforePositionWidth, double requiredSpaceAbove,
            double requiredSpaceBelow, StateSpacing spacing) {
        if (pos != null) {
            List<int[]> openSpaces = this.getOpenSpacesOn(pos, ignoreSpacesBeforePositionWidth, requiredSpaceAbove, requiredSpaceBelow, spacing);
            for (DocumentPart p : getContent()) {
                if (p instanceof StatePlaceableDocumentPart) {
                    StatePlaceableDocumentPart part = ((StatePlaceableDocumentPart) p);
                    Position position = part.getPosition();
                    if (position.getY() < pos.getY()) {
                        for (int[] usedSpace : part.getUsedSpaces(position.getY(), this.getWidth())) {
                            openSpaces = adjustOpenSpaces(openSpaces, usedSpace);
                            int i = 0;
                            boolean usedSpaceAdded = false;
                            while (!usedSpaceAdded && i < openSpaces.size()) {
                                int[] openSpace = openSpaces.get(i);
                                if (openSpace[1] == usedSpace[0]) {
                                    int[] newOpenSpace = new int[] { usedSpace[0], usedSpace[1], (int) (pos.getY() - position.getY()) };
                                    openSpaces.add(openSpaces.indexOf(openSpace) + 1, newOpenSpace);
                                    usedSpaceAdded = true;
                                }
                                ++i;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < openSpaces.size(); ++i) {
                int[] openSpace = openSpaces.get(i);
                //if the open spaces does not include height of the open space
                if (openSpace.length != 3) {
                    int[] newOpenSpace = new int[] { openSpace[0], openSpace[1], (int) (pos.getY() - this.getMarginBottom()) };
                    openSpaces.set(i, newOpenSpace);
                }
            }
            return openSpaces;
        }
        return new LinkedList<int[]>();
    }

    /**
     * Returns the used spaces from the given document part on the given position.
     * @param part Part to get used spaces from.
     * @param pos The position to check.
     * @param requiredSpaceAbove The space required above the given position.
     * @param requiredSpaceBelow The space required below the given position.
     * @return List of int arrays containing the used spaces. Each array contains two int values specifying the start and ending x-value of a used space.
     */
    private List<int[]> getUsedSpacesFrom(StatePlaceableDocumentPart part, Position pos, double requiredSpaceAbove, double requiredSpaceBelow) {
        List<int[]> usedSpaces = new ArrayList<int[]>();
        double[] heights = new double[] { pos.getY(), pos.getY() + requiredSpaceAbove, pos.getY() - requiredSpaceBelow };
        for (double y : heights) {
            usedSpaces.addAll(part.getUsedSpaces(y, this.getWidth()));
        }
        return usedSpaces;
    }

    /**
     * Processes the given used space by adjusting all the given open spaces. If an open space and used space overlap the open space will be adjusted.
     * @param openSpaces Open spaces to process.
     * @param usedSpace Used space to process.
     * @return List of open spaces that have been adjusted for the given usedSpace.
     */
    private List<int[]> adjustOpenSpaces(List<int[]> openSpaces, int[] usedSpace) {
        List<int[]> newOpenSpaces = new ArrayList<int[]>();
        for (int[] openSpace : openSpaces) {
            if (usedSpace[0] >= openSpace[0] && usedSpace[1] <= openSpace[1]) {
                if (openSpace[0] != usedSpace[0]) {
                    addOpenSpaceToList(newOpenSpaces, new int[] { openSpace[0], usedSpace[0] });
                }
                if (usedSpace[1] != openSpace[1]) {
                    addOpenSpaceToList(newOpenSpaces, new int[] { usedSpace[1], openSpace[1] });
                }
            } else if (usedSpace[0] < openSpace[0] && usedSpace[1] > openSpace[0] && !(usedSpace[1] > openSpace[1])) {
                addOpenSpaceToList(newOpenSpaces, new int[] { usedSpace[1], openSpace[1] });
            } else if (usedSpace[0] < openSpace[1] && usedSpace[1] > openSpace[1] && !(usedSpace[0] < openSpace[0])) {
                addOpenSpaceToList(newOpenSpaces, new int[] { openSpace[0], usedSpace[0] });
            } else if (!(usedSpace[0] < openSpace[0] && usedSpace[1] > openSpace[1])) {
                addOpenSpaceToList(newOpenSpaces, openSpace);
            }
        }
        return newOpenSpaces;
    }

    /**
     * Adds the given openSpace to the given list of open spaces.
     * @param openSpaces List of open spaces.
     * @param newOpenSpace Open space to add.
     * @return List containing the existing open spaces and the added open space (if valid).
     */
    private List<int[]> addOpenSpaceToList(List<int[]> openSpaces, int[] newOpenSpace) {
        if (newOpenSpace[0] != newOpenSpace[1]) {
            openSpaces.add(newOpenSpace);
        }
        return openSpaces;
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
}
