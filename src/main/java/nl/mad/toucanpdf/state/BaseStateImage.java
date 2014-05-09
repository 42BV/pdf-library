package nl.mad.toucanpdf.state;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.api.BaseImage;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.StateImage;
import nl.mad.toucanpdf.model.StatePage;

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
    public int getContentWidth(Page page, Position position) {
        return this.getWidth();
    }

    @Override
    public int[] getPositionAt(double height) {
        return new int[] { (int) this.getPosition().getX() };
    }

    @Override
    public List<int[]> getUsedSpaces(double height) {
        List<int[]> space = new LinkedList<int[]>();
        space.add(new int[] { (int) this.getPosition().getX(), (int) (this.getPosition().getX() + getWidth()) });
        return space;
    }

    @Override
    public double getRequiredSpaceAbove() {
        return 0;
    }

    @Override
    public double getRequiredSpaceBelow() {
        return this.getHeight();
    }

    @Override
    public void processContentSize(StatePage page) {
        this.processContentSize(page, this.wrappingAllowed(), true);
    }

    @Override
    public void processContentSize(StatePage page, boolean wrapping, boolean processAlignment) {
        double requiredSpaceAbove = this.getRequiredSpaceAbove();
        double requiredSpaceBelow = this.getRequiredSpaceBelow();
        Position pos = new Position(this.getPosition());
        List<int[]> openSpaces = page.getOpenSpacesOn(pos, true, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow());
        int i = 0;
        boolean imagePositioned = false;
        while (!imagePositioned && i < openSpaces.size()) {
            int[] openSpace = openSpaces.get(i);
            if (pos.getX() < openSpace[0]) {
                pos.setX(openSpace[0]);
            }
            int openSpaceWidth = (openSpace[1] - openSpace[0]);
            if (openSpaceWidth >= this.getWidth() && (page.getAvailableHeight(pos, requiredSpaceAbove, requiredSpaceBelow) >= this.getHeight())) {
                imagePositioned = true;
                if (processAlignment) {
                    this.processAlignment(pos, openSpaceWidth);
                }
                this.setPosition(pos);
            }
            ++i;
        }
        if (!wrapping) {
            adjustFilledHeight(page);
        }
    }

    private void adjustFilledHeight(StatePage page) {
        page.setFilledHeight(page.getFilledHeight() + this.getHeight() + Page.DEFAULT_NEW_LINE_SIZE * 2);
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
        int remainder = openSpaceWidth - this.getWidth();
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
}
