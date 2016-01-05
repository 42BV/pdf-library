package org.toucanpdf.state;

import org.toucanpdf.api.BaseImage;
import org.toucanpdf.model.DocumentPart;
import org.toucanpdf.model.Image;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.state.StateCellContent;

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
        double requiredHeight = getRequiredHeight();
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
    public double getSpecifiedWidth() {
        return this.width;
    }

    @Override
    public double getTotalRequiredWidth() {
        return this.getRequiredWidth();
    }

    @Override
    public double getMinimumWidth() {
        return this.getRequiredWidth();
    }

    @Override
    public void processVerticalAlignment(double height) {
        double diff = height - getRequiredHeight();
        if (diff > 0) {
            this.setPosition(new Position(this.getPosition().getX(), this.getPosition().getY() - (diff / 2)));
        }
    }

    @Override
    public double getRequiredSpaceLeft() {
        return this.marginLeft;
    }

    @Override
    public double getRequiredSpaceRight() {
        return this.marginRight;
    }

    private double getRequiredHeight() {
        return this.getRequiredSpaceAbove() + this.getRequiredSpaceBelow();
    }
}