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
import nl.mad.toucanpdf.model.StatePage;
import nl.mad.toucanpdf.model.StatePlaceableDocumentPart;
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
    public Position getOpenPosition() {
        return this.getOpenPosition(0, 0);
    }

    @Override
    public Position getOpenPosition(double requiredSpaceAbove, double requiredSpaceBelow) {
        return this.getOpenPosition(requiredSpaceAbove, requiredSpaceBelow, MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING);
    }

    @Override
    public Position getOpenPosition(double requiredSpaceAbove, double requiredSpaceBelow, double requiredWidth) {
        double posHeight = getHeight() - filledHeight - requiredSpaceAbove;
        return this.getOpenPosition(0, posHeight, requiredSpaceAbove, requiredSpaceBelow, requiredWidth);
    }

    @Override
    public Position getOpenPosition(double positionWidth, double positionHeight, double requiredSpaceAbove, double requiredSpaceBelow, double requiredWidth) {
        boolean openPositionFound = false;
        int marginBottom = getMarginBottom();
        double potentialHeight = positionHeight;
        if (requiredWidth == 0) {
            requiredWidth = MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING;
        }
        if (positionHeight > marginBottom) {
            double potentialWidth = positionWidth + getMarginLeft();
            Position position = new Position(potentialWidth, potentialHeight);
            while (!openPositionFound) {
                if (getWidestOpenSpaceOn(position, requiredSpaceAbove, requiredSpaceBelow) > (requiredWidth)) {
                    return position;
                }
                if (potentialHeight <= marginBottom) {
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
    private double getWidestOpenSpaceOn(Position position, double requiredSpaceAbove, double requiredSpaceBelow) {
        int maxWidth = 0;
        for (int[] openSpace : this.getOpenSpacesOn(position, true, requiredSpaceAbove, requiredSpaceBelow)) {
            maxWidth = Math.max(maxWidth, openSpace[1] - openSpace[0]);
        }
        return maxWidth;
    }

    @Override
    public int getTotalAvailableWidth(Position position, double requiredSpaceAbove, double requiredSpaceBelow) {
        int availableWidth = 0;
        for (int[] openSpace : this.getOpenSpacesOn(position, true, requiredSpaceAbove, requiredSpaceBelow)) {
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
    private List<StatePlaceableDocumentPart> getPartsOnLine(Position pos, double requiredSpaceAbove, double requiredSpaceBelow) {
        List<StatePlaceableDocumentPart> contentOnSameLine = new ArrayList<StatePlaceableDocumentPart>();
        List<DocumentPart> pageContent = new LinkedList<DocumentPart>(getContent());
        for (int i = 0; i < pageContent.size(); ++i) {
            DocumentPart p = pageContent.get(i);
            if (p instanceof StatePlaceableDocumentPart) {
                if (DocumentPartType.PARAGRAPH.equals(p.getType())) {
                    Paragraph paragraph = (Paragraph) p;
                    pageContent.addAll(i + 1, paragraph.getTextCollection());
                } else {
                    StatePlaceableDocumentPart part = (StatePlaceableDocumentPart) p;
                    if (onSameLine(pos, requiredSpaceAbove, requiredSpaceBelow, part)) {
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
    private boolean onSameLine(Position position, double requiredSpaceAbove, double requiredSpaceBelow, StatePlaceableDocumentPart part) {
        if (part.getPosition().hasCustomPosition()) {
            double topLimit = part.getPosition().getY() + part.getRequiredSpaceAbove();
            double bottomLimit = part.getPosition().getY() - part.getContentHeight(this) + part.getRequiredSpaceAbove();
            double y = position.getY();
            double positionTopLimit = y + requiredSpaceAbove;
            double positionBottomLimit = y - requiredSpaceBelow;
            if (part.getType().equals(DocumentPartType.IMAGE)) {
                System.out.println("Image stuff!");
                System.out.println("top limit of image: " + topLimit);
                System.out.println("bottom limit of image: " + bottomLimit);
                System.out.println("pos Y : " + y);
                System.out.println("Position top limit: " + positionTopLimit);
                System.out.println("Position bottom limit: " + positionBottomLimit);
                System.out.println();
            }

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
    public List<int[]> getOpenSpacesOn(Position pos, boolean ignoreSpacesBeforePositionWidth, double requiredSpaceAbove, double requiredSpaceBelow) {
        List<StatePlaceableDocumentPart> parts = this.getPartsOnLine(pos, requiredSpaceAbove, requiredSpaceBelow);
        List<int[]> openSpaces = new ArrayList<int[]>();

        //this might cause trouble with fixed position stuff beyond the margins
        int startingPoint = getMarginLeft();
        if (ignoreSpacesBeforePositionWidth) {
            startingPoint = (int) pos.getX();
        }
        openSpaces.add(new int[] { startingPoint, (getWidth() - getMarginRight()) });
        for (StatePlaceableDocumentPart part : parts) {
            for (int[] usedSpace : getUsedSpacesFrom(part, pos, requiredSpaceAbove, requiredSpaceBelow)) {
                openSpaces = adjustOpenSpaces(openSpaces, usedSpace);
            }
        }
        return openSpaces;
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
            usedSpaces.addAll(part.getUsedSpaces(y));
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
}
