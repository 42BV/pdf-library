package nl.mad.toucanpdf.state;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.api.BaseImage;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.StateImage;
import nl.mad.toucanpdf.model.StatePage;

public class BaseStateImage extends BaseImage implements StateImage {

    public BaseStateImage(int height, int width, InputStream imageStream, ImageType type) {
        super(height, width, imageStream, type);
    }

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
        //TODO: make wrapping an attribute of PlaceableFixedSizeDocumentPart?  
        this.processContentSize(page, true);
    }

    @Override
    public void processContentSize(StatePage page, boolean wrapping) {
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
                this.setPosition(pos);
            }
        }
        if (!wrapping) {
            adjustFilledHeight(page);
        }
    }

    private void adjustFilledHeight(StatePage page) {
        page.setFilledHeight(page.getFilledHeight() + this.getHeight());
    }
}
