package org.toucanpdf.state;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.toucanpdf.api.BaseImage;
import org.toucanpdf.model.DocumentPart;
import org.toucanpdf.model.Image;
import org.toucanpdf.model.ImageType;
import org.toucanpdf.model.Page;
import org.toucanpdf.model.PlaceableDocumentPart;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Space;
import org.toucanpdf.model.state.StateImage;
import org.toucanpdf.model.state.StatePage;
import org.toucanpdf.utility.FloatEqualityTester;

/**
 * This is the base implementation of the StateImage interface. This class offers the same functionality as the 
 * BaseImage class, but also offers methods for positioning. 
 * @author Dylan de Wolff
 */
public class BaseStateImage extends BaseImage implements StateImage {
    private DocumentPart originalObject;

    /**
     * Creates a new instance of BaseStateImage.
     * @param height Custom height to use for the image.
     * @param width Custom width to use for the image.
     * @param imageStream InputStream with the image file.
     * @param type Format of the image.
     */
    public BaseStateImage(int height, int width, InputStream imageStream, ImageType type) {
        super(height, width, imageStream, type);
    }

    /**
     * Creates a copy of the given Image object.
     * @param image object to copy.
     */
    public BaseStateImage(Image image) {
        super(image);
    }

    @Override
    public PlaceableDocumentPart copy() {
        return new BaseStateImage(this);
    }

    @Override
    public double getContentHeight(Page page) {
        return this.getHeight();
    }

    @Override
    public double getContentWidth(Page page, Position position) {
        return this.getWidth();
    }

    @Override
    public int[] getPositionAt(double height) {
        if (isImageOnTheGivenHeight(height)) {
            return new int[] { (int) getPosition().getX() };
        }
        return new int[] {};
    }

    private boolean isImageOnTheGivenHeight(double height) {
        Position pos = getPosition();
        return FloatEqualityTester.lessThanOrEqualTo(height, pos.getY() + this.getRequiredSpaceAbove())
                && FloatEqualityTester.greaterThanOrEqualTo(height, pos.getY() - this.getRequiredSpaceBelow());
    }

    @Override
    public List<Space> getUsedSpaces(double height, int pageWidth) {
        List<Space> space = new LinkedList<>();
        if (isImageOnTheGivenHeight(height)) {
            if (isWrappingAllowed()) {
                space.add(new Space((int) this.getPosition().getX() - marginLeft, (int) (this.getPosition().getX() + getWidth() + marginRight)));
            } else {
                space.add(new Space (0, pageWidth));
            }
        }
        return space;
    }

    @Override
    public double getRequiredSpaceAbove() {
        return marginTop;
    }

    @Override
    public double getRequiredSpaceBelow() {
        return this.getHeight() + marginBottom;
    }

    @Override
    public boolean processContentSize(StatePage page) {
        return this.processContentSize(page, this.isWrappingAllowed(), true, false);
    }

    @Override
    public boolean processContentSize(StatePage page, boolean wrapping, boolean processAlignment, boolean fixed) {
        if (!fixed) {
            double requiredSpaceAbove = this.getRequiredSpaceAbove();
            double requiredSpaceBelow = this.getRequiredSpaceBelow();
            Position pos = new Position(this.getPosition());
            List<Space> openSpaces = page.getOpenSpacesIncludingHeight(pos, true, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this);
            boolean imagePositioned = false;
            while (pos != null && !imagePositioned) {
                imagePositioned = PlaceImage(processAlignment, pos, openSpaces);
                if (!imagePositioned) {
                    requiredSpaceAbove += page.getLeading();
                    pos = page.getOpenPosition(requiredSpaceAbove, requiredSpaceBelow, this, this.width);
                    openSpaces = page.getOpenSpacesIncludingHeight(pos, true, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this);
                }
            }
            if (pos != null) {
                if (!wrapping) {
                    adjustFilledHeight(page);
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean PlaceImage(boolean processAlignment, Position pos, List<Space> openSpaces) {
        int i = 0;
        boolean positionFound = false;
        while (!positionFound && i < openSpaces.size()) {
            Space openSpace = openSpaces.get(i);
            if (pos.getX() < openSpace.getStartPoint()) {
                pos.setX(openSpace.getStartPoint());
            }
            int openSpaceWidth = (openSpace.getEndPoint() - openSpace.getStartPoint());
            if (openSpaceWidth >= this.getWidth() && openSpace.getHeight() >= this.getHeight()) {
                positionFound = true;
                if (processAlignment) {
                    this.processAlignment(pos, openSpaceWidth);
                }
                this.on(new Position(pos));
            }
            ++i;
        }
        return positionFound;
    }

    private void adjustFilledHeight(StatePage page) {
        page.setFilledHeight(page.getFilledHeight() + this.getRequiredSpaceAbove() + this.getRequiredSpaceBelow() + Page.DEFAULT_NEW_LINE_SIZE * 2);
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

    private void processAlignment(Position pos, int openSpaceWidth) {
        double remainder = openSpaceWidth - this.getWidth();
        double adjustment = 0;
        if (remainder > 0) {
            switch (this.getAlignment()) {
            case CENTERED:
                adjustment += remainder / 2;
                break;
            case RIGHT:
                adjustment += remainder;
                break;
            case LEFT:
            case JUSTIFIED:
            default:
                break;
            }
        }
        pos.adjustX(adjustment);
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
