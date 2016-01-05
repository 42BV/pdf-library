package org.toucanpdf.state;

import org.toucanpdf.api.AbstractCell;
import org.toucanpdf.model.Cell;
import org.toucanpdf.model.Image;
import org.toucanpdf.model.PlaceableDocumentPart;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Text;
import org.toucanpdf.model.state.StateCell;
import org.toucanpdf.model.state.StateCellContent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseStateCell extends AbstractCell implements StateCell {
    private StateCellContent content;
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseStateCell.class);

    public BaseStateCell(Cell c) {
        super(c);
        this.content = convertContent(c.getContent());
    }

    public BaseStateCell() {
        super();
    }

    private StateCellContent convertContent(PlaceableDocumentPart part) {
        if (part != null) {
            switch (part.getType()) {
            case TEXT:
                return new BaseStateCellText((Text) part);
            case IMAGE:
                return new BaseStateCellImage((Image) part);
            default:
                LOGGER.warn("The given object type: " + part.getType() + " is not supported within cells.");
                return null;
            }
        }
        return null;
    }

    @Override
    public double getRequiredHeight(double leading) {
        return Math.max(height, calculateContentHeight(leading, false));
    }

    @Override
    public void processContentSize(double leading) {
        this.calculateContentHeight(leading, true);
    }

    private double calculateContentHeight(double leading, boolean processPositioning) {
        double height = 0;
        if (this.content != null) {
            Position positionInclBorder = (this.getPosition() != null) ? new Position(this.getPosition().getX() + (this.border / 2) + padding,
                    this.getPosition().getY() - (this.border / 2) - padding) : null;
            height = this.content.calculateContentHeight(this.getWidth() - (padding * 2), leading, positionInclBorder, processPositioning);
        }

        if (padding != null) {
            height += padding * 2;
        }
        if (border != null) {
            height += border;
        }
        return height;
    }

    @Override
    public Cell setPosition(Position position) {
        this.position = position;
        return this;
    }

    @Override
    public PlaceableDocumentPart getContent() {
        return this.content;
    }

    @Override
    public StateCellContent getStateCellContent() {
        return this.content;
    }

    @Override
    public double getRequiredWidth() {
        if (this.content != null) {
            return Math.max(width, this.content.getRequiredWidth()) + (padding * 2);
        }
        return width;
    }

    @Override
    public void setContent(StateCellContent content) {
        this.content = content;
    }

    @Override
    public void processVerticalAlignment() {
        if (content != null) {
            content.processVerticalAlignment(this.height - (padding * 2) - border);
        }
    }

    @Override
    public Cell content(PlaceableDocumentPart part) {
        if (part instanceof StateCellContent) {
            this.setContent((StateCellContent) part);
        }
        return this;
    }
}
