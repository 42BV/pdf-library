package nl.mad.toucanpdf.state;

import nl.mad.toucanpdf.api.AbstractCell;
import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.model.state.StateCell;
import nl.mad.toucanpdf.model.state.StateCellContent;

public class BaseStateCell extends AbstractCell implements StateCell {
    private StateCellContent content;

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
            case PARAGRAPH:
            case TABLE:
            default:
                break;
            }
        }
        return null;
    }

    @Override
    public double getRequiredHeight(double leading, double borderWidth) {
        return Math.max(height, calculateContentHeight(leading, borderWidth, false));
    }

    @Override
    public void processContentSize(double leading, double borderWidth) {
        this.calculateContentHeight(leading, borderWidth, true);
    }

    private double calculateContentHeight(double leading, double borderWidth, boolean processPositioning) {
        if (this.content != null) {
            Position positionInclBorder = new Position(this.getPosition().getX() + borderWidth, this.getPosition().getY() - borderWidth);
            return this.content.calculateContentHeight(this.getWidth() - (borderWidth * 2), leading, positionInclBorder, processPositioning);
        }
        return 0;
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
            return Math.max(width, this.content.getRequiredWidth());
        }
        return width;
    }

    @Override
    public void setContent(StateCellContent content) {
        this.content = content;
    }
}
