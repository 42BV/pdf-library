package org.toucanpdf.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.toucanpdf.api.BasePage;
import org.toucanpdf.model.DocumentPart;
import org.toucanpdf.model.DocumentPartType;
import org.toucanpdf.model.Page;
import org.toucanpdf.model.Paragraph;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Space;
import org.toucanpdf.model.state.StatePage;
import org.toucanpdf.model.state.StatePlaceableDocumentPart;
import org.toucanpdf.model.state.StateSpacing;
import org.toucanpdf.utility.FloatEqualityTester;

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
        marginTop(this.getMarginTop());
    }

    @Override
    public Page marginTop(int marginTop) {
        if (FloatEqualityTester.equals(filledHeight, this.getMarginTop()) || FloatEqualityTester.equals(filledHeight, 0)) {
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
        if (this.getWidthWithoutMargins() > Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING) {
            return Page.MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING;
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
        boolean positionDetermined = false;
        int marginBottom = getMarginBottom();
        double potentialHeight = positionHeight;
        if (requiredWidth == 0) {
            requiredWidth = this.getMinimalWidthForWrapping(spacing);
        }

        Position position = null;
        if (positionHeight > marginBottom) {
            double potentialWidth = positionWidth + getMarginLeft();
            position = new Position(potentialWidth, potentialHeight);
            while (!positionDetermined) {
                if (potentialHeight - requiredSpaceBelow <= marginBottom) {
                    positionDetermined = true;
                    position = null;
                } else if (getWidestOpenSpaceOn(position, requiredSpaceAbove, requiredSpaceBelow, spacing) >= (requiredWidth)) {
                    positionDetermined = true;
                }

                if (!positionDetermined) {
                    potentialHeight -= getLeading();
                    position = new Position(potentialWidth, potentialHeight);
                }
            }
        }
        return position;
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
        for (Space openSpace : this.getOpenSpacesOn(position, true, requiredSpaceAbove, requiredSpaceBelow, spacing)) {
            maxWidth = Math.max(maxWidth, openSpace.getEndPoint() - openSpace.getStartPoint());
        }
        return maxWidth;
    }

    @Override
    public int getTotalAvailableWidth(Position position, double requiredSpaceAbove, double requiredSpaceBelow, StateSpacing spacing) {
        int availableWidth = 0;
        for (Space openSpace : this.getOpenSpacesOn(position, true, requiredSpaceAbove, requiredSpaceBelow, spacing)) {
            availableWidth += openSpace.getEndPoint() - openSpace.getStartPoint();
        }
        return availableWidth;
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
        if (part.getPosition().hasCustomPosition() && !part.equals(ignoreObj)) {
            double topLimit = part.getPosition().getY() + part.getRequiredSpaceAbove();
            double bottomLimit = part.getPosition().getY() - part.getContentHeight(this) - part.getMarginBottom();// + part.getRequiredSpaceAbove();
            double y = position.getY();
            double positionTopLimit = y + requiredSpaceAbove;
            double positionBottomLimit = y - requiredSpaceBelow;

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
    public List<Space> getOpenSpacesOn(Position pos, boolean ignoreSpacesBeforePositionWidth, double requiredSpaceAbove, double requiredSpaceBelow,
            StateSpacing spacing) {
        List<StatePlaceableDocumentPart> parts = this.getPartsOnLine(pos, requiredSpaceAbove, requiredSpaceBelow, spacing);
        List<Space> openSpaces = new ArrayList<Space>();

        int startingPoint = getMarginLeft();
        if (ignoreSpacesBeforePositionWidth) {
            startingPoint = (int) (pos.getX() + spacing.getRequiredSpaceLeft());
        }
        openSpaces.add(new Space(startingPoint, (int) ((getWidth() - getMarginRight()) - spacing.getRequiredSpaceRight())));
        for (StatePlaceableDocumentPart part : parts) {
            for (Space usedSpace : getUsedSpacesFrom(part, pos, requiredSpaceAbove, requiredSpaceBelow)) {
                openSpaces = adjustOpenSpaces(openSpaces, usedSpace);
            }
        }
        return openSpaces;
    }

    @Override
    public List<Space> getOpenSpacesIncludingHeight(Position pos, boolean ignoreSpacesBeforePositionWidth, double requiredSpaceAbove,
            double requiredSpaceBelow, StateSpacing spacing) {
        if (pos != null) {
            List<Space> openSpaces = this.getOpenSpacesOn(pos, ignoreSpacesBeforePositionWidth, requiredSpaceAbove, requiredSpaceBelow, spacing);
            for (DocumentPart p : getContent()) {
                openSpaces = getOpenSpacesIncludingHeightForDocumentPart(pos, openSpaces, p);
            }
            for (int i = 0; i < openSpaces.size(); ++i) {
                Space openSpace = openSpaces.get(i);
                //if the open spaces does not include height of the open space
                if (openSpace.getHeight() == null) {
                    Space newOpenSpace = new Space(openSpace.getStartPoint(), openSpace.getEndPoint(), (int) (pos.getY() - this.getMarginBottom()));
                    openSpaces.set(i, newOpenSpace);
                }
            }
            return openSpaces;
        }
        return new LinkedList<Space>();
    }

    private List<Space> getOpenSpacesIncludingHeightForDocumentPart(Position pos, List<Space> openSpaces, DocumentPart p) {
        if (p instanceof StatePlaceableDocumentPart) {
            StatePlaceableDocumentPart part = ((StatePlaceableDocumentPart) p);
            Position position = part.getPosition();
            if (position.getY() < pos.getY()) {
                for (Space usedSpace : part.getUsedSpaces(position.getY(), this.getWidth())) {
                    openSpaces = adjustOpenSpaces(openSpaces, usedSpace);
                    int i = 0;
                    boolean usedSpaceAdded = false;
                    while (!usedSpaceAdded && i < openSpaces.size()) {
                        Space openSpace = openSpaces.get(i);
                        if (openSpace.getEndPoint() == usedSpace.getStartPoint()) {
                            Space newOpenSpace = new Space(usedSpace.getStartPoint(), usedSpace.getEndPoint(), (int) (pos.getY() - position.getY()));
                            openSpaces.add(openSpaces.indexOf(openSpace) + 1, newOpenSpace);
                            usedSpaceAdded = true;
                        }
                        ++i;
                    }
                }
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
    private List<Space> getUsedSpacesFrom(StatePlaceableDocumentPart part, Position pos, double requiredSpaceAbove, double requiredSpaceBelow) {
        List<Space> usedSpaces = new ArrayList<Space>();
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
    private List<Space> adjustOpenSpaces(List<Space> openSpaces, Space usedSpace) {
        List<Space> newOpenSpaces = new ArrayList<Space>();
        for (Space openSpace : openSpaces) {
            if (usedSpaceWithinOpenSpace(usedSpace, openSpace)) {
                //if the two spaces do not start on the same point, we have a new open space between the starting points of the two spaces
                if (openSpace.getStartPoint() != usedSpace.getStartPoint()) {
                    addOpenSpaceToList(newOpenSpaces, new Space(openSpace.getStartPoint(), usedSpace.getStartPoint()));
                }
                //same here for the end points
                if (usedSpace.getEndPoint() != openSpace.getEndPoint()) {
                    addOpenSpaceToList(newOpenSpaces, new Space(usedSpace.getEndPoint(), openSpace.getEndPoint()));
                }
            } else if (usedSpaceStartsBeforeOpenSpaceAndEndsWithinOpenSpace(usedSpace, openSpace)) {
                addOpenSpaceToList(newOpenSpaces, new Space(usedSpace.getEndPoint(), openSpace.getEndPoint()));
            } else if (usedSpaceStartsWithinOpenSpaceAndEndsAfterOpenSpace(usedSpace, openSpace)) {
                addOpenSpaceToList(newOpenSpaces, new Space(openSpace.getStartPoint(), usedSpace.getStartPoint()));
            } else if (!usedSpaceStartsBeforeOpenSpaceAndEndsAfterOpenSpace(usedSpace, openSpace)) {
                addOpenSpaceToList(newOpenSpaces, openSpace);
            }
        }
        return newOpenSpaces;
    }

    private boolean usedSpaceStartsWithinOpenSpaceAndEndsAfterOpenSpace(Space usedSpace, Space openSpace) {
        return usedSpace.getStartPoint() < openSpace.getEndPoint() && usedSpace.getEndPoint() > openSpace.getEndPoint()
                && !(usedSpace.getStartPoint() < openSpace.getStartPoint());
    }

    private boolean usedSpaceWithinOpenSpace(Space usedSpace, Space openSpace) {
        return usedSpace.getStartPoint() >= openSpace.getStartPoint() && usedSpace.getEndPoint() <= openSpace.getEndPoint();
    }

    private boolean usedSpaceStartsBeforeOpenSpaceAndEndsWithinOpenSpace(Space usedSpace, Space openSpace) {
        return usedSpace.getStartPoint() < openSpace.getStartPoint() && usedSpace.getEndPoint() > openSpace.getStartPoint()
                && !(usedSpace.getEndPoint() > openSpace.getEndPoint());
    }

    private boolean usedSpaceStartsBeforeOpenSpaceAndEndsAfterOpenSpace(Space usedSpace, Space openSpace) {
        return (usedSpace.getStartPoint() < openSpace.getStartPoint() && usedSpace.getEndPoint() > openSpace.getEndPoint());
    }

    /**
     * Adds the given openSpace to the given list of open spaces.
     * @param openSpaces List of open spaces.
     * @param newOpenSpace Open space to add.
     * @return List containing the existing open spaces and the added open space (if valid).
     */
    private List<Space> addOpenSpaceToList(List<Space> openSpaces, Space newOpenSpace) {
        if (newOpenSpace.getStartPoint() != newOpenSpace.getEndPoint()) {
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
