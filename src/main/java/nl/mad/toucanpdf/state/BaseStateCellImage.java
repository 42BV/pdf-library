package nl.mad.toucanpdf.state;

import nl.mad.toucanpdf.api.BaseImage;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.state.StateCellContent;

public class BaseStateCellImage extends BaseImage implements StateCellContent {
    private DocumentPart originalObject;

    public BaseStateCellImage(Image image) {
        super(image);
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
    public double getRequiredSpaceAbove() {
        return marginTop;
    }

    @Override
    public double getRequiredSpaceBelow() {
        return this.height + marginBottom;
    }

    @Override
    public double calculateContentHeight(double availableWidth, double leading, Position position, boolean processPositioning) {
        double requiredSpaceAbove = this.getRequiredSpaceAbove();
        double requiredSpaceBelow = this.getRequiredSpaceBelow();
        double requiredHeight = requiredSpaceAbove + requiredSpaceBelow;
        if (processPositioning) {
            Position pos = new Position(position);
            pos.adjustX(marginLeft);
            pos.adjustY(-requiredSpaceAbove);
            this.setPosition(pos);
        }
        return requiredHeight;
    }

    @Override
    public double getRequiredWidth() {
        return this.width + marginLeft + marginRight;
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